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

package com.lyncode.pal.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.lyncode.pal.junit.runner.TableRunner;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static java.util.Arrays.asList;

public class TestScenarios extends ArrayList<Scenario> implements Comparable<TestScenarios> {
    private final Class<?> testClass;

    public TestScenarios(Class<?> testClass) {
        this.testClass = testClass;
        Collection<List<Scenario>> scenariosLists = transform(transform(filter(asList(this.testClass.getMethods()), new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable Method x) {
                return x.getAnnotation(Test.class) != null;
            }
        }), new Function<Method, TestMethod>() {
            @Nullable
            @Override
            public TestMethod apply(@Nullable Method input) {
                return new TestMethod(input);
            }
        }), new Function<TestMethod, List<Scenario>>() {
            @Nullable
            @Override
            public List<Scenario> apply(@Nullable TestMethod input) {
                return input.getScenarios();
            }
        });

        for (List<Scenario> scenariosList : scenariosLists) {
            for (Scenario scenario : scenariosList) {
                this.add(scenario);
            }
        }
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public File toFile (File baseDirectory) {
        baseDirectory.mkdirs();
        File file = new File(baseDirectory, testClass.getName() + ".html");
        return file;
    }

    public String getName () {
        return getTestClass().getName().substring(TestIndex.common().length() + 1).replace(".", " &gt; ");
    }

    public long count(final String status) {
        return filter(this, new Predicate<Scenario>() {
            @Override
            public boolean apply(@Nullable Scenario x) {
                return x.status() == Scenario.Status.valueOf(status);
            }
        }).size();
    }

    public Scenario select(TableRunner.DecoratingFrameworkMethod method) {
        for (Scenario scenario : this) {
            if (scenario.getMethod().equals(method.getMethod())) {
                if (method.getRow() == null && scenario.noParameters())
                    return scenario;
                else if (method.getRow() != null && asList(method.getRow().value()).equals(scenario.getParameters()))
                    return scenario;
            }
        }
        return null;
    }

    public Scenario.Status getStatus () {
        Scenario.Status status = Scenario.Status.Ignored;
        for (Scenario scenario : this) {
            if (scenario.status() == Scenario.Status.Failed)
                status = Scenario.Status.Failed;
            else if (status != Scenario.Status.Failed && scenario.status() == Scenario.Status.Passed)
                status = Scenario.Status.Passed;

        }
        return status;
    }

    @Override
    public int compareTo(TestScenarios o) {
        return (o.getStatus().getViewPriority() - this.getStatus().getViewPriority());
    }

    public String toClass () {
        switch (getStatus()) {
            case Passed:
                return "success";
            case Failed:
                return "danger";
            default:
                return "warning";
        }
    }

    @Override
    public int hashCode() {
        return testClass.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TestScenarios) {
            return ((TestScenarios) o).getTestClass().equals(getTestClass());
        }
        return super.equals(o);
    }
}
