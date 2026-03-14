package com.example.ratelimit_demo.ratelimit.filter;

import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.RateLimitAlgorithm;

/**
 * Per-route rate limit config.
 *
 * Since a Filter has no access to @RateLimit annotations (it runs before
 * Spring MVC resolves the handler), limits are declared here per URL pattern.
 *
 * Each route gets its own algorithm instance — so /search can use
 * SlidingWindowLog while /products uses FixedWindow, all in one filter.
 *
 * pattern      : Ant-style URL pattern  e.g. "/api/v1/search/**"
 * maxRequests  : max requests allowed in the window
 * windowSeconds: window duration
 * algorithm    : which algorithm enforces this route's limit
 */
public record RouteRateLimit(
        String pattern,
        int maxRequests,
        int windowSeconds,
        RateLimitAlgorithm algorithm
) {}
