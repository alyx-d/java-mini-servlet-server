package com.qt;

import com.qt.server.HttpConnector;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var host = "0.0.0.0";
        var port = 8080;
        try(var server = new HttpConnector(host, port)) {
//        try(var server = new SimpleHttpServer(host, port)) {
            while (true) {
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SimpleHttpServer implements HttpHandler, AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HttpServer server;
    private String host;
    private int port;

    public SimpleHttpServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(host, port), 0, "/", this);
        this.server.start();
        logger.info("HTTP server started at {}:{}", host, port);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String query = uri.getQuery();
        logger.info("HTTP request: {} {} {}", method, path, query);
        exchange.sendResponseHeaders(200, 0);
        var headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=utf-8");
        headers.set("Cache-Control", "no-cache");
        String content = "<h1>hello world</h1>";
        content += "<p>" +
                " path = " + path +
                " now = " + LocalDateTime.now() +
                "</p>";
        try(var writer = exchange.getResponseBody()) {
            writer.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void close() throws Exception {
        this.server.stop(3);
    }
}