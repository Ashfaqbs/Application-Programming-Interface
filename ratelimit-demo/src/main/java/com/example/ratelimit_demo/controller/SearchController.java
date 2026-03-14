package com.example.ratelimit_demo.controller;

import com.example.ratelimit_demo.dto.ApiResponse;
import com.example.ratelimit_demo.dto.ProductResponse;
import com.example.ratelimit_demo.dto.SearchResponse;
import com.example.ratelimit_demo.ratelimit.interceptor.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Search endpoint — strictly rate limited via @RateLimit (NO-LIBRARY approach).
 *
 * Search is expensive (full-text scan, relevance ranking, etc.).
 * Lower limit: 5 requests per 60 seconds.
 *
 * This is a common real-world pattern: public search APIs have tight rate limits
 * to prevent abuse and protect backend resources.
 */
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private static final List<ProductResponse> ALL_PRODUCTS = List.of(
            new ProductResponse(1L, "Mechanical Keyboard", "Electronics", new BigDecimal("149.99"), 50),
            new ProductResponse(2L, "USB-C Hub",           "Electronics", new BigDecimal("39.99"),  120),
            new ProductResponse(3L, "Standing Desk",       "Furniture",   new BigDecimal("499.00"), 15),
            new ProductResponse(4L, "Monitor Arm",         "Furniture",   new BigDecimal("89.00"),  80),
            new ProductResponse(5L, "Webcam HD",           "Electronics", new BigDecimal("69.99"),  200)
    );

    /**
     * Full-text product search.
     * Limit: 5 requests per 60 seconds — search is resource-intensive.
     *
     * Try hitting this > 5 times quickly — on the 6th request you'll get HTTP 429
     * with Retry-After header and X-RateLimit-* headers.
     */
    @RateLimit(requests = 5, windowSeconds = 60)
    @GetMapping
    public ResponseEntity<ApiResponse<SearchResponse>> search(@RequestParam String q) {
        List<ProductResponse> results = ALL_PRODUCTS.stream()
                .filter(p -> p.name().toLowerCase().contains(q.toLowerCase())
                          || p.category().toLowerCase().contains(q.toLowerCase()))
                .toList();

        return ResponseEntity.ok(
                ApiResponse.ok(new SearchResponse(q, results.size(), results))
        );
    }
}
