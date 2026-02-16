package com.tsm.ur.card.wiam.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class TracingConfig extends OncePerRequestFilter {

    private static final String TRACING_ID_KEY = "tracingId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // controllo se c'e tracing id negli header altrimenti lo genere
        String tracingId = (ObjectUtils.isEmpty(request.getHeader(TRACING_ID_KEY))) ? UUID.randomUUID().toString() : request.getHeader(TRACING_ID_KEY);
        // Put into MDC for logging
        MDC.put(TRACING_ID_KEY, tracingId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Cleanup MDC to avoid leaks
            MDC.remove(TRACING_ID_KEY);
        }
    }
}
