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

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import com.lyncode.pal.model.TestScenarios;
import com.lyncode.pal.render.RenderException;
import com.lyncode.pal.render.Renderer;

import java.io.File;
import java.io.FileOutputStream;

public class TestScenariosRenderer implements Renderer<TestScenarios> {
    private static final JtwigConfiguration configuration = new JtwigConfiguration();
    private static final JtwigTemplate template =
            new JtwigTemplate(new ClasspathJtwigResource("classpath:/pal/templates/pages/test.twig.html"), configuration);
    private final Renderable compiledTemplate;
    private final File baseDirectory;

    public TestScenariosRenderer(File baseDirectory) {
        this.baseDirectory = baseDirectory;
        try {
            this.compiledTemplate = template.compile();
        } catch (ParseException e) {
            throw new RuntimeException("Unable to compile Jtwig Template");
        } catch (CompileException e) {
            throw new RuntimeException("Unable to compile Jtwig Template");
        }
    }

    @Override
    public void render(TestScenarios scenarios) throws RenderException {
        try {
            File file = scenarios.toFile(baseDirectory);
//            System.out.println("Pal Test:");
//            System.out.println(file.getPath());
            FileOutputStream stream = new FileOutputStream(file);
            JtwigModelMap context = new JtwigModelMap();
            context.withModelAttribute("scenarios", scenarios);
            compiledTemplate.render(RenderContext.create(configuration.render(), context, stream));
        } catch (Exception e) {
            throw new RenderException(e);
        }
    }
}
