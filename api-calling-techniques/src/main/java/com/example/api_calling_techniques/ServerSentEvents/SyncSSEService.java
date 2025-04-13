package com.example.api_calling_techniques.ServerSentEvents;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SyncSSEService {

    private final WebClient webClient = WebClient.create("http://localhost:5000");

//    @Scheduled(fixedRate = 20000) // Poll every 20 seconds
    public void callSSESync() {
        System.out.println("SSE Sync - Thread: " + Thread.currentThread().getName());

        webClient.get()
                .uri("/sse")
                .retrieve()
                .bodyToFlux(String.class)  // This returns a Flux, which is a stream of data
                .doOnError(err -> System.out.println("Error: " + err.getMessage()))
                .doOnTerminate(() -> System.out.println("SSE Sync Stream Ended"))
                .subscribe(event -> System.out.println("SSE Sync Event: " + event));
    }

}
