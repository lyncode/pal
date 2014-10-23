package org.paltest.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;

import java.util.ArrayList;
import java.util.List;

public class AbstractMatcherBuilder<M extends AbstractMatcherBuilder, T> extends BaseMatcher<T> {
    private List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();

    @Override
    public boolean matches(Object input) {
        return matcher().matches(input);
    }

    private Matcher<T> matcher() {
        return AllOf.allOf(matchers);
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(matcher());
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        matcher().describeMismatch(item, description);
    }

    protected M with (Matcher<? super T> matcher) {
        this.matchers.add(matcher);
        return (M) this;
    }

    public M and () {
        return (M) this;
    }
}
