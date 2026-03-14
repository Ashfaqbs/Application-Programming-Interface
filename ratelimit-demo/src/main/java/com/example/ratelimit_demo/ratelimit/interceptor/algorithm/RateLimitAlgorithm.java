package com.example.ratelimit_demo.ratelimit.interceptor.algorithm;

/**
 * Strategy interface for rate limiting algorithms.
 *
 * The interceptor depends ONLY on this interface — it never knows which
 * algorithm is running. Swap the @Bean in RateLimitConfig to change behavior.
 *
 * clientKey: identifies WHO is making the request.
 *   Typically: "ip:endpoint"  →  each IP gets its own counter per endpoint.
 *   e.g.:  "192.168.1.1:/api/v1/search"
 */
public interface RateLimitAlgorithm {

    /**
     * Try to allow one request for this client.
     *
     * @param clientKey     unique identifier (IP + endpoint, or user ID + endpoint)
     * @param maxRequests   max allowed requests in the window
     * @param windowSeconds window duration in seconds
     * @return RateLimitResult with allowed/denied + remaining quota info
     */
    RateLimitResult tryAcquire(String clientKey, int maxRequests, int windowSeconds);
}
