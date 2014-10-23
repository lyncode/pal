package org.paltest.http.server;

import org.eclipse.jetty.server.Server;
import org.paltest.exceptions.PalTestException;

public class FluentProgrammableServer<T extends FluentProgrammableServer> {
    public static FluentProgrammableServer server () {
        return new FluentProgrammableServer();
    }
    public static FluentProgrammableServer server (int port) {
        return new FluentProgrammableServer(port);
    }

    public static FluentProgrammableServer server (int port, RequestHandler defaultRequestHandler) {
        return new FluentProgrammableServer(port, defaultRequestHandler);
    }

    private final int port;
    private final FluentProgrammableHandler fluentProgrammableHandler;
    private Server server;

    protected FluentProgrammableServer(int port, RequestHandler defaultRequestHandler) {
        this.port = port;
        this.fluentProgrammableHandler = new FluentProgrammableHandler(defaultRequestHandler);
    }
    protected FluentProgrammableServer(int port) {
        this.port = port;
        this.fluentProgrammableHandler = new FluentProgrammableHandler();
    }

    protected FluentProgrammableServer() {
        this(0);
    }

    public T start() {
        synchronized (server) {
            this.server = new Server(port);
        }

        try {
            this.server.setHandler(fluentProgrammableHandler);
            this.server.start();
        } catch (Exception e) {
            throw new PalTestException(e);
        }

        return self();
    }

    public T join() {
        synchronized (server) {
            if (server == null) return self();
        }
        try {
            server.join();
        } catch (InterruptedException e) {
            throw new PalTestException(e);
        }
        return self();
    }

    public T stop () {
        synchronized (server) {
            if (server == null) return self();
        }
        try {
            this.server.stop();
        } catch (Exception e) {
            throw new PalTestException(e);
        }
        return self();
    }

    public T defines (RuleBuilder rule) {
        this.fluentProgrammableHandler.defines(rule.build());
        return self();
    }

    public int port() {
        return port;
    }

    protected T self() {
        return (T) this;
    }
}
