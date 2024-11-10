package com.ainigma100.departmentapi.filter;

import com.ainigma100.departmentapi.utils.Utils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Order(1)
public class RateLimitingFilter implements Filter {

    @Value("${rate-limiting.max-requests}")
    private int maxNumberOfRequests;

    @Value("${rate-limiting.refill-duration}")
    private String refillDuration;


    private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIP = Utils.getClientIP(httpRequest);
        Bucket bucket = bucketMap.computeIfAbsent(clientIP, ip -> createNewBucket());


        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);

        } else {
            log.warn("Rate limit exceeded for IP: {}. Blocking request.", clientIP);
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Rate limit exceeded. Please try again later.");
        }

    }

    public void clearBuckets() {
        bucketMap.clear();
    }

    // reference here: https://bucket4j.com/
    private Bucket createNewBucket() {

        long duration = Long.parseLong(refillDuration);

        Bandwidth limit = Bandwidth.builder()
                .capacity(maxNumberOfRequests)
                .refillGreedy(maxNumberOfRequests, Duration.ofMinutes(duration))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

}
