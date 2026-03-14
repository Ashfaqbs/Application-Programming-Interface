package com.example.ratelimit_demo.controller;

import com.example.ratelimit_demo.dto.ApiResponse;
import com.example.ratelimit_demo.dto.LoginRequest;
import com.example.ratelimit_demo.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth endpoint — rate limited via Bucket4j Token Bucket (WITH-LIBRARY approach).
 *
 * Notice: NO @RateLimit annotation here.
 * The Bucket4jRateLimitFilter intercepts /api/v1/auth/** BEFORE this controller runs.
 * Limit: 5 requests per 60 seconds (configured in the filter).
 *
 * Why auth endpoints need stricter limits:
 *   - Brute-force password attacks need rapid retries.
 *   - 5 req/min makes brute-force impractical.
 *   - Credential stuffing attacks fire thousands of requests — rate limiting stops them.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    /**
     * Simulated login. In production: validate credentials, issue JWT.
     *
     * The Bucket4j filter already rejected the request if rate limit exceeded —
     * if this method runs, the request was allowed through.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        // Demo: accept any username/password
        LoginResponse token = new LoginResponse(
                "demo-token-" + request.username(),
                request.username(),
                3600L
        );
        return ResponseEntity.ok(ApiResponse.ok("Login successful", token));
    }

    /**
     * Token refresh — also rate limited by the Bucket4j filter (same /api/v1/auth/** rule).
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh() {
        LoginResponse refreshed = new LoginResponse("refreshed-token", "demo-user", 3600L);
        return ResponseEntity.ok(ApiResponse.ok("Token refreshed", refreshed));
    }
}
