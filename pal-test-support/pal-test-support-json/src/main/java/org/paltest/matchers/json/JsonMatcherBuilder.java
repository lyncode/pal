package org.paltest.matchers.json;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.paltest.matchers.AbstractMatcherBuilder;

import java.io.IOException;
import java.util.Map;

public class JsonMatcherBuilder<M extends JsonMatcherBuilder> extends AbstractMatcherBuilder<M, String> {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Check https://code.google.com/p/json-path/
     *
     * @param jsonPath
     * @return
     */
    public M withJsonPath (final String jsonPath) {
        return with(new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String input) {
                try {
                    return JsonPath.compile(jsonPath).read(input) != null;
                } catch (PathNotFoundException e) {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("jsonPath ").appendValue(jsonPath).appendText(" exists");
            }
        });
    }

    /**
     * Check https://code.google.com/p/json-path/
     *
     * @param jsonPath
     * @return
     */
    public M withoutJsonPath (final String jsonPath) {
        return with(new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String input) {
                try {
                    JsonPath.compile(jsonPath).read(input);
                    return false;
                } catch (PathNotFoundException e) {
                    return true;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("jsonPath ").appendValue(jsonPath).appendText(" doesn't exist");
            }
        });
    }

    public M withJsonPath (final String jsonPath, Matcher<? super String> value) {
        return with(new FeatureMatcher<String, String>(value, "jsonPath", "JSON Path") {
            @Override
            protected String featureValueOf(String input) {
                Object read = JsonPath.compile(jsonPath).read(input);
                return string(read);
            }
        });
    }

    private String string(Object read) {
        try {
            if (read instanceof Map || read instanceof Iterable || read.getClass().isArray())
                return MAPPER.writeValueAsString(read);
            else
                return read.toString();
        } catch (IOException e) {
            return null;
        }
    }


}
