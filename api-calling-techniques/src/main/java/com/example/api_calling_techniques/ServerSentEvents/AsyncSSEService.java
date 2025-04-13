package com.example.api_calling_techniques.ServerSentEvents;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AsyncSSEService {

    private final WebClient webClient = WebClient.create("http://localhost:5000");

//    @Scheduled(fixedRate = 20000) // Poll every 20 seconds
    @Async
    public void callSSEAsync() {
        System.out.println("SSE Async - Thread: " + Thread.currentThread().getName());

        webClient.get()
                .uri("/sse")
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(err -> System.out.println("Error: " + err.getMessage()))
                .doOnTerminate(() -> System.out.println("SSE Async Stream Ended"))
                .subscribe(event -> System.out.println("SSE Async Event: " + event));
    }

}
