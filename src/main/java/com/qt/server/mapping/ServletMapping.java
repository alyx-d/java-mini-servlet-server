package com.qt.server.mapping;

import jakarta.servlet.Servlet;

import java.util.regex.Pattern;

public class ServletMapping extends AbstractMapping {
    private final Servlet servlet;

    public ServletMapping(String url, Servlet servlet) {
        super(url);
        this.servlet = servlet;
    }

    public boolean matches(String url) {
        return pattern.matcher(url).matches();
    }

    public Servlet getServlet() {
        return servlet;
    }
}
