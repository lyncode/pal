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

import com.lyncode.pal.render.renderers.IndexRenderer;
import com.lyncode.pal.render.renderers.TestScenariosRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestIndex {
    public static final String OUTPUT_DIR = "pal.output.dir";
    private static final IndexRenderer INDEX_RENDERER = new IndexRenderer(outputDirectory());
    private static List<TestScenarios> index = new ArrayList<TestScenarios>();


    public static String common () {
        if (index.isEmpty()) return "";
        TestScenarios[] scenarios = index.toArray(new TestScenarios[index.size()]);
        String prefix = scenarios[0].getTestClass().getPackage().getName();
        for (int i=1;i<scenarios.length;i++)
            prefix = commonPrefix(prefix, scenarios[i].getTestClass().getPackage().getName());

        return prefix;
    }

    private static String commonPrefix (String s1, String s2) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s1.length() && i < s2.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                builder.append(s1.charAt(i));
            }
            else {
                break;
            }
        }
        return builder.toString();
    }

    public static void render() {
        Collections.sort(index);
        TestScenariosRenderer renderer = new TestScenariosRenderer(outputDirectory());
        for (TestScenarios scenarios : index) {
            renderer.render(scenarios);
        }
        INDEX_RENDERER.render(index);
    }

    public static File outputDirectory() {
        return new File(System.getProperty(OUTPUT_DIR, System.getProperty("java.io.tmpdir")));
    }

    public static void add(TestScenarios scenarios) {
        if (!index.contains(scenarios))
            index.add(scenarios);
    }

    private TestIndex () {}
}
