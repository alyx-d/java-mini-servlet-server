package com.qt.server;

import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpExchangeResponse {
    Headers getResponseHeaders();
    void sendResponseHeaders(int code, long responseLength) throws IOException;
    OutputStream getResponseBody();
}
