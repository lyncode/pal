package org.paltest.http.matchers;

import org.apache.http.Header;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.paltest.http.client.FluentHttpResponse;
import org.paltest.matchers.AbstractMatcherBuilder;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

public class HttpResponseMatcherBuilder extends AbstractMatcherBuilder<HttpResponseMatcherBuilder, FluentHttpResponse> {
    public HttpResponseMatcherBuilder withContent(Matcher<String> matcher) {
        return with(new FeatureMatcher<FluentHttpResponse, String>(matcher, "body", "HTTP Body") {
            @Override
            protected String featureValueOf(FluentHttpResponse fluentHttpResponse) {
                return fluentHttpResponse.contentAsString();
            }
        });
    }

    public HttpResponseMatcherBuilder withStatusCode(Matcher<Integer> statusCodeMatcher) {
        return with(new FeatureMatcher<FluentHttpResponse, Integer>(statusCodeMatcher, "status code", "HTTP status") {
            @Override
            protected Integer featureValueOf(FluentHttpResponse fluentHttpResponse) {
                return fluentHttpResponse.statusCode();
            }
        });
    }

    public HttpResponseMatcherBuilder whereHeaders(Matcher<Iterable<? super Header>> matcher) {
        return with(new FeatureMatcher<FluentHttpResponse, Iterable<Header>>(matcher, "headers", "HTTP Headers") {
            @Override
            protected Iterable<Header> featureValueOf(FluentHttpResponse fluentHttpResponse) {
                return fluentHttpResponse.headers();
            }
        });
    }

    public HttpResponseMatcherBuilder withHeader(String name, Matcher<String> matcher) {
        return whereHeaders(hasItem(new HttpHeaderMatcherBuilder().withName(equalTo(name)).withValue(matcher)));
    }
}
