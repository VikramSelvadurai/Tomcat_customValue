//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TestMethodFilters implements Filter {
    public TestMethodFilters() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String serverPort = System.getenv("SERVER_PORT");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String serverName = httpRequest.getServerName();
        String protocol = httpRequest.getScheme();

        if ((httpRequest.getRequestURI().equals("/") ||
                httpRequest.getRequestURI().equals("/index.html"))
                && !method.equals("GET")) {

            httpResponse.setStatus(400);
            httpResponse.getWriter().write("Invalid HTTP method: " + method);
            return;
        }

        if (!this.isValidHttpMethod(method)) {
            httpResponse.setStatus(400);
            httpResponse.getWriter().write("Invalid HTTP method: " + method);
            return;
        }

        // ---- Security Headers ----

        httpResponse.setHeader(
                "Referrer-Policy",
                "strict-origin-when-cross-origin"
        );

        httpResponse.setHeader(
                "Permissions-Policy",
                "geolocation=(), microphone=(), camera=()"
        );

        httpResponse.setHeader(
                "X-XSS-Protection",
                "1; mode=block"
        );

        // ---- CSP (null-safe SERVER_PORT) ----

        StringBuilder csp = new StringBuilder();
        csp.append("default-src 'self' data: blob: 'unsafe-inline' 'unsafe-eval'; ");
        csp.append("frame-ancestors 'self'; ");
        csp.append(protocol).append("://").append(serverName).append(" ");
        csp.append(protocol).append("://").append(serverName).append(":* ");

        if (serverPort != null && !serverPort.isEmpty()) {
            csp.append("ws://").append(serverName).append(" ");
            csp.append("ws://").append(serverName).append(":").append(serverPort).append(" ");
            csp.append("wss://").append(serverName).append(":").append(serverPort).append(" ");
        }

        csp.append(";");

        httpResponse.setHeader("Content-Security-Policy", csp.toString());

        chain.doFilter(request, response);
    }


    private boolean isValidHttpMethod(String method) {
        return "GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method) || "OPTIONS".equalsIgnoreCase(method) || "TRACE".equalsIgnoreCase(method);
    }

    public void destroy() {
    }
}
