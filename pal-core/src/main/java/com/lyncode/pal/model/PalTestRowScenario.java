package com.lyncode.pal.model;

import com.lyncode.pal.junit.annotations.Row;
import com.lyncode.pal.junit.runner.TableRunner;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

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
}
