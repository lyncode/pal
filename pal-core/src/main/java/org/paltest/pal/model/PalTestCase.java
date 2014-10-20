package org.paltest.pal.model;

import com.lyncode.jtwig.JtwigModelMap;
import org.paltest.pal.junit.annotations.Row;
import org.paltest.pal.junit.annotations.Table;
import org.paltest.pal.junit.runner.TableRunner;
import org.paltest.pal.render.model.TestCaseModel;
import org.paltest.pal.utils.JtwigUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PalTestCase implements Comparable<PalTestCase> {
    private final Class<?> typeClass;
    private List<PalTestScenario> scenarios = new ArrayList<PalTestScenario>();

    public PalTestCase(Class<?> typeClass) {
        this.typeClass = typeClass;

        for (Method method : typeClass.getDeclaredMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                FrameworkMethod frameworkMethod = new FrameworkMethod(method);

                Table table = frameworkMethod.getAnnotation(Table.class);
                if (table != null) {
                    for (Row row : table.value()) {
                        scenarios.add(new PalTestRowScenario(method, row));
                    }
                } else
                    scenarios.add(new PalTestScenario(method));
            }
        }
    }

    public PalTestScenario scenario(TableRunner.DecoratingFrameworkMethod method) {
        for (PalTestScenario scenario : scenarios) {
            if (scenario.generatedBy(method))
                return scenario;
        }
        throw new RuntimeException("Unable to find the scenario you are looking for!");
    }

    public Package getPackage() {
        return typeClass.getPackage();
    }

    @Override
    public int compareTo(PalTestCase o) {
        return new Integer(typeClass.getPackage().getName().length()).compareTo(
                o.typeClass.getPackage().getName().length()
        );
    }

    public int count(Status status) {
        int sum = 0;
        for (PalTestScenario scenario : scenarios) {
            if (scenario.isMarkedAs(status))
                sum++;
        }
        return sum;
    }

    public Class<?> type() {
        return typeClass;
    }

    public void render() {
        File outputFile = new File(FileUtils.getTempDirectory(), String.format("%s.html", type().getName()));
        String templateLocation = "/pal/templates/pages/test.twig.html";

        try {
            JtwigUtils.renderTo(outputFile, templateLocation,
                    new JtwigModelMap()
                            .withModelAttribute("test", new TestCaseModel(this))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PalTestScenario> scenarios() {
        return scenarios;
    }
}
