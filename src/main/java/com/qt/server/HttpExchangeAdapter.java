package com.qt.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;

public class HttpExchangeAdapter implements HttpExchangeRequest, HttpExchangeResponse{

    private final HttpExchange exchange;

    public HttpExchangeAdapter(HttpExchange exchange){
        this.exchange = exchange;
    }

    @Override
    public String getMethod() {
        return this.exchange.getRequestMethod();
    }

    @Override
    public URI getRequestURI() {
        return this.exchange.getRequestURI();
    }

    @Override
    public String getParameter(String name) {
        String query = this.exchange.getRequestURI().getQuery();
        var map = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            map.put(pair[0], pair[1]);
        }
        return map.get(name);
    }

    @Override
    public Headers getResponseHeaders() {
        return this.exchange.getResponseHeaders();
    }

    @Override
    public void sendResponseHeaders(int code, long responseLength) throws IOException {
        this.exchange.sendResponseHeaders(code, responseLength);
    }

    @Override
    public OutputStream getResponseBody() {
        return this.exchange.getResponseBody();
    }
}
