package com.example.ratelimit_demo.ratelimit.withlibrary;

import io.github.bucket4j.Bandwidth;

/**
 * Per-route Bucket4j config — mirrors RouteRateLimit from the filter package
 * but uses Bucket4j's Bandwidth instead of our custom RateLimitAlgorithm.
 *
 * Bandwidth IS the algorithm config in Bucket4j.
 * You build it with one of two refill strategies:
 *
 *   refillGreedy(tokens, period)
 *     Tokens trickle back in continuously throughout the period.
 *     e.g. capacity=10, period=60s → 1 new token every 6 seconds.
 *     Smooth recovery. Good for: general APIs, browsing endpoints.
 *
 *   refillIntervally(tokens, period)
 *     All tokens restored at once when the period resets.
 *     e.g. capacity=5, period=60s → 0 tokens at t=59s, full 5 at t=60s.
 *     Hard periodic limit. Good for: auth, billing, expensive operations.
 *
 * pattern: Ant-style URL pattern matched against request URI.
 */
public record BucketRouteLimit(
        String pattern,
        Bandwidth limit
) {}
