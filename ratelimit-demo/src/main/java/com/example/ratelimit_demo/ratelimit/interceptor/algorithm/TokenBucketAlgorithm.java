package com.example.ratelimit_demo.ratelimit.interceptor.algorithm;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ALGORITHM 4: Token Bucket
 * -------------------------------------------------
 * Think of it as a physical bucket that holds tokens.
 *
 * Visual:
 *
 *   Bucket capacity = 10 tokens
 *   Refill rate = 10 tokens per 60 seconds  →  1 token every 6 seconds
 *
 *   t=0:    bucket = [●●●●●●●●●●]  (full, 10 tokens)
 *   request → consume 1 token
 *   t=1:    bucket = [●●●●●●●●● ]  (9 tokens)
 *   ...
 *   t=10:   bucket = [          ]  (0 tokens → DENIED)
 *   t=16:   bucket = [●         ]  (1 token refilled after 6s → ALLOWED again)
 *
 * Key difference from window algorithms:
 *   - Token Bucket allows BURST up to bucket capacity.
 *     A fresh client can fire 10 requests instantly (burns all tokens),
 *     then is throttled until tokens refill.
 *   - Window algorithms: 10 req in 60s means spread-ish; Token Bucket means
 *     10 right now is fine, then slow down.
 *
 * Implementation (no external library):
 *   We don't run a background thread to refill tokens.
 *   Instead: on each request, calculate how many tokens should have
 *   accumulated since the last check — "lazy refill".
 *
 *   tokensToAdd = elapsed_seconds * (capacity / windowSeconds)
 *   currentTokens = min(capacity, storedTokens + tokensToAdd)
 *
 * Use when:
 *   - You WANT to allow short bursts (e.g., mobile apps syncing on wake).
 *   - Smooth average rate matters but occasional spikes are acceptable.
 *   - This is what Bucket4j implements (hence the name).
 */
public class TokenBucketAlgorithm implements RateLimitAlgorithm {

    // clientKey → [lastRefillTimeMillis, currentTokens * 1000 (stored as fixed-point)]
    // We store tokens * 1000 as a long to avoid floating point in synchronized blocks
    private final ConcurrentHashMap<String, long[]> store = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    @Override
    public RateLimitResult tryAcquire(String clientKey, int maxRequests, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMs = (long) windowSeconds * 1000;

        // refillRate: tokens added per millisecond (scaled by 1000 for integer math)
        // e.g. 10 tokens / 60000ms = 0.0001666 tokens/ms → stored as 166 (per 1_000_000)
        long capacityScaled = (long) maxRequests * 1_000_000;
        long refillPerMs = capacityScaled / windowMs; // tokens added per ms (scaled)

        Object lock = locks.computeIfAbsent(clientKey, k -> new Object());

        synchronized (lock) {
            long[] data = store.computeIfAbsent(clientKey, k ->
                    new long[]{now, capacityScaled} // start full
            );

            long lastRefill = data[0];
            long storedTokensScaled = data[1];

            // Lazy refill: add tokens proportional to elapsed time
            long elapsedMs = now - lastRefill;
            long newTokensScaled = elapsedMs * refillPerMs;
            long currentTokensScaled = Math.min(capacityScaled, storedTokensScaled + newTokensScaled);

            long oneTokenScaled = 1_000_000L;

            if (currentTokensScaled >= oneTokenScaled) {
                // Consume one token
                data[0] = now;
                data[1] = currentTokensScaled - oneTokenScaled;

                long remainingTokens = currentTokensScaled / oneTokenScaled;
                // Time until next token: how long until 1_000_000 more units accumulate
                long msUntilNextToken = refillPerMs > 0 ? (oneTokenScaled / refillPerMs) : windowMs;
                return new RateLimitResult(true, remainingTokens - 1, msUntilNextToken);
            }

            // Not enough tokens — tell client when the next token arrives
            long deficit = oneTokenScaled - currentTokensScaled;
            long waitMs = refillPerMs > 0 ? (deficit / refillPerMs) : windowMs;
            data[0] = now; // update last refill time even on deny
            data[1] = currentTokensScaled;
            return new RateLimitResult(false, 0, waitMs);
        }
    }
}
