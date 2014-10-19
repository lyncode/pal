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

import com.lyncode.pal.junit.annotations.Row;
import com.lyncode.pal.junit.runner.TableRunner;
import com.lyncode.pal.parser.api.MethodCodeExtractor;
import com.lyncode.pal.parser.impl.ClassFileLocatorImpl;
import com.lyncode.pal.parser.impl.MethodCodeExtractorImpl;
import com.lyncode.pal.parser.impl.java8.Java8Parser;
import com.lyncode.pal.syntax.flow.Communication;
import com.lyncode.pal.syntax.flow.CommunicationStore;
import com.lyncode.pal.syntax.given.GivensStore;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.lyncode.pal.utils.SubtleWordUtils.sentencify;
import static java.util.Arrays.asList;

public class Scenario implements Comparable<Scenario> {
    private static final MethodCodeExtractor codeExtractor = new MethodCodeExtractorImpl(new ClassFileLocatorImpl());

    public static Scenario from(Method javaMethod, Row x) {
        return new Scenario(javaMethod, asList(x.value()));
    }
    public static Scenario from(Method javaMethod) {
        return new Scenario(javaMethod, Collections.<String>emptyList());
    }
    public static Scenario from(TableRunner.DecoratingFrameworkMethod method) {
        return new Scenario(method.getMethod(), asList(method.getRow().value()));
    }
    private final Method method;
    private final List<String> parameters;
    private GivensStore givens;
    private CommunicationStore communications;
    private long time = 0;
    private Throwable thrownException;
    private Status status = Status.Ignored;

    private Scenario(Method method, List<String> parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    public Scenario fail(Throwable exception) {
        this.thrownException = exception;
        return markAs(Status.Failed);
    }

    public Scenario markAs (Status status) {
        this.status = status;
        return this;
    }

    public Scenario with(GivensStore givens) {
        this.givens = new GivensStore();
        this.givens.putAll(givens);
        return this;
    }

    public Scenario with(CommunicationStore communications) {
        this.communications = new CommunicationStore();
        this.communications.addAll(communications);
        return this;
    }

    @Override
    public String toString() {
        String result = "Test: " + this.method.getName();
        if (!parameters.isEmpty())
            result += "(" + StringUtils.join(parameters, ",") + ")";

        result += " - " + status.name();

        return result;
    }

    public String getName() {
        String sentencify = sentencify(method.getName(), true);
        if (!parameters.isEmpty()) {
            try {
                TestSpecification specification = codeExtractor.extract(getMethod());
                List<Java8Parser.FormalParameterContext> list = specification.parameters();
                List<String> textParts = new ArrayList<String>();
                for (int i=0;i<list.size();i++)
                    textParts.add("with "+ sentencify(list.get(i).variableDeclaratorId().Identifier().getText(), false) + " '"+ parameters.get(i) +"'");

                return sentencify + " " + StringUtils.join(textParts, " and ");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sentencify;
    }

    public String getUrl () {
        return method.getDeclaringClass().getName() + "-" + getName().replaceAll("[ \\/\\']", "_") + ".html";
    }

    public String getId () {
        return method.getDeclaringClass().getName() + "_" + getName().replaceAll("[ /'\"]", "_");
    }

    public String getSpecification () {
        try {
            return doReplacements(codeExtractor.extract(method).text().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String doReplacements(String string) {
        return String.format(string, parameters.toArray());
    }

    public Status status() {
        return status;
    }

    public Method getMethod() {
        return method;
    }

    public boolean noParameters() {
        return parameters.isEmpty();
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Scenario timeElapsed(long time) {
        this.time = time;
        return this;
    }

    public List<Communication> getCommunications () {
        return communications;
    }

    public HashMap<String, Object> getGivens () {
        return givens;
    }

    public long getTime() {
        return time;
    }

    @Override
    public int compareTo(Scenario o) {
        return (o.status().getViewPriority() - this.status().getViewPriority());
    }

    public File toFile(File baseDirectory) {
        return new File(baseDirectory, getUrl());
    }

    public Scenario communications(CommunicationStore communications) {
        this.communications = communications;
        return this;
    }

}
