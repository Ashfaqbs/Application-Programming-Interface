package com.example.ratelimit_demo.dto;

public record LoginRequest(
        String username,
        String password
) {}
