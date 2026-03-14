package com.example.ratelimit_demo.ratelimit.withlibrary;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WITH-LIBRARY rate limiting via Bucket4j + Servlet Filter.
 *
 * Structurally identical to RateLimitFilter — same flow:
 *   1. Match request path against configured routes.
 *   2. Get or create a Bucket for this client+route.
 *   3. tryConsumeAndReturnRemaining(1) — atomic check-and-decrement.
 *   4. Allowed → set headers, continue chain.
 *   5. Denied  → HTTP 429.
 *
 * What Bucket4j adds over our manual implementation:
 *   - Thread-safe by design — no synchronized blocks needed.
 *   - ConsumptionProbe gives remaining tokens + nanoseconds to next refill atomically.
 *   - Supports distributed buckets (Redis, Hazelcast) with a one-line swap.
 *
 * Bucket key: clientIp + ":" + route.pattern()
 *   Uses the PATTERN not the full path so that /products/1 and /products/2
 *   share the same bucket per client (same route, same limit).
 *
 * Not a @Component — registered explicitly via Bucket4jFilterConfig.
 */
public class Bucket4jRateLimitFilter extends OncePerRequestFilter {

    private final List<BucketRouteLimit> routes;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // key: "clientIp:routePattern" → one bucket per client per route
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket4jRateLimitFilter(List<BucketRouteLimit> routes) {
        this.routes = routes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();

        Optional<BucketRouteLimit> matched = routes.stream()
                .filter(route -> pathMatcher.match(route.pattern(), path))
                .findFirst();

        if (matched.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        BucketRouteLimit route = matched.get();
        String clientIp = resolveClientIp(request);
        String bucketKey = clientIp + ":" + route.pattern();

        Bucket bucket = buckets.computeIfAbsent(
                bucketKey,
                k -> Bucket.builder().addLimit(route.limit()).build()
        );

        // Atomic: checks token availability and consumes 1 in one operation
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        long resetAfterSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;

        response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
        response.setHeader("X-RateLimit-Reset",     String.valueOf(resetAfterSeconds));

        if (probe.isConsumed()) {
            chain.doFilter(request, response);
            return;
        }

        response.setStatus(429);
        response.setContentType("application/json");
        response.setHeader("Retry-After", String.valueOf(resetAfterSeconds));
        response.getWriter().write("""
                {
                  "success": false,
                  "message": "Rate limit exceeded. Retry after %d seconds.",
                  "data": null
                }
                """.formatted(resetAfterSeconds));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
