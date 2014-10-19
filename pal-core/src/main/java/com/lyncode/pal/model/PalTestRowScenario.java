package com.lyncode.pal.model;

import com.lyncode.pal.junit.annotations.Row;
import com.lyncode.pal.junit.runner.TableRunner;
import com.lyncode.pal.parser.impl.java8.Java8Parser;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PalTestRowScenario extends PalTestScenario {
    private final Row row;

    public PalTestRowScenario(Method method, Row row) {
        super(method);
        this.row = row;
    }

    @Override
    public boolean generatedBy(TableRunner.DecoratingFrameworkMethod method) {
        return equalRow(method.getRow(), this.row) && super.generatedBy(method);
    }

    private boolean equalRow(Row first, Row second) {
        if (first.value().length != second.value().length) return false;
        for (int i = 0; i < first.value().length; i++) {
            if (!StringUtils.equals(first.value()[i], second.value()[i]))
                return false;
        }
        return true;
    }

    @Override
    public String name() {
        List<String> parameters = new ArrayList<String>();
        for (int i = 0; i < row.value().length; i++) {
            String value = row.value()[i];
            Java8Parser.FormalParameterContext formalParameterContext = extractSpecification().parameters().get(i);
            parameters.add(String.format("with %s %s", formalParameterContext.variableDeclaratorId().Identifier().toString(), value));
        }
        return String.format("%s %s", super.name(), StringUtils.join(parameters, " and "));
    }
}
