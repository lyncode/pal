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

package com.lyncode.pal.parser.impl;

import com.lyncode.pal.model.TestSpecification;
import com.lyncode.pal.parser.api.ClassFileLocator;
import com.lyncode.pal.parser.api.MethodCodeExtractor;
import com.lyncode.pal.parser.impl.java8.Java8Lexer;
import com.lyncode.pal.parser.impl.java8.Java8MethodSpecificationListener;
import com.lyncode.pal.parser.impl.java8.Java8Parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodCodeExtractorImpl implements MethodCodeExtractor {
    private final ClassFileLocator locator;
    private static final Map<String, String> testContent = new HashMap<String, String>();

    public MethodCodeExtractorImpl(ClassFileLocator locator) {
        this.locator = locator;
    }

    @Override
    public TestSpecification extract(Method method) throws IOException, NoSuchMethodException {
        Java8Lexer lexer = new Java8Lexer(new ANTLRInputStream(new ByteArrayInputStream(getContent(method).getBytes())));
        ParseTree tree = new Java8Parser(new CommonTokenStream(lexer)).compilationUnit();

        ParseTreeWalker walker = new ParseTreeWalker();
        Java8MethodSpecificationListener listener = new Java8MethodSpecificationListener(method);
        walker.walk(listener, tree);

        TestSpecification result = listener.result();
        return result;
    }

    private synchronized String getContent(Method method) throws IOException {
        if (!testContent.containsKey(method.getDeclaringClass().getName())) {
            FileInputStream stream = new FileInputStream(locator.locationOf(method.getDeclaringClass()));
            testContent.put(method.getDeclaringClass().getName(), IOUtils.toString(stream));
            stream.close();
        }
        return testContent.get(method.getDeclaringClass().getName());
    }
}
