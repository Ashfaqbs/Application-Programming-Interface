package com.example.ratelimit_demo.ratelimit.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a controller method for rate limiting.
 * The interceptor reads this to know the limit and window for that method.
 *
 * Usage:
 *   @RateLimit(requests = 5, windowSeconds = 60)
 *   @GetMapping("/search")
 *   public ResponseEntity<?> search(...) { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int requests() default 10;
    int windowSeconds() default 60;
}
