package com.sitedia.common.rest.utils;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Security filter
 * @author cedric
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityFilter implements Filter {

    /**
     * Web browsers requires the exact host of the server
     */
    @Value("${application.allowOrigin}")
    private String allowOrigin;

    /**
     * Applies security filter
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // Set response headers
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-total-count, Set-Cookie");
        response.setHeader("Access-Control-Expose-Headers", "x-total-count, Set-Cookie");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }

        // Log access
        logAccess(response, request);
    }

    private void logAccess(HttpServletResponse response, HttpServletRequest request) {
        int responseStatus = response.getStatus();
        String incomingRequest = request.getMethod() + " " + request.getRequestURL() + " => " + responseStatus;
        Integer responseType = responseStatus / 100;

        switch (responseType) {
        case 2:
            Logger.getLogger("endpoint.success").info(incomingRequest);
            break;
        case 3:
            Logger.getLogger("endpoint.redirect").info(incomingRequest);
            break;
        case 4:
            Logger.getLogger("endpoint.warning").info(incomingRequest);
            break;
        default:
            Logger.getLogger("endpoint.error").info(incomingRequest);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

}
