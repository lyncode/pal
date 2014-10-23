package org.paltest.http.server;

import org.hamcrest.Matcher;

import javax.servlet.http.HttpServletRequest;

public class RuleBuilder {
    public static RuleBuilder rule (String name) {
        return new RuleBuilder(name);
    }

    private final String name;
    private Matcher<? super HttpServletRequest> matcher;
    private RequestHandler handler;

    protected RuleBuilder(String name) {
        assert name != null;
        this.name = name;
    }


    public RuleBuilder when(Matcher<? super HttpServletRequest> request) {
        this.matcher = request;
        return this;
    }

    public RuleBuilder respond(RequestHandler handler) {
        this.handler = handler;
        return this;
    }

    Rule build () {
        return new Rule() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public Matcher<? super HttpServletRequest> matcher() {
                return matcher;
            }

            @Override
            public RequestHandler handler() {
                return handler;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Rule)
                    return ((Rule) obj).name().equals(name);
                return super.equals(obj);
            }
        };
    }
}
