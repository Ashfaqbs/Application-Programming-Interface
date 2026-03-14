package com.example.ratelimit_demo.exception;

/**
 * Thrown when a client exceeds the configured request rate.
 * Maps to HTTP 429 Too Many Requests via GlobalExceptionHandler.
 */
public class RateLimitExceededException extends RuntimeException {

    private final long retryAfterSeconds;

    public RateLimitExceededException(long retryAfterSeconds) {
        super("Rate limit exceeded. Retry after " + retryAfterSeconds + " seconds.");
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
