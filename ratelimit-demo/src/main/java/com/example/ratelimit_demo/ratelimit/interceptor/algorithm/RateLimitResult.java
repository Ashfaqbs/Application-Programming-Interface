package com.example.ratelimit_demo.ratelimit.interceptor.algorithm;

/**
 * What an algorithm returns after evaluating a request.
 *
 * The interceptor uses this to:
 *   - decide allowed/denied (HTTP 429 or pass-through)
 *   - set X-RateLimit-* response headers so callers know their status
 */
public record RateLimitResult(
        boolean allowed,
        long remainingRequests,   // tokens/slots left in current window
        long resetAfterMillis     // ms until window resets or tokens refill
) {

    public long resetAfterSeconds() {
        return Math.max(0, resetAfterMillis / 1000);
    }
}
