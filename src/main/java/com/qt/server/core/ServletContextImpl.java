package com.qt.server.core;

import com.qt.server.mapping.ServletMapping;
import com.qt.server.utils.AnnotationUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.descriptor.JspConfigDescriptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ServletContextImpl implements ServletContext {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<ServletMapping> servletMappings = new ArrayList<>();
    private final Map<String, ServletRegistrationImpl> servletRegistrations = new HashMap<>();
    private final Map<String, Servlet> servlets = new HashMap<>();

    private boolean initialized = false;

    public ServletContextImpl() {}

    public boolean isInitialized() {
        return initialized;
    }

    public void initialize(List<Class<? extends Servlet>> servletClasses) {
        for (var servletClass : servletClasses) {
            WebServlet ws = servletClass.getAnnotation(WebServlet.class);
            if (ws != null) {
                var registration = addServlet(AnnotationUtil.getServletName(servletClass), servletClass);
                registration.addMapping(AnnotationUtil.getServletUrlPatterns(servletClass));
                registration.setInitParameters(AnnotationUtil.getServletInitParams(servletClass));
            }
        }

        for (var name : servletRegistrations.keySet()) {
            var registration = servletRegistrations.get(name);
            try {
                registration.getServlet().init(registration.getServletConfig());
                servlets.put(name, registration.getServlet());
                for(String urlPattern : registration.getMappings()) {
                    servletMappings.add(new ServletMapping(urlPattern, registration.getServlet()));
                }
            } catch (Exception e) {
                logger.error("init servlet failed name={}, message={}", name, e.getMessage());
            }
        }
        initialized = true;
    }


    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        Servlet servlet = null;
        for (ServletMapping mapping : servletMappings) {
            if (mapping.matches(path)) {
                servlet = mapping.getServlet();
                break;
            }
        }
        if (servlet == null) {
            response.setStatus(404);
            try(var writer = response.getWriter()) {
                writer.write("path = " + path + " not found");
            }
        }else {
            servlet.service(request, response);
        }
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public ServletContext getContext(String s) {
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public String getMimeType(String s) {
        return "";
    }

    @Override
    public Set<String> getResourcePaths(String s) {
        return Set.of();
    }

    @Override
    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }

    @Override
    public void log(String s) {

    }

    @Override
    public void log(String s, Throwable throwable) {

    }

    @Override
    public String getRealPath(String s) {
        return "";
    }

    @Override
    public String getServerInfo() {
        return "";
    }

    @Override
    public String getInitParameter(String s) {
        return "";
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }

    @Override
    public boolean setInitParameter(String s, String s1) {
        return false;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public String getServletContextName() {
        return "";
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String name, String className) {
        Servlet servlet = null;
        try {
            var clazz = Class.forName(className).asSubclass(Servlet.class);
            servlet = createServlet(clazz);
        } catch (ClassNotFoundException | ServletException e) {
            throw new RuntimeException(e);
        }
        return addServlet(name, servlet);
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        var registration = new ServletRegistrationImpl(this, s, servlet);
        servletRegistrations.put(s, registration);
        return registration;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String name, Class<? extends Servlet> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("servletClass must not be null");
        }
        Servlet servlet = null;
        try {
            servlet = createServlet(clazz);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return addServlet(name, servlet);
    }

    private Servlet createInstance(Class<? extends Servlet> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getConstructor().newInstance();
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String s, String s1) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Servlet> T createServlet(Class<T> aClass) throws ServletException {
        try {
            return (T) createInstance(aClass);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletRegistration getServletRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return Map.of();
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, String s1) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return Map.of();
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> set) {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return Set.of();
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return Set.of();
    }

    @Override
    public void addListener(String s) {

    }

    @Override
    public <T extends EventListener> void addListener(T t) {

    }

    @Override
    public void addListener(Class<? extends EventListener> aClass) {

    }

    @Override
    public <T extends EventListener> T createListener(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void declareRoles(String... strings) {

    }

    @Override
    public String getVirtualServerName() {
        return "";
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int i) {

    }

    @Override
    public String getRequestCharacterEncoding() {
        return "";
    }

    @Override
    public void setRequestCharacterEncoding(String s) {

    }

    @Override
    public String getResponseCharacterEncoding() {
        return "";
    }

    @Override
    public void setResponseCharacterEncoding(String s) {

    }
}
