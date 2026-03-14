package com.example.ratelimit_demo.ratelimit.withlibrary;

import io.github.bucket4j.Bandwidth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.time.Duration;
import java.util.List;

/**
 * Registers the Bucket4j filter with per-route bandwidth configs.
 *
 * Two refill strategies demonstrated across routes:
 *
 *   /api/v1/search/**  → refillGreedy
 *     Smooth recovery. 5 tokens total, 1 token refills every 12s.
 *     After hitting the limit, requests start being allowed again gradually.
 *     client fires 5 at t=0 → denied at t=1 → 1 token back at t=12 → allowed
 *
 *   /api/v1/auth/**    → refillIntervally
 *     Hard periodic limit. 5 tokens, ALL restored every 60s.
 *     client fires 5 at t=0 → denied until t=60 → full 5 restored at once
 *     No gradual recovery — mimics a strict per-minute quota.
 */
@Configuration
@ConditionalOnProperty(name = "rate.limit.strategy", havingValue = "BUCKET4J")
public class Bucket4jFilterConfig {

    @Bean
    public FilterRegistrationBean<Bucket4jRateLimitFilter> bucket4jRateLimitFilter() {
        List<BucketRouteLimit> routes = List.of(

                // GREEDY REFILL — tokens trickle back continuously
                // capacity=5, period=60s → 1 token refills every 12 seconds
                new BucketRouteLimit(
                        "/api/v1/search/**",
                        Bandwidth.builder()
                                .capacity(5)
                                .refillGreedy(5, Duration.ofSeconds(60))
                                .build()
                ),

                // INTERVAL REFILL — all tokens restored at once per period
                // capacity=5, period=60s → hard reset every 60 seconds
                new BucketRouteLimit(
                        "/api/v1/auth/**",
                        Bandwidth.builder()
                                .capacity(5)
                                .refillIntervally(5, Duration.ofSeconds(60))
                                .build()
                )
        );

        FilterRegistrationBean<Bucket4jRateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Bucket4jRateLimitFilter(routes));
        registration.addUrlPatterns("/api/v1/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1); // runs after no-library filter
        registration.setName("bucket4jRateLimitFilter");
        return registration;
    }
}
