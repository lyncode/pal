package org.paltest.http.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHandler {
    void handle (HttpServletRequest request, HttpServletResponse response) throws Exception;
}
