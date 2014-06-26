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

package com.lyncode.pal.parser.impl.java8;

import com.lyncode.pal.model.TestSpecification;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Method;

import static com.lyncode.pal.utils.SubtleWordUtils.sentencify;

public class Java8MethodSpecificationListener extends Java8BaseListener {
    private boolean record = false;
    private boolean recordLine = false;
    private boolean firstInTheLine = true;
    private final Method method;
    private TestSpecification specification = new TestSpecification();


    public Java8MethodSpecificationListener(Method method) {
        this.method = method;
    }

    public TestSpecification result() {
        return specification;
    }

    @Override
    public void enterMethodDeclaration(@NotNull Java8Parser.MethodDeclarationContext ctx) {
        if (ctx.Identifier().getText().equals(method.getName())) {
            if (ctx.formalParameters().formalParameterList() == null) {
                if (method.getParameterTypes().length == 0)
                    record = true;
            } else {
                if (method.getParameterTypes().length == ctx.formalParameters().formalParameterList().formalParameter().size()) {
                    record = true;
                    specification.parameters(ctx.formalParameters().formalParameterList().formalParameter());
                }
            }
        }
    }

    @Override
    public void exitMethodDeclaration(@NotNull Java8Parser.MethodDeclarationContext ctx) {
        if (record) record = false;
    }

    @Override
    public void enterStatementExpression(@NotNull Java8Parser.StatementExpressionContext ctx) {
        if (record) {
            specification.text().append("<p class=\"line\">");
            firstInTheLine = true;
            recordLine = true;
        }
    }

    @Override
    public void exitStatementExpression(@NotNull Java8Parser.StatementExpressionContext ctx) {
        if (record) {
            specification.text().append(".</p>\n");
            recordLine = false;
        }
    }

    @Override
    public void enterPrimary(@NotNull Java8Parser.PrimaryContext ctx) {
        if (record && ctx.getChild(0) instanceof TerminalNode) {
            if (firstInTheLine) {
                specification.text().append("<span class=\"first\">")
                        .append(sentencify(ctx.getText(), true))
                        .append("</span>");
                firstInTheLine = false;
            } else {
                String text = ctx.getText();
                if (text.matches("[_A-Z][_A-Z1-9]*"))
                    specification.text().append(" <span class=\"constant\">")
                            .append(text)
                            .append("</span>");
                else {
                    int parameter = parameterPosition(ctx.Identifier().getText());
                    if (parameter != -1) {
                        specification.text().append(" ")
                                .append("<span class=\"parameter\">%"+parameter+"s</span>");
                    } else
                    specification.text().append(" ")
                            .append(sentencify(text, false));
                }
            }
        }
    }

    @Override
    public void enterLiteral(@NotNull Java8Parser.LiteralContext ctx) {
        if (record) {
            specification.text().append(" <span class=\"literal\">")
                    .append(ctx.getText())
                    .append("</span>");
        }
    }

    @Override
    public void visitTerminal(@NotNull TerminalNode node) {
        if (recordLine && !isInstanceOf(node.getParent(),
                Java8Parser.LiteralContext.class,
                Java8Parser.PrimaryContext.class) && !isSymbol(node.getText())) {
            specification.text().append(" ")
                    .append(sentencify(node.getText(), false));
        }
    }

    private int parameterPosition(String text) {
        int position = 1;
        for (Java8Parser.FormalParameterContext formalParameterContext : specification.parameters()) {
            if (formalParameterContext.variableDeclaratorId().getText().equals(text))
                return position;
            else
                position++;
        }
        return -1;
    }

    private boolean isSymbol(String text) {
        return text.matches("[,\\(\\)\\.\\[\\]]");
    }

    private boolean isInstanceOf(Object o, Class<?>... types) {
        for (Class<?> type : types) {
            if (type.isInstance(o))
                return true;
        }
        return false;
    }
}
