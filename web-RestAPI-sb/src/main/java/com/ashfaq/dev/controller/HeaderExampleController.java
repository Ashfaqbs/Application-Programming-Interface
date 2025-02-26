package com.ashfaq.dev.controller;


import java.util.Collections;
import java.util.Map;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderExampleController {

    @GetMapping("/example")
    public ResponseEntity<String> exampleEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        
        // Adding custom headers
        headers.add("Custom-Header", "CustomValue");
        headers.add("Another-Header", "AnotherValue");
        
        // Adding standard headers
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setCacheControl(CacheControl.noCache());
        headers.set("User-Agent", "PostmanRuntime/7.28.4");
        headers.set("Authorization", "Bearer your_token_here");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        return new ResponseEntity<>("Headers added to response", headers, HttpStatus.OK);
    }
    
    @GetMapping("/specific-header")
    public ResponseEntity<String> greetUser(@RequestHeader("User-Agent") String userAgent) {
        String response = "Hello! Your User-Agent is: " + userAgent;
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/all-headers")
    public ResponseEntity<String> getAllHeaders(@RequestHeader Map<String, String> headers) {
        headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });

        return ResponseEntity.ok("Headers printed in console");
    }
   
    @GetMapping("/common-headers")
    public ResponseEntity<String> processRequest(
            @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestHeader(value = "X-Custom-Header", required = false) String customHeader,
            @RequestBody String body
            ) {

        // Process headers
        System.out.println("Content-Type: " + contentType);
        if (authorization != null) {
            System.out.println("Authorization: " + authorization);
        }
        if (customHeader != null) {
            System.out.println("X-Custom-Header: " + customHeader);
        }

        // Process body
        System.out.println("Request Body: " + body);

        return ResponseEntity.ok("Headers and body processed");
    }
    
}
