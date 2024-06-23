package com.ainigma100.departmentapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        if ( this.shouldLogRequest(httpServletRequest) ) {

            log.info("Request {}, method: {}", httpServletRequest.getRequestURL().toString(), httpServletRequest.getMethod());

        }

        // pre methods call stamps
        chain.doFilter(request, response);

        // post method calls stamps
        if ( this.shouldLogRequest(httpServletRequest) ) {
            log.info("Response status {} ", httpServletResponse.getStatus());
        }

    }

    private boolean shouldLogRequest(HttpServletRequest request) {

        // (?i) enables case-insensitive matching, \b matched as whole words
        // reference: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Regular_expressions
        return !request.getServletPath().matches("(?i).*\\b(actuator|swagger|api-docs|favicon|ui)\\b.*");
    }

}
