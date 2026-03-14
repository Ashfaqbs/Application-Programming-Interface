package com.example.ratelimit_demo.ratelimit.filter;

import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.RateLimitResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * No-library rate limiting via Servlet Filter.
 *
 * Runs BEFORE DispatcherServlet — even before Spring Security and interceptors.
 *
 * Flow:
 *   Request → [this filter] → DispatcherServlet → Interceptor → Controller
 *
 * Steps per request:
 *   1. Extract request path.
 *   2. Find the first RouteRateLimit whose pattern matches the path.
 *   3. If no match → pass through (not all routes need rate limiting).
 *   4. If match → call that route's algorithm.tryAcquire().
 *   5. Allowed  → set X-RateLimit-* headers, continue chain.
 *   6. Denied   → write HTTP 429 response directly, stop chain.
 *
 * Why OncePerRequestFilter?
 *   Servlet filters can be called multiple times for the same logical request
 *   (e.g., on forward/include dispatches). OncePerRequestFilter guarantees
 *   doFilterInternal() runs exactly once per incoming HTTP request.
 *
 * Not a @Component — registered explicitly via FilterRegistrationBean in
 * FilterRateLimitConfig so we control URL patterns and order from one place.
 */
public class RateLimitFilter extends OncePerRequestFilter {

    private final List<RouteRateLimit> routes;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public RateLimitFilter(List<RouteRateLimit> routes) {
        this.routes = routes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();

        Optional<RouteRateLimit> matched = routes.stream()
                .filter(route -> pathMatcher.match(route.pattern(), path))
                .findFirst();

        if (matched.isEmpty()) {
            chain.doFilter(request, response); // no rule for this path
            return;
        }

        RouteRateLimit route = matched.get();
        String clientKey = resolveClientIp(request) + ":" + path;

        RateLimitResult result = route.algorithm().tryAcquire(
                clientKey, route.maxRequests(), route.windowSeconds()
        );

        // Always expose quota headers — useful for clients to back off proactively
        response.setHeader("X-RateLimit-Limit",     String.valueOf(route.maxRequests()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(result.remainingRequests()));
        response.setHeader("X-RateLimit-Reset",     String.valueOf(result.resetAfterSeconds()));

        if (result.allowed()) {
            chain.doFilter(request, response);
            return;
        }

        response.setStatus(429);
        response.setContentType("application/json");
        response.setHeader("Retry-After", String.valueOf(result.resetAfterSeconds()));
        response.getWriter().write("""
                {
                  "success": false,
                  "message": "Rate limit exceeded. Retry after %d seconds.",
                  "data": null
                }
                """.formatted(result.resetAfterSeconds()));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
