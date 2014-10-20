/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.paltest.pal.junit.runner;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.paltest.pal.PalTest;
import org.paltest.pal.model.PalTestIndex;
import org.paltest.pal.model.PalTestScenario;
import org.paltest.pal.model.Status;
import org.paltest.pal.parser.impl.ClassFileLocatorImpl;
import org.paltest.pal.parser.impl.MethodCodeExtractorImpl;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PalRunner extends TableRunner {
    private static final MethodCodeExtractorImpl methodCodeExtractor = new MethodCodeExtractorImpl(new ClassFileLocatorImpl());

    public static MethodCodeExtractorImpl extractor() {
        return methodCodeExtractor;
    }

    private PalTestScenario currentScenario = null;

    public PalRunner(Class<?> typeClass) throws InitializationError {
        super(typeClass);

        if (!PalTest.class.isAssignableFrom(typeClass))
            throw new IllegalStateException(String.format("Test should extend %s class", PalTest.class.getSimpleName()));


        PalTestIndex.add(typeClass);
        super.setScheduler(new RunnerScheduler() {
            @Override
            public void schedule(Runnable childStatement) {
                childStatement.run();
            }

            @Override
            public void finished() {
                PalTestIndex.render();
            }
        });
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return new ArrayList<FrameworkMethod>(Collections2.filter(super.computeTestMethods(), isNotEvaluateMethod()));
    }

    private static Predicate<FrameworkMethod> isNotEvaluateMethod() {
        return new Predicate<FrameworkMethod>() {
            @Override
            public boolean apply(@Nullable FrameworkMethod input) {
                return !input.getName().equals("evaluate");
            }
        };
    }

    @Override
    public void run(RunNotifier notifier) {
        final RunListener listener = new ResultListener();
        notifier.addListener(listener);
        super.run(notifier);
        notifier.removeListener(listener);
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final Statement statement = super.methodInvoker(method, test);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                currentScenario = PalTestIndex.scenario((DecoratingFrameworkMethod) method);
                if (test instanceof PalTest) {
                    PalTest palTest = (PalTest) test;
                    currentScenario.communicationStore(palTest.communications())
                            .givensStore(palTest.givens())
                            .markAs(Status.Passed)
                            .startTimer();
                    statement.evaluate();
                    currentScenario.stopTimer();
                } else
                    throw new IllegalStateException(String.format("Test should extend %s class", PalTest.class.getSimpleName()));
            }
        };
    }

    private final class ResultListener extends RunListener {
        @Override
        public void testFailure(Failure failure) throws Exception {
            if (currentScenario != null) {
                currentScenario.fail(failure.getException());
                currentScenario.stopTimer();
            }
            super.testFailure(failure);
        }

        @Override
        public void testRunFinished(Result result) throws Exception {
            if (currentScenario != null) {
                if (result.wasSuccessful())
                    currentScenario.markAs(Status.Passed);
            }
            super.testRunFinished(result);
        }
    }
}
