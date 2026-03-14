package com.example.ratelimit_demo.dto;

import java.util.List;

public record SearchResponse(
        String query,
        int totalResults,
        List<ProductResponse> results
) {}
