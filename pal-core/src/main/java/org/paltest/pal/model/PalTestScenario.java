package org.paltest.pal.model;

import org.paltest.pal.junit.runner.PalRunner;
import org.paltest.pal.junit.runner.TableRunner;
import org.paltest.pal.syntax.flow.CommunicationStore;
import org.paltest.pal.syntax.given.GivensStore;
import org.paltest.pal.utils.SubtleWordUtils;
import org.paltest.pal.utils.TimeWatch;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class PalTestScenario implements Comparable<PalTestScenario> {
    private final Method method;
    private Status status = Status.Ignored;
    private long elapsedTime = -1;
    private Throwable exception;
    private CommunicationStore communicationStore;
    private GivensStore givensStore;
    private TimeWatch watch;
    private TestSpecification testSpecification;

    public PalTestScenario(Method method) {
        this.method = method;
    }

    public boolean generatedBy(TableRunner.DecoratingFrameworkMethod method) {
        return method.getMethod().equals(this.method);
    }

    public PalTestScenario markAs(Status status) {
        this.status = status;
        return this;
    }

    public PalTestScenario fail(Throwable exception) {
        this.exception = exception;
        this.markAs(Status.Failed);
        return this;
    }

    public boolean isMarkedAs(Status status) {
        return this.status == status;
    }

    public Status status() {
        return status;
    }

    public String name() {
        return SubtleWordUtils.sentencify(method.getName(), true);
    }

    public long timeElapsed() {
        return elapsedTime;
    }


    @Override
    public int compareTo(PalTestScenario o) {
        return (o.status().getViewPriority() - this.status().getViewPriority());
    }

    public String specification() {
        try {
            return extractSpecification().text().toString();
        } catch (Exception e) {
            return String.format("Error: Unable to parse Java Class %s", method.getDeclaringClass().getName());
        }
    }

    public TestSpecification extractSpecification() {
        if (testSpecification == null) {
            try {
                testSpecification = PalRunner.extractor().extract(method);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Error: Unable to parse Java Class %s", method.getDeclaringClass().getName()), e);
            }
        }
        return testSpecification;
    }

    public GivensStore givensStore() {
        return givensStore;
    }

    public CommunicationStore communicationStore () {
        return communicationStore;
    }

    public PalTestScenario startTimer() {
        this.watch = TimeWatch.start();
        return this;
    }

    public PalTestScenario stopTimer() {
        this.elapsedTime = watch.time(TimeUnit.MILLISECONDS);
        return this;
    }

    public PalTestScenario communicationStore(CommunicationStore communications) {
        this.communicationStore = communications;
        return this;
    }

    public PalTestScenario givensStore(GivensStore givens) {
        this.givensStore = givens;
        return this;
    }
}
