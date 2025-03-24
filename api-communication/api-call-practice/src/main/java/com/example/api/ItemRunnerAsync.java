package com.example.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ItemRunnerAsync implements CommandLineRunner {

    private final WebClient webClient;

    public ItemRunnerAsync(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/items").build(); // or define a bean
    }

    @Override
    public void run(String... args) {
        try {
            // Get by ID
//            webClient.get()
//                    .uri("/1")
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .retrieve()
//                    .bodyToMono(Item.class)
//                    .doOnNext(item -> System.out.println("Item: " + item))
//                    .doOnError(error -> System.out.println("Error fetching item: " + error.getMessage()))
//                    .subscribe();

            // Get All
//            webClient.get()
//                    .uri("")
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .header("customHeader", "customValue")
//                    .retrieve()
//                    .bodyToFlux(Item.class)
//                    .collectList()
//                    .doOnNext(items -> {
//                        System.out.println("Size of the list: " + items.size());
//                        items.forEach(System.out::println);
//                    })
//                    .subscribe();
//
//            // Post Item
//            webClient.post()
//                    .uri("")
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .bodyValue(new Item("Fan", 1000.00, 1))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .doOnNext(response -> System.out.println("Post response: " + response))
//                    .subscribe();
//
//            // Update Item
//            webClient.put()
//                    .uri("/18")
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .bodyValue(new Item("Fan", 2000, 5))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .doOnNext(response -> System.out.println("Update response: " + response))
//                    .subscribe();
//
//            // Delete Item
//            webClient.delete()
//                    .uri("/17")
//                    .header("Authorization", APIUtils.auth("ashu", "ashu"))
//                    .retrieve()
//                    .bodyToMono(Void.class)
//                    .doOnSuccess(v -> System.out.println("Item deleted successfully"))
//                    .subscribe();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
