package com.lyncode.pal.parser.impl;

import com.lyncode.pal.parser.api.ClassFileLocator;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class ClassFileLocatorImplTest {
    private ClassFileLocator locator = new ClassFileLocatorImpl();

    @Test
    public void shouldFindFileInCurrentProject() throws Exception {
        File file = locator.locationOf(ClassFileLocatorImplTest.class);
        assertThat(file, notNullValue());
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldNotFindFileNotInCurrentProject() throws Exception {
        locator.locationOf(Test.class);
    }
}