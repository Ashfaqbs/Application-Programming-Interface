package com.example.ratelimit_demo.dto;

public record LoginResponse(
        String token,
        String username,
        long expiresInSeconds
) {}
