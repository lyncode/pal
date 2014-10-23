package org.paltest.http.server;

import org.hamcrest.Matcher;

import javax.servlet.http.HttpServletRequest;

public interface Rule {
    String name ();
    Matcher<? super HttpServletRequest> matcher ();
    RequestHandler handler ();
}
