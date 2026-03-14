package com.example.ratelimit_demo.config;

import com.example.ratelimit_demo.ratelimit.interceptor.RateLimitInterceptor;
import com.example.ratelimit_demo.ratelimit.interceptor.algorithm.RateLimitAlgorithm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Active only when rate.limit.strategy=INTERCEPTOR.
 *
 * Instantiates RateLimitInterceptor here (not a @Component) so Spring
 * never tries to create it when a different strategy is active.
 */
@Configuration
@ConditionalOnProperty(name = "rate.limit.strategy", havingValue = "INTERCEPTOR")
public class WebMvcConfig implements WebMvcConfigurer {

    private final RateLimitAlgorithm algorithm;

    public WebMvcConfig(RateLimitAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(algorithm))
                .addPathPatterns("/api/v1/**");
    }
}
