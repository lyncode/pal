package org.paltest.http.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.paltest.exceptions.PalTestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class FluentProgrammableHandler extends AbstractHandler {
    private final LinkedList<Rule> rules = new LinkedList<Rule>();
    private final RequestHandler defaultRequestHandler;

    public FluentProgrammableHandler(RequestHandler defaultRequestHandler) {
        this.defaultRequestHandler = defaultRequestHandler;
    }

    public FluentProgrammableHandler() {
        this(new RequestHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getOutputStream().write("Not found".getBytes());
                response.getOutputStream().close();
            }
        });
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        for (Rule rule : rules) {
            if (rule.matcher().matches(request)) {
                try {
                    rule.handler().handle(request, response);
                } catch (Exception e) {
                    throw new PalTestException(e);
                }
                return;
            }
        }

        try {
            defaultRequestHandler.handle(request, response);
        } catch (Exception e) {
            throw new PalTestException(e);
        }
    }

    public FluentProgrammableHandler defines (Rule rule) {
        this.rules.addFirst(rule); // So that the last one added is the first to be checked
        return this;
    }
}
