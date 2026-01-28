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

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String serverPort = System.getenv("SERVER_PORT");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String method = httpRequest.getMethod();
        String serverName = httpRequest.getServerName();
        String protocol = httpRequest.getScheme();
        if ((httpRequest.getRequestURI().equals("/") || httpRequest.getRequestURI().equals("/index.html")) && !method.equals("GET")) {
            httpResponse.setStatus(400);
            httpResponse.getWriter().write("Invalid HTTP method: " + method);
        } else {
            if (!this.isValidHttpMethod(method)) {
                httpResponse.setStatus(400);
                httpResponse.getWriter().write("Invalid HTTP method: " + method);
            }
            httpResponse.setHeader(
                    "Referrer-Policy",
                    "strict-origin-when-cross-origin"
            );

            httpResponse.setHeader(
                    "Permissions-Policy",
                    "geolocation=(), microphone=(), camera=()"
            );
            httpResponse.setHeader("Content-Security-Policy", "default-src 'self' data: blob: 'unsafe-inline' 'unsafe-eval'; frame-ancestors 'self';" + protocol + "://" + serverName + " " + protocol + "://" + serverName + ":*" + " ws://" + serverName + " ws://" + serverName + ":"+serverPort + " wss://" + serverName + ":"+serverPort+";" + "frame-ancestors 'self';");
//            httpResponse.setHeader("Content-Security-Policy", " font-src 'self' ; img-src 'self'; object-src 'self';" + protocol + "://" + serverName + " " + protocol + "://" + serverName + ":*" + " ws://" + serverName + " ws://" + serverName + ":"+serverPort + " wss://" + serverName + ":"+serverPort+";" + "frame-ancestors 'self';");
            chain.doFilter(request, response);
        }
    }

    private boolean isValidHttpMethod(String method) {
        return "GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method) || "OPTIONS".equalsIgnoreCase(method) || "TRACE".equalsIgnoreCase(method);
    }

    public void destroy() {
    }
}
