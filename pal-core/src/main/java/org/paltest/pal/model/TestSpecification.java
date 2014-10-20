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

package org.paltest.pal.model;

import org.paltest.pal.parser.impl.java8.Java8Parser;

import java.util.ArrayList;
import java.util.List;

public class TestSpecification {
    private List<Java8Parser.FormalParameterContext> parameters = new ArrayList<Java8Parser.FormalParameterContext>();
    private StringBuilder builder = new StringBuilder();

    public void parameters(List<Java8Parser.FormalParameterContext> formalParameterContexts) {
        this.parameters = formalParameterContexts;
    }

    public List<Java8Parser.FormalParameterContext> parameters() {
        return parameters;
    }
    public StringBuilder text() {
        return this.builder;
    }
}
