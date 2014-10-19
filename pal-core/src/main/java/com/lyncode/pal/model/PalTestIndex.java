package com.lyncode.pal.model;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import com.lyncode.pal.junit.runner.TableRunner;
import com.lyncode.pal.render.model.IndexModel;
import com.lyncode.pal.utils.JtwigUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import static com.lyncode.pal.model.PalTestGroup.fromTestCases;

public class PalTestIndex {
    private static final JtwigConfiguration configuration = new JtwigConfiguration();
    private static final Renderable template = template();
    private static Map<String, PalTestCase> testCases = new TreeMap<String, PalTestCase>();

    private static Renderable template() {
        try {
            return new JtwigTemplate(new ClasspathJtwigResource(""), configuration).compile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void add(Class<?> typeClass) {
        if (!testCases.containsKey(typeClass.getName())) {
            PalTestCase testCase = new PalTestCase(typeClass);
            testCases.put(typeClass.getName(), testCase);
        }
    }

    public static void render() {
        try {
            File outputFile = new File(FileUtils.getTempDirectory(), "index.html");
            String templateLocation = "/pal/templates/pages/index.twig.html";
            Collection<PalTestGroup> palTestGroups = fromTestCases(testCases.values());
            JtwigUtils.renderTo(outputFile, templateLocation,
                    new JtwigModelMap()
                            .withModelAttribute("index", new IndexModel(palTestGroups))
            );

            for (PalTestGroup palTestGroup : palTestGroups) {
                palTestGroup.render();
            }

            System.out.println(String.format("Results: %s", outputFile.getAbsolutePath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static PalTestScenario scenario(TableRunner.DecoratingFrameworkMethod method) {
        return testCases.get(method.getMethod().getDeclaringClass().getName()).scenario(method);
    }
}
