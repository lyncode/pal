package com.lyncode.pal.model;

import com.lyncode.pal.PalTest;
import com.lyncode.pal.junit.annotations.Row;
import com.lyncode.pal.junit.annotations.Table;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class PalTestRowScenarioTest extends PalTest {
    private final Row row = mock(Row.class);
    private PalTestRowScenario underTest = new PalTestRowScenario(getMethod(), row);

    @Test
    @Table({
            @Row({ "joao" })
    })
    public void checkName(String name) throws Exception {
        Mockito.when(row.value()).thenReturn(new String[]{ name });

        assertThat(underTest.name(), equalTo("Check name with name joao"));
    }

    public Method getMethod() {
        try {
            return getClass().getMethod("checkName", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}