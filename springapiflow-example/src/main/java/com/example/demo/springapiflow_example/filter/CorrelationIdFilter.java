package com.example.demo.springapiflow_example.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    /** The HTTP header used to carry the correlation/request id. */
    public static final String HDR = "X-Request-Id";

    /** HTTP methods considered “write” operations (must provide X-Request-Id). */
    private static final Set<String> WRITE_METHODS = Set.of("POST", "PUT", "PATCH", "DELETE");

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // 1) Read the incoming X-Request-Id header if present (client-supplied correlation id).
        //    If absent or blank, treat as null.
        String cid = Optional.ofNullable(req.getHeader(HDR))
                .filter(s -> !s.isBlank())
                .orElse(null);

        // 2) Enforce policy:
        //    - For WRITE operations (POST/PUT/PATCH/DELETE):
        //        If the header is missing → reject with 400 (SC_BAD_REQUEST).
        //        This ensures writes are always traceable/idempotent-friendly how to trace it is from controller take is as
        //        @GetMapping("/api/books/{id}")
        //public BookResponse get(@PathVariable Long id, HttpServletRequest req) {
        //  String requestId = req.getHeader("X-Request-Id");
        //  return service.get(id);
        //}
        // OR
        // // Explicit parameter
        //@GetMapping("/api/books")
        //public List<BookResponse> list(@RequestHeader("X-Request-Id") String requestId) {
        //  // pass to service if you want
        //  return service.list();
        //}
        //
        // .
        //    - For READ operations (GET/HEAD/etc.):
        //        If the header is missing → we auto-generate a UUID so reads still get traced.
        if (cid == null && WRITE_METHODS.contains(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType("application/json");
            res.getWriter().write("""
        {"code":"MISSING_REQUEST_ID","message":"Provide X-Request-Id header for write operations"}
        """);
            return; // Short-circuit: DispatcherServlet/Controller are not invoked.
        }

        // 3) If it’s a READ and the header wasn’t provided, generate a UUID.
        //    This gives every request (including reads) a correlation id.
        if (cid == null) {
            cid = UUID.randomUUID().toString();
        }

        // 4) Echo the final correlation id back to the client in the response header.
        //    Downstream (controller/service) can also read it from the *request header* if needed.
        //    (We’re not modifying the request object here; the header passes through as-is.)
        res.setHeader(HDR, cid);

        // 5) Continue the chain → DispatcherServlet → (Interceptors) → Controller → Service → Repo.
        chain.doFilter(req, res);
    }
}
