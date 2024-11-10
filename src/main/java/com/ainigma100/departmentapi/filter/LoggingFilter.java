package com.ainigma100.departmentapi.filter;

import com.ainigma100.departmentapi.utils.Utils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * LoggingFilter is a servlet filter that logs HTTP requests and responses.
 *
 * <p>This filter logs the URL and method of incoming requests and the status
 * of outgoing responses. It excludes certain paths from logging, such as those
 * related to Actuator, Swagger, API documentation, favicon, and UI resources.</p>
 *
 */
@Component
@Slf4j
@Order(2)
public class LoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String clientIP = Utils.getClientIP(httpServletRequest);

        if ( this.shouldLogRequest(httpServletRequest) ) {
            log.info("Client IP: {}, Request URL: {}, Method: {}", clientIP, httpServletRequest.getRequestURL(), httpServletRequest.getMethod());
        }

        // pre methods call stamps
        chain.doFilter(request, response);

        // post method calls stamps
        if ( this.shouldLogRequest(httpServletRequest) ) {
            log.info("Response status: {}", httpServletResponse.getStatus());
        }

    }

    private boolean shouldLogRequest(HttpServletRequest request) {

        // (?i) enables case-insensitive matching, \b matched as whole words
        // reference: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Regular_expressions
        return !request.getServletPath().matches("(?i).*\\b(actuator|swagger|api-docs|favicon|ui|h2-console)\\b.*");
    }

}
