package com.qt.server;

import java.net.URI;

public interface HttpExchangeRequest {
    String getMethod();
    URI getRequestURI();
    String getParameter(String name);
}
