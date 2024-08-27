package com.qt.server.utils;

import jakarta.servlet.Servlet;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;

import java.util.*;

public class AnnotationUtil {

    public static String getServletName(Class<? extends Servlet> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("servlet class must not be null");
        }
        var ws = clazz.getAnnotation(WebServlet.class);
        if (ws == null || ws.name().isBlank()) {
            var simpleName = clazz.getSimpleName();
            return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
        }
        return ws.name();
    }

    public static Map<String, String> getServletInitParams(Class<? extends Servlet> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("servlet class must not be null");
        }
        var ws = clazz.getAnnotation(WebServlet.class);
        if (ws == null || ws.initParams().length == 0) {
            return Collections.emptyMap();
        }
        return initParamsToMap(ws.initParams());
    }

    private static Map<String, String> initParamsToMap(WebInitParam[] webInitParams) {
        var map = new HashMap<String, String>();
        for (var webInitParam : webInitParams) {
            map.put(webInitParam.name(), webInitParam.value());
        }
        return map;
    }


    public static String[] getServletUrlPatterns(Class<? extends Servlet> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("servlet class must not be null");
        }
        var ws = clazz.getAnnotation(WebServlet.class);
        if (ws == null || (ws.urlPatterns().length == 0 && ws.value().length == 0)) {
            return new String[0];
        }
        return arrayToSet(ws.value(), ws.urlPatterns()).toArray(new String[0]);
    }

    public static Set<String> arrayToSet(String[] args) {
        var set = new HashSet<String>();
        Collections.addAll(set, args);
        return set;
    }

    public static Set<String> arrayToSet(String[] args1, String[] args2) {
        var set = arrayToSet(args1);
        set.addAll(arrayToSet(args2));
        return set;
    }
}
