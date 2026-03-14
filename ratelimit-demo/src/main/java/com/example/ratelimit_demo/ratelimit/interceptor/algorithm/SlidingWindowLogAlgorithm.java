package com.example.ratelimit_demo.ratelimit.interceptor.algorithm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ALGORITHM 2: Sliding Window Log
 * -------------------------------------------------
 * Visual:
 *
 *   now = t=65s,  window = 60s  →  look back to t=5s
 *
 *   timestamps: [t=5, t=20, t=30, t=50, t=60, t=64, t=65]
 *                ↑ this is older than 5s — EVICT it
 *
 *   After eviction: [t=20, t=30, t=50, t=60, t=64, t=65]
 *   Count = 6.  If limit=10: ALLOW. Add t=65 to log.
 *
 * Solves the boundary burst problem completely.
 * The window SLIDES with every request — no hard reset boundaries.
 *
 * Cost:
 *   - Memory: stores every request timestamp (up to maxRequests per client).
 *   - If a client hits exactly the limit constantly, the deque always has
 *     `maxRequests` entries. Set limit = 10 → max 10 timestamps per client.
 *
 * Use when:
 *   - Accuracy matters (payment APIs, auth endpoints).
 *   - You can afford the memory (limit is not extremely high).
 *
 * Thread safety:
 *   - ConcurrentHashMap for the map; synchronized per-key deque access.
 */
public class SlidingWindowLogAlgorithm implements RateLimitAlgorithm {

    // clientKey → deque of request timestamps (oldest first)
    private final ConcurrentHashMap<String, Deque<Long>> logs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    @Override
    public RateLimitResult tryAcquire(String clientKey, int maxRequests, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMs = (long) windowSeconds * 1000;
        long cutoff = now - windowMs; // timestamps older than this are expired

        Object lock = locks.computeIfAbsent(clientKey, k -> new Object());

        synchronized (lock) {
            Deque<Long> log = logs.computeIfAbsent(clientKey, k -> new ArrayDeque<>());

            // Evict expired timestamps from the front of the deque
            while (!log.isEmpty() && log.peekFirst() <= cutoff) {
                log.pollFirst();
            }

            long count = log.size();

            if (count < maxRequests) {
                log.addLast(now); // record this request
                long remaining = maxRequests - count - 1;
                // reset = time until the oldest entry in the log expires
                long resetIn = log.isEmpty() ? windowMs : (log.peekFirst() + windowMs) - now;
                return new RateLimitResult(true, remaining, Math.max(0, resetIn));
            }

            // When will the oldest request expire? That's when one slot opens up.
            long oldestTimestamp = log.peekFirst() != null ? log.peekFirst() : now;
            long retryAfter = (oldestTimestamp + windowMs) - now;
            return new RateLimitResult(false, 0, Math.max(0, retryAfter));
        }
    }
}
