package com.example.ratelimit_demo.ratelimit.filter;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.FixedWindowAlgorithm;
import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.SlidingWindowCounterAlgorithm;
import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.TokenBucketAlgorithm;

/**
 * Registers the no-library RateLimitFilter with explicit per-route algorithm config.
 *
 * Unlike the interceptor (where each controller method carries its own @RateLimit),
 * here ALL config lives in one place. You see every route's limit and algorithm
 * at a glance — easier to audit, easier to change without touching controllers.
 *
 * Route matching uses Ant-style patterns (first match wins):
 *   /api/v1/search/**   matches /api/v1/search, /api/v1/search?q=foo
 *   /api/v1/auth/**     matches /api/v1/auth/login, /api/v1/auth/refresh
 *   /api/v1/products/** matches /api/v1/products, /api/v1/products/1
 *
 * Three different algorithms for three different traffic patterns:
 *
 *   SEARCH   → SlidingWindowCounter : accurate sliding window, no burst problem,
 *                                      low memory — good for expensive endpoints
 *   AUTH     → TokenBucket          : allows a tiny burst (client retries on flaky net),
 *                                      then throttles hard — ideal for login brute-force protection
 *   PRODUCTS → FixedWindow          : simplest, low overhead — fine for cheap read endpoints
 *                                      where the boundary burst is an acceptable trade-off
 */
@Configuration
@ConditionalOnProperty(name = "rate.limit.strategy", havingValue = "FILTER")
public class FilterRateLimitConfig {

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        List<RouteRateLimit> routes = List.of(

                // Expensive search — accurate limit, no boundary burst
                new RouteRateLimit(
                        "/api/v1/search/**",
                        5, 60,
                        new SlidingWindowCounterAlgorithm()
                ),

                // Auth — token bucket: allows 1-2 rapid retries, then hard-stop
                new RouteRateLimit(
                        "/api/v1/auth/**",
                        5, 60,
                        new TokenBucketAlgorithm()
                ),

                // Product browsing — fixed window is fine, bursts are low-risk
                new RouteRateLimit(
                        "/api/v1/products/**",
                        20, 60,
                        new FixedWindowAlgorithm()
                )
        );

        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitFilter(routes));
        registration.addUrlPatterns("/api/v1/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("noLibraryRateLimitFilter");
        return registration;
    }
}
