package com.example.api_calling_techniques.pooling;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SyncPollingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "http://localhost:5000/data";

//    @Scheduled(fixedRate = 5000) // Poll every 5 seconds
    public void pollSync() {
        // Synchronous API call
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
        System.out.println("SYNC: " + response.getBody());
    }
}
