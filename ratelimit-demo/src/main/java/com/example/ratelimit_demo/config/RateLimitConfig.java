package com.example.ratelimit_demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.FixedWindowAlgorithm;
import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.RateLimitAlgorithm;

/**
 * Wires ONE algorithm into the interceptor.
 *
 * THIS IS THE ONLY PLACE YOU NEED TO CHANGE to swap algorithms.
 * The interceptor, annotation, and controllers are all untouched.
 *
 * Try each one and observe the difference in behavior:
 *
 *   FixedWindowAlgorithm       → fire 10 req at t=59s and 10 at t=61s → 20 go through
 *   SlidingWindowLogAlgorithm  → no boundary burst, exact accounting, more memory
 *   SlidingWindowCounterAlgorithm → Cloudflare-style, very accurate, low memory
 *   TokenBucketAlgorithm       → 10 requests can fire instantly (burst), then throttled
 */
@Configuration
@ConditionalOnProperty(name = "rate.limit.strategy", havingValue = "INTERCEPTOR")
public class RateLimitConfig {

    @Bean
    public RateLimitAlgorithm rateLimitAlgorithm() {
        return new FixedWindowAlgorithm();        // <-- change this line to swap
        // return new SlidingWindowLogAlgorithm();
        // return new SlidingWindowCounterAlgorithm();
        // return new TokenBucketAlgorithm();
    }
}
