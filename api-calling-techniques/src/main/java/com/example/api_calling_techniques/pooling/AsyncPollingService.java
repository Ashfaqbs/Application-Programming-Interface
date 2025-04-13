package com.example.api_calling_techniques.pooling;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AsyncPollingService {

    private final WebClient webClient = WebClient.create("http://localhost:5000");

//    @Scheduled(fixedRate = 5000) // Poll every 5 seconds
//    @Async
    public void pollAsync() {
        System.out.println("Running on thread: " + Thread.currentThread().getName());

        webClient.get()
                .uri("/data")
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> System.out.println("Error: " + err.getMessage()))
                .subscribe(body -> System.out.println("ASYNC POLL RESPONSE: " + body));
    }
}
