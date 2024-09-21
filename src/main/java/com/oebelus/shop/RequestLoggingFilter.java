package com.oebelus.shop;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String headers = Collections.list(req.getHeaderNames())
                .stream()
                .map(headerName -> headerName + ": " + req.getHeader(headerName))
                .collect(Collectors.joining(", "));

        log.info("Incoming request - Method: {}, URL: {}, Headers: {}",
                req.getMethod(), req.getRequestURL(), headers);

        chain.doFilter(request, response);
    }
}