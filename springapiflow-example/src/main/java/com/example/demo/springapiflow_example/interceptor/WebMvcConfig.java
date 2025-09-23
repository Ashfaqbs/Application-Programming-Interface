package com.example.demo.springapiflow_example.interceptor;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final TenantGuardInterceptor tenantGuardInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantGuardInterceptor)
                .addPathPatterns("/api/**")     // protect our REST APIs
                .excludePathPatterns("/health"); // allow health checks
    }
}
