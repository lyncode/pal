package org.paltest.http.matchers;

import org.apache.http.Header;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.paltest.matchers.AbstractMatcherBuilder;

public class HttpHeaderMatcherBuilder extends AbstractMatcherBuilder<HttpHeaderMatcherBuilder, Header> {

    public HttpHeaderMatcherBuilder withName(Matcher<String> matcher) {
        return with(new FeatureMatcher<Header, String>(matcher, "name", "Header name") {
            @Override
            protected String featureValueOf(Header header) {
                return header.getName();
            }
        });
    }


    public HttpHeaderMatcherBuilder withValue(Matcher<String> matcher) {
        return with(new FeatureMatcher<Header, String>(matcher, "value", "Header value") {
            @Override
            protected String featureValueOf(Header header) {
                return header.getValue();
            }
        });
    }
}
