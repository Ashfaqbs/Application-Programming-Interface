## Overview of Synchronous and Asynchronous APIs

Synchronous APIs process requests in a linear, blocking fashion: each call waits until the server responds before proceeding. This model simplifies sequencing and error handling but can lead to thread exhaustion if calls involve significant I/O or computation.
Asynchronous APIs return control immediately, often providing a handle (promise, future, callback) for later retrieval of results. This non-blocking approach allows other work to proceed while awaiting completion and is particularly beneficial for high-latency operations or high-concurrency scenarios.

## Interaction Patterns Between Client and Source APIs

### 1. Both Synchronous

* **Behavior**: Client calls source via a blocking HTTP client (e.g., RestTemplate), waits for response, processes result, and returns to its caller.
* **Implications**: Simple and easy to debug; end-to-end latency is sum of client processing + network + source processing. Under heavy load, threads pile up waiting for responses.

### 2. Asynchronous Client, Synchronous Source

* **Behavior**: Client issues calls in parallel using threads or CompletableFutures, but each call to source still blocks a thread on the source side.
* **Implications**: Client can aggregate or scatter-gather results efficiently, reducing total wall-clock time. Source scalability remains limited by its thread pool.

### 3. Synchronous Client, Asynchronous Source

* **Behavior**: Client blocks on a future or waits on a reactive stream (`.block()`), while the source handles requests non-blocking (e.g., WebClient in a reactive service).
* **Implications**: The source can serve more requests per thread, but client still occupies one thread per request until completion.

### 4. Both Asynchronous

* **Behavior**: Endpoints and inter-service calls are non-blocking (e.g., WebFlux controllers + WebClient), returning `Mono`/`Flux` or `CompletableFuture` at each layer.
* **Implications**: Maximized throughput and resource efficiency; complexity increases due to reactive programming model and back-pressure handling .

## Java Spring Boot Examples

### A. Fully Synchronous Chain

```java
@RestController
@RequestMapping("/client")
public class ClientController {
    private final RestTemplate restTemplate;
    public ClientController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }
    @GetMapping("/sync")
    public SourceResponse fetchSync(@RequestParam String param) {
        return restTemplate.postForObject(
            "http://source/api/data", new SourceRequest(param), SourceResponse.class);
    }
}
```

* Uses `RestTemplate`, a blocking HTTP client.

### B. Asynchronous Client, Synchronous Source

```java
@Service
@EnableAsync
public class ClientService {
    private final RestTemplate restTemplate;
    @Async
    public CompletableFuture<SourceResponse> fetchAsync(SourceRequest req) {
        SourceResponse resp = restTemplate.postForObject(
            "http://source/api/data", req, SourceResponse.class);
        return CompletableFuture.completedFuture(resp);
    }
}
```

```java
@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService service;
    public ClientController(ClientService service) {
        this.service = service;
    }
    @GetMapping("/async")
    public CompletableFuture<SourceResponse> fetch(@RequestParam String param) {
        return service.fetchAsync(new SourceRequest(param));
    }
}
```

* Leverages `@Async` with `CompletableFuture`.

### C. Synchronous Client, Asynchronous Source

```java
@Service
public class ClientService {
    private final WebClient webClient = WebClient.create("http://source");
    public SourceResponse fetch() {
        return webClient.post()
            .uri("/api/data")
            .bodyValue(new SourceRequest("param"))
            .retrieve()
            .bodyToMono(SourceResponse.class)
            .block(); // blocking at client
    }
}
```

* Source can handle requests non-blocking, but client blocks on `.block()`.

### D. Fully Asynchronous (Reactive) Chain

```java
@RestController
@RequestMapping("/client")
public class ReactiveController {
    private final WebClient webClient = WebClient.create("http://source");
    @GetMapping("/reactive")
    public Mono<SourceResponse> fetchReactive(@RequestParam String param) {
        return webClient.post()
            .uri("/api/data")
            .bodyValue(new SourceRequest(param))
            .retrieve()
            .bodyToMono(SourceResponse.class);
    }
}
```

* Both controller and client calls return `Mono<…>` without blocking .

---
