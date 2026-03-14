package com.example.ratelimit_demo.controller;

import com.example.ratelimit_demo.dto.ApiResponse;
import com.example.ratelimit_demo.dto.ProductResponse;
import com.example.ratelimit_demo.ratelimit.interceptor.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product endpoints — rate limited via @RateLimit (NO-LIBRARY approach).
 *
 * The RateLimitInterceptor picks up @RateLimit before the method runs.
 * Each method can have its own limit — GET /products is cheaper, so higher limit.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    // Simulated in-memory data — no real DB needed for this demo
    private static final List<ProductResponse> PRODUCTS = List.of(
            new ProductResponse(1L, "Mechanical Keyboard", "Electronics", new BigDecimal("149.99"), 50),
            new ProductResponse(2L, "USB-C Hub",           "Electronics", new BigDecimal("39.99"),  120),
            new ProductResponse(3L, "Standing Desk",       "Furniture",   new BigDecimal("499.00"), 15),
            new ProductResponse(4L, "Monitor Arm",         "Furniture",   new BigDecimal("89.00"),  80),
            new ProductResponse(5L, "Webcam HD",           "Electronics", new BigDecimal("69.99"),  200)
    );

    /**
     * List all products.
     * Limit: 20 requests per 60 seconds — generous, listing is cheap.
     */
    @RateLimit(requests = 20, windowSeconds = 60)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> listProducts() {
        return ResponseEntity.ok(ApiResponse.ok(PRODUCTS));
    }

    /**
     * Get product by ID.
     * Limit: 30 requests per 60 seconds — single lookup, very cheap.
     */
    @RateLimit(requests = 30, windowSeconds = 60)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return PRODUCTS.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .map(p -> ResponseEntity.ok(ApiResponse.ok(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get products by category.
     * Limit: 10 requests per 60 seconds — involves filtering, slightly heavier.
     */
    @RateLimit(requests = 10, windowSeconds = 60)
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByCategory(@PathVariable String category) {
        List<ProductResponse> filtered = PRODUCTS.stream()
                .filter(p -> p.category().equalsIgnoreCase(category))
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(filtered));
    }
}
