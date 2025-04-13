package com.example.api_calling_techniques.longpooling;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SyncLongPollingService {

    private final RestTemplate restTemplate = new RestTemplate();

//    @Scheduled(fixedRate = 20000) // Poll every 20 seconds
    public void callLongPollSync() {
        String response = restTemplate.getForObject("http://localhost:5000/long-poll", String.class);
        System.out.println("LONG POLL SYNC RESPONSE: " + response);
    }
}
