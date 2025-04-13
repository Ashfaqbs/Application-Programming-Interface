package com.example.api_calling_techniques.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WebhookReceiver {

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Webhook received on thread: " + Thread.currentThread().getName());
        System.out.println("Payload: " + payload);
        return ResponseEntity.ok("Received");
    }
}
