package com.example.api_calling_techniques.longpooling;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AsyncLongPollingService {

    private final WebClient webClient = WebClient.create("http://localhost:5000");


//    @Scheduled(fixedRate = 20000) // Poll every 20 seconds
//    @Async
    public void callLongPollAsync() {
        System.out.println("LONG POLL ASYNC - Thread: " + Thread.currentThread().getName());

        webClient.get()
                .uri("/long-poll")
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> System.out.println("Error: " + err.getMessage()))
                .subscribe(body -> System.out.println("LONG POLL ASYNC RESPONSE: " + body));
    }

}
