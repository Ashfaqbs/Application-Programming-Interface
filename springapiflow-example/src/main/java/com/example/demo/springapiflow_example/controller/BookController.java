package com.example.demo.springapiflow_example.controller;

import com.example.demo.springapiflow_example.dto.BookCreateRequest;
import com.example.demo.springapiflow_example.dto.BookResponse;
import com.example.demo.springapiflow_example.dto.BookUpdateRequest;
import com.example.demo.springapiflow_example.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookCreateRequest req, HttpServletRequest request) {
        BookResponse res = service.create(req);
        String tenantId = (String) request.getAttribute("tenantId"); //pulls the validated attribute your Interceptor stored.
        String tenantIdPostman = (String)   request.getHeader("X-Tenant-Id"); //pulls the raw header value that Postman sent.
        System.out.println("Created book for tenant: " + tenantId);
        System.out.println("Created book for tenant: " + tenantIdPostman);

//
//        Created book for tenant: demo-key-123
//        Created book for tenant: demo-key-123
//        Created book for tenant: demo-key-123 // ⚠️ Always read the tenant from the request attribute ("tenantId") we set in the Interceptor,
// not directly from the Postman header, to ensure all downstream layers use the validated value.

        return ResponseEntity.created(URI.create("/api/books/" + res.id())).body(res);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

