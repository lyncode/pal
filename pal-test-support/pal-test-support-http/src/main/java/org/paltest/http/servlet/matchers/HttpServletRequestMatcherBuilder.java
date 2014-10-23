package org.paltest.http.servlet.matchers;

import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.paltest.matchers.AbstractMatcherBuilder;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestMatcherBuilder<T extends HttpServletRequestMatcherBuilder> extends AbstractMatcherBuilder<T, HttpServletRequest> {
    public static HttpServletRequestMatcherBuilder httpRequest () {
        return new HttpServletRequestMatcherBuilder();
    }

    public T withHeaderName (final Matcher<? super String> valueMatcher) {
        return with(new TypeSafeMatcher<HttpServletRequest>() {
            @Override
            protected boolean matchesSafely(HttpServletRequest request) {
                while (request.getHeaderNames().hasMoreElements()) {
                    String headerName = request.getHeaderNames().nextElement();
                    if (valueMatcher.matches(headerName))
                        return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Contains header name ").appendDescriptionOf(valueMatcher);
            }
        });
    }

    public T withHeader (final String name, Matcher<? super String> valueMatcher) {
        String description = String.format("Header '%s'", name);
        return with(new FeatureMatcher<HttpServletRequest, String>(valueMatcher, description, description) {
            @Override
            protected String featureValueOf(HttpServletRequest request) {
                return request.getHeader(name);
            }
        });
    }

    public T withUri (Matcher<? super String> valueMatcher) {
        String description = "Request URI";
        return with(new FeatureMatcher<HttpServletRequest, String>(valueMatcher, description, description) {
            @Override
            protected String featureValueOf(HttpServletRequest request) {
                return request.getRequestURI();
            }
        });
    }

    public T withMethod (Matcher<? super String> valueMatcher) {
        String description = "HTTP Method";
        return with(new FeatureMatcher<HttpServletRequest, String>(valueMatcher, description, description) {
            @Override
            protected String featureValueOf(HttpServletRequest request) {
                return request.getMethod();
            }
        });
    }

    public T withContentType (Matcher<? super String> valueMatcher) {
        String description = "HTTP Content-Type";
        return with(new FeatureMatcher<HttpServletRequest, String>(valueMatcher, description, description) {
            @Override
            protected String featureValueOf(HttpServletRequest request) {
                return request.getContentType();
            }
        });
    }

    public T withParameter (final String name, Matcher<? super String> matcher) {
        String description = String.format("Parameter '%s'", name);
        return with(new FeatureMatcher<HttpServletRequest, String>(matcher, description, description) {
            @Override
            protected String featureValueOf(HttpServletRequest request) {
                return request.getParameter(name);
            }
        });
    }

    public T withParameterName (final Matcher<? super String> valueMatcher) {
        return with(new TypeSafeMatcher<HttpServletRequest>() {
            @Override
            protected boolean matchesSafely(HttpServletRequest request) {
                while (request.getParameterNames().hasMoreElements()) {
                    String parameterName = request.getHeaderNames().nextElement();
                    if (valueMatcher.matches(parameterName))
                        return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Contains parameter name ").appendDescriptionOf(valueMatcher);
            }
        });
    }

}
