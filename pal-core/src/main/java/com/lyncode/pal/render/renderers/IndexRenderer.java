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

package com.lyncode.pal.render.renderers;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.pal.model.TestScenarios;
import com.lyncode.pal.render.RenderException;
import com.lyncode.pal.render.Renderer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class IndexRenderer implements Renderer<List<TestScenarios>> {
    private static final JtwigTemplate template =
            new JtwigTemplate(new ClasspathJtwigResource("classpath:/pal/templates/pages/index.twig.html"));
    private final Content compiledTemplate;
    private final File base;

    public IndexRenderer(File base) {
        this.base = base;
        try {
            this.compiledTemplate = template.compile();
        } catch (ParseException e) {
            throw new RuntimeException("Unable to compile Jtwig Template");
        } catch (CompileException e) {
            throw new RuntimeException("Unable to compile Jtwig Template");
        }
    }

    @Override
    public void render(List<TestScenarios> objectToRender) throws RenderException {
        try {
            File file = new File(base, "index.html");
            System.out.println("Pal Results: ");
            System.out.println(file.getPath());
            FileOutputStream stream = new FileOutputStream(file);
            JtwigContext context = new JtwigContext();
            context.withModelAttribute("scenarios", objectToRender);
            context.withModelAttribute("stats", new Stats(objectToRender));
            compiledTemplate.render(stream, context);
        } catch (Exception e) {
            throw new RenderException(e);
        }
    }

    public static class Stats {
        private final List<TestScenarios> scenarios;

        public Stats(List<TestScenarios> scenarios) {
            this.scenarios = scenarios;
        }

        public int count (String statusName) {
            int count = 0;
            for (TestScenarios scenario : scenarios) {
                count += scenario.count(statusName);
            }
            return count;
        }
    }
}
