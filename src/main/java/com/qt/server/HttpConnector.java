package com.qt.server;

import com.qt.server.core.HttpServletRequestImpl;
import com.qt.server.core.HttpServletResponseImpl;
import com.qt.server.core.ServletContextImpl;
import com.qt.server.servlet.HelloServlet;
import com.qt.server.servlet.IndexServlet;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;

public class HttpConnector implements HttpHandler, AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String host;
    private final int port;
    private final HttpServer httpServer;
    private final ServletContextImpl servletContext;

    public HttpConnector(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0, "/", this);
        var servletContext = new ServletContextImpl();
        this.servletContext = servletContext;
        servletContext.initialize(List.of(IndexServlet.class, HelloServlet.class));
        httpServer.start();
        logger.info("HTTP Server started at {}:{}", host, port);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var adapter = new HttpExchangeAdapter(exchange);
        var request = new HttpServletRequestImpl(adapter);
        var response = new HttpServletResponseImpl(adapter);
        try {
            this.servletContext.process(request, response);
        } catch (ServletException e) {
            logger.error("servlet handle error {}", e.getMessage());
        }
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        var method = request.getMethod();
        var uri = request.getRequestURI();
        String path = request.getPathInfo();
        String query = request.getQueryString();
        logger.info("HTTP request: {} {} {}", method, path, query);
        response.setStatus(200);
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String content = "<h1>hello world</h1>";
        content += "<p>" +
                " path = " + path +
                " now = " + LocalDateTime.now() +
                "</p>";
        try(var writer = response.getWriter()) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.httpServer.stop(0);
    }
}
