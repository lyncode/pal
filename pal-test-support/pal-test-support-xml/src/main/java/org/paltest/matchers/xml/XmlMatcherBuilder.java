package org.paltest.matchers.xml;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.paltest.matchers.AbstractMatcherBuilder;
import org.xmlmatchers.XmlMatchers;
import org.xmlmatchers.namespace.SimpleNamespaceContext;
import org.xmlmatchers.transform.StringSource;

import static org.hamcrest.core.IsNot.not;

public class XmlMatcherBuilder<M extends XmlMatcherBuilder> extends AbstractMatcherBuilder<M, String> {
    private SimpleNamespaceContext namespaceContext = null;

    public M withXPath (final String xPath) {
        return with(new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String input) {
                return XmlMatchers.hasXPath(xPath, namespaceContext).matches(StringSource.toSource(input));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has xpath ").appendValue(xPath);
            }
        });
    }
    public M withoutXPath (final String xPath) {
        return with(new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String input) {
                return not(XmlMatchers.hasXPath(xPath, namespaceContext)).matches(StringSource.toSource(input));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has xpath ").appendValue(xPath);
            }
        });
    }

    public M withXPath (final String xPath, final Matcher<? super String> valueMatcher) {
        return with(new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String input) {
                return XmlMatchers.hasXPath(xPath, namespaceContext, valueMatcher).matches(StringSource.toSource(input));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("xpath ").appendValue(xPath).appendText(" ").appendDescriptionOf(valueMatcher);
            }
        });
    }

    public M withNamespace (String prefix, String uri) {
        if (namespaceContext == null)
            namespaceContext = new SimpleNamespaceContext();
        namespaceContext.withBinding(prefix, uri);
        return (M) this;
    }
}
