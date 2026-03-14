package com.example.ratelimit_demo.ratelimit.interceptor.algorithm;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ALGORITHM 1: Fixed Window Counter
 * -------------------------------------------------
 * Visual:
 *
 *   |--- window 1 (60s) ---|--- window 2 (60s) ---|
 *   t=0s                  t=60s                  t=120s
 *
 *   Each client gets a counter that RESETS at every window boundary.
 *   Allow if count < limit. Deny if count >= limit.
 *
 * Problem — the boundary burst:
 *
 *   limit = 10 requests/60s
 *
 *   t=59s: client sends 10 requests → allowed (last second of window 1)
 *   t=61s: client sends 10 requests → allowed (first second of window 2)
 *   Result: 20 requests in just 2 seconds. NOT what you intended.
 *
 * Use when:
 *   - You want dead-simple implementation and boundary burst is acceptable.
 *   - Internal admin APIs, dashboards, low-risk endpoints.
 *
 * Thread safety:
 *   - One lock object per clientKey (fine-grained).
 *   - synchronized block protects the [windowStart, count] pair atomically.
 */
public class FixedWindowAlgorithm implements RateLimitAlgorithm {

    // clientKey → [windowStartMillis, requestCount]
    private final ConcurrentHashMap<String, long[]> store = new ConcurrentHashMap<>();

    // one lock per key — avoids blocking all clients on a single global lock
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    @Override
    public RateLimitResult tryAcquire(String clientKey, int maxRequests, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMs = (long) windowSeconds * 1000;

        Object lock = locks.computeIfAbsent(clientKey, k -> new Object());

        synchronized (lock) {
            long[] data = store.computeIfAbsent(clientKey, k -> new long[]{now, 0L});
            //                                                               ^ window start  ^ count

            boolean windowExpired = (now - data[0]) >= windowMs;
            if (windowExpired) {
                data[0] = now; // reset window start
                data[1] = 1;   // this request is the first in new window
                return new RateLimitResult(true, maxRequests - 1, windowMs);
            }

            if (data[1] < maxRequests) {
                data[1]++;
                long remaining = maxRequests - data[1];
                long resetIn = windowMs - (now - data[0]);
                return new RateLimitResult(true, remaining, resetIn);
            }

            // window not expired + count exhausted → deny
            long resetIn = windowMs - (now - data[0]);
            return new RateLimitResult(false, 0, resetIn);
        }
    }
}
