package com.example.ratelimit_demo.ratelimit.interceptor.algorithm;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ALGORITHM 3: Sliding Window Counter
 * -------------------------------------------------
 * The smart middle ground — O(1) memory like Fixed Window,
 * accuracy close to Sliding Window Log. Used by Cloudflare & Redis.
 *
 * Idea:
 *   Track TWO fixed windows: the previous one and the current one.
 *   Estimate the request count for the "real" sliding window by
 *   weighing the previous window based on how far into the current window we are.
 *
 * Visual (window = 60s, now = t=70s):
 *
 *   prev window: [t=0 → t=60]   prevCount = 8
 *   curr window: [t=60 → t=120]  currCount = 3
 *   elapsed in curr window = 10s out of 60s  →  overlap ratio = 10/60 ≈ 0.17
 *
 *   weight of prev window = 1 - 0.17 = 0.83  (we're only 17% into current window,
 *                                               so prev window still contributes 83%)
 *
 *   estimated count = prevCount * 0.83 + currCount
 *                   = 8 * 0.83 + 3
 *                   = 6.64 + 3 = 9.64
 *
 *   If limit = 10 and estimated < 10: ALLOW
 *
 * Why this works:
 *   At t=0 of a new window, previous window fully counts (weight=1.0).
 *   At t=60 (end), previous window doesn't count (weight=0.0).
 *   Linear interpolation gives a smooth, approximate sliding window.
 *
 * Accuracy:
 *   Assumes requests were evenly distributed in the previous window.
 *   In practice: ~0.003% error rate — acceptable for almost all use cases.
 *
 * Use when:
 *   - High traffic, memory is a concern (only stores 2 longs per client).
 *   - You want better accuracy than Fixed Window without Sliding Log's memory cost.
 */
public class SlidingWindowCounterAlgorithm implements RateLimitAlgorithm {

    // clientKey → [prevWindowStart, prevCount, currWindowStart, currCount]
    private final ConcurrentHashMap<String, long[]> store = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    @Override
    public RateLimitResult tryAcquire(String clientKey, int maxRequests, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMs = (long) windowSeconds * 1000;

        Object lock = locks.computeIfAbsent(clientKey, k -> new Object());

        synchronized (lock) {
            // data[0]=prevWindowStart, data[1]=prevCount, data[2]=currWindowStart, data[3]=currCount
            long[] data = store.computeIfAbsent(clientKey, k -> new long[]{0L, 0L, now, 0L});

            long currWindowStart = data[2];

            // Check if we've moved into a new window
            boolean newWindow = (now - currWindowStart) >= windowMs;
            if (newWindow) {
                // Current becomes previous; start fresh current window
                data[0] = currWindowStart; // prev window start
                data[1] = data[3];         // prev count = old curr count
                data[2] = now;             // new current window starts now
                data[3] = 0;              // reset current count
                currWindowStart = now;
            }

            // How far are we into the current window? (0.0 = just started, 1.0 = almost done)
            double elapsedRatio = (double)(now - currWindowStart) / windowMs;

            // Weight the previous window: if we're 20% into current, prev counts at 80%
            double weightedPrevCount = data[1] * (1.0 - elapsedRatio);
            double estimatedCount = weightedPrevCount + data[3];

            if (estimatedCount < maxRequests) {
                data[3]++; // increment current window count
                long remaining = (long)(maxRequests - estimatedCount - 1);
                long resetIn = windowMs - (now - currWindowStart);
                return new RateLimitResult(true, Math.max(0, remaining), resetIn);
            }

            long resetIn = windowMs - (now - currWindowStart);
            return new RateLimitResult(false, 0, Math.max(0, resetIn));
        }
    }
}
