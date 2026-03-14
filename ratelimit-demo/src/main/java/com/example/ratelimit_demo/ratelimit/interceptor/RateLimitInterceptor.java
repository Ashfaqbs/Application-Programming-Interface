package com.example.ratelimit_demo.ratelimit.interceptor;

import com.example.ratelimit_demo.exception.RateLimitExceededException;
import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.RateLimitAlgorithm;
import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.RateLimitResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * The interceptor — sits between DispatcherServlet and your controller.
 *
 * Not a @Component — instantiated by WebMvcConfig only when
 * rate.limit.strategy=INTERCEPTOR. This prevents Spring from trying
 * to create it (and failing to find a RateLimitAlgorithm bean)
 * when a different strategy is active.
 */
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitAlgorithm algorithm;

    // Constructor injection — Spring injects whichever @Bean implements RateLimitAlgorithm
    public RateLimitInterceptor(RateLimitAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // Skip static resources — only intercept controller methods
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
        if (rateLimit == null) {
            return true; // no annotation on this method → no rate limiting
        }

        String clientIp = resolveClientIp(request);
        String clientKey = clientIp + ":" + request.getRequestURI();

        RateLimitResult result = algorithm.tryAcquire(clientKey, rateLimit.requests(), rateLimit.windowSeconds());

        // Always set headers so clients can track their quota
        response.setHeader("X-RateLimit-Limit",     String.valueOf(rateLimit.requests()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(result.remainingRequests()));
        response.setHeader("X-RateLimit-Reset",     String.valueOf(result.resetAfterSeconds()));

        if (result.allowed()) {
            return true;
        }

        throw new RateLimitExceededException(result.resetAfterSeconds());
    }

    /**
     * Prefer X-Forwarded-For (set by nginx/load balancer) over remoteAddr.
     * In production behind a proxy, remoteAddr is always the proxy IP.
     */
    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
