package com.example.demo.springapiflow_example.interceptor;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantGuardInterceptor implements HandlerInterceptor {
    public static final String TENANT_HDR = "X-Tenant-Id";
    public static final String TENANT_ATTR = "tenantId";
    private static final String START_NS = "startNanos";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        // Enforce tenant on all /api/** routes (configured below)
        String tenantId = req.getHeader(TENANT_HDR);
        if (tenantId == null || tenantId.isBlank()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType("application/json");
            res.getWriter().write("""
        {"code":"MISSING_TENANT","message":"Provide X-Tenant-Id header"}
        """);
            return false; // Stop before controller
        }
        // Expose tenant to downstream (controller/service) via request attribute
        req.setAttribute(TENANT_ATTR, tenantId);
        // Simple timing
        req.setAttribute(START_NS, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        Long start = (Long) req.getAttribute(START_NS);
        if (start != null) {
            long ms = (System.nanoTime() - start) / 1_000_000;
            // Send timing + tenant back as response headers (handy in Postman)
            res.setHeader("X-Server-Timing", "handler;dur=" + ms);
            Object tenant = req.getAttribute(TENANT_ATTR);
            if (tenant != null) res.setHeader(TENANT_HDR, tenant.toString());
        }
    }
}


