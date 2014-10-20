package org.paltest.pal.model;

import com.lyncode.jtwig.JtwigModelMap;
import org.paltest.pal.render.model.GroupModel;
import org.paltest.pal.utils.JtwigUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

public class PalTestGroup implements Comparable<PalTestGroup> {
    public static Collection<PalTestGroup> fromTestCases(Collection<PalTestCase> testCases) {
        Map<String, List<PalTestCase>> packages = new HashMap<String, List<PalTestCase>>();
        for (PalTestCase testCase : testCases) {
            String name = testCase.getPackage().getName();
            if (!packages.containsKey(name))
                packages.put(name, new ArrayList<PalTestCase>());
            packages.get(name).add(testCase);
        }

        Collection<PalTestGroup> groups = new PriorityQueue<PalTestGroup>();
        for (String aPackage : packages.keySet()) {
            groups.add(new PalTestGroup(aPackage, packages.get(aPackage)));
        }

        return groups;
    }

    private final String aPackage;
    private final List<PalTestCase> testCases;

    public PalTestGroup(String aPackage, List<PalTestCase> palTestCases) {
        this.aPackage = aPackage;
        this.testCases = palTestCases;
    }

    @Override
    public int compareTo(PalTestGroup o) {
        return aPackage.compareTo(o.aPackage);
    }

    public int count(Status status) {
        int sum = 0;
        for (PalTestCase testCase : testCases) {
            sum += testCase.count(status);
        }
        return sum;
    }

    public void render() {
        File outputFile = new File(FileUtils.getTempDirectory(), String.format("%s.html", getPackage()));
        String templateLocation = "/pal/templates/pages/group.twig.html";

        try {
            JtwigUtils.renderTo(outputFile, templateLocation,
                    new JtwigModelMap()
                            .withModelAttribute("group", new GroupModel(this))
            );
            for (PalTestCase testCase : testCases) {
                testCase.render();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PalTestCase> testCases() {
        return testCases;
    }

    public String getPackage() {
        return aPackage;
    }
}
