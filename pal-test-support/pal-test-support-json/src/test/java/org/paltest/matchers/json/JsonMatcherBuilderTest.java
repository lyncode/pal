package org.paltest.matchers.json;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JsonMatcherBuilderTest {

    @Test
    public void mustMatchWithJson() throws Exception {
        assertTrue(new JsonMatcherBuilder()
                .withJsonPath("$.hello")
                .matches("{ hello: 'melo' }"));
    }
    @Test
    public void mustMatchWithoutJson() throws Exception {
        assertTrue(new JsonMatcherBuilder()
                .withoutJsonPath("$.hello")
                .matches("{ hella: 'melo' }"));
    }

    @Test
    public void mustFailWithJson() throws Exception {
        assertFalse(new JsonMatcherBuilder()
                .withJsonPath("$.hello")
                .matches("{ hella: 'melo' }"));
    }

    @Test
    public void mustFailWithoutJson() throws Exception {
        assertFalse(new JsonMatcherBuilder()
                .withoutJsonPath("$.hello")
                .matches("{ hello: 'melo' }"));
    }

    @Test
    public void nestedPaths() throws Exception {
        assertTrue(new JsonMatcherBuilder()
                .withJsonPath("$.hello", new JsonMatcherBuilder().withJsonPath("$.another"))
                .matches("{ hello: { another: 'one' } }"));
    }

    @Test
    public void assertOnValue() throws Exception {
        assertTrue(new JsonMatcherBuilder()
                .withJsonPath("$.hello", equalTo("one"))
                .matches("{ hello: 'one' }"));
    }

    @Test
    public void assertOnIntValue() throws Exception {
        assertTrue(new JsonMatcherBuilder()
                .withJsonPath("$.hello", equalTo("1"))
                .matches("{ hello: 1 }"));
    }
}