### **📌 RestClient vs WebClient: Blocking vs Non-Blocking Explained Simply**

### **1️⃣ RestClient (Blocking - Synchronous)**
- **Blocking:** The thread waits for the API response before continuing execution.
- **How it Works:**
    - we make a request.
    - The thread waits (blocked) until the response comes back.
    - Execution resumes after the response is received.

#### **Example (RestClient - Blocking)**
```java
Item item = RestClient.create("http://localhost:8080/api/items/1")
        .get()
        .retrieve()
        .body(Item.class); // Waits (blocking) for response
System.out.println(item); // Only prints after response is received
```
✅ **When to Use RestClient?**
- When calls must happen **one after another** (sequential execution).
- When performance isn't a major concern (e.g., simple apps).
- When working in a **single-threaded** environment (like command-line tools).

🚀 **Advantages of RestClient**
- Easier to understand & debug.
- No complex async handling.
- Better suited for traditional Spring MVC applications.

❌ **Disadvantages**
- Wastes time **waiting** for responses.
- Not scalable for high-performance applications.

---

### **2️⃣ WebClient (Non-Blocking - Asynchronous)**
- **Non-Blocking:** The thread **does NOT wait** for the response and continues executing other tasks.
- **How it Works:**
    - we make a request.
    - The response is processed **when it arrives**, without blocking the main thread.
    - Other tasks can run in the meantime.

#### **Example (WebClient - Non-Blocking)**
```java
WebClient webClient = WebClient.create("http://localhost:8080");

webClient.get()
    .uri("/api/items/1")
    .retrieve()
    .bodyToMono(Item.class) // Doesn't block, returns Mono<Item>
    .subscribe(item -> System.out.println("Item received: " + item));

System.out.println("Request sent, doing other work...");
```
### **🛑 What is `.subscribe()`?**
- **It tells WebClient to execute the request asynchronously.**
- The request runs in the background.
- The callback (`item -> System.out.println(...)`) is executed **when the response is ready**.

---

### **📌 Automatic Subscription & Return Handling in WebClient**
#### **Why Doesn't WebClient Need `.subscribe()` in Controllers or Services?**
- When using WebClient in a **Spring Controller**, **Spring automatically subscribes** to `Mono<T>` or `Flux<T>`, so explicit `.subscribe()` is **not needed**.
- If a service method returns `Mono<T>`, it **does not execute immediately**. Instead, execution happens **when the controller subscribes** (implicitly handled by Spring WebFlux).

#### **What Happens if the Background Thread is Running But `return` Executes?**
- In a **blocking** call (`RestClient`), execution **waits** for the response before reaching `return`.
- In a **non-blocking WebClient call**, the request runs **in the background**, and `return` may execute **before the response arrives**.
- **However, if the method returns `Mono<T>`, Spring waits for the result before sending the response.**

#### **Example: WebClient in Service & Controller (No `.subscribe()`)**
##### **Service Layer (Async WebClient)**
```java
@Service
public class ItemService {
    private final WebClient webClient;

    public ItemService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public Mono<Item> getItemById(Long id) {
        return webClient.get()
                .uri("/api/items/{id}", id)
                .retrieve()
                .bodyToMono(Item.class); // Async call, no .subscribe()
    }
}
```
##### **Controller Layer (Returning `Mono<Item>`)**
```java
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public Mono<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id); // Spring handles subscription automatically
    }
}
```
#### **Why Doesn't This Cause Issues?**
- Even though WebClient is **async**, returning `Mono<Item>` tells Spring to **wait** for the result before sending the HTTP response.
- If we try to return `Item` instead of `Mono<Item>`, it **won’t work** because the API call is still running.

#### **What If we Try to Return `Item` Instead of `Mono<Item>`?**
❌ **Wrong way:**
```java
@GetMapping("/{id}")
public Item getItemById(@PathVariable Long id) {
    return itemService.getItemById(id); // ❌ Won't work, `Mono<Item>` cannot be assigned to `Item`
}
```
✅ **Correct way:**
```java
@GetMapping("/{id}")
public Mono<Item> getItemById(@PathVariable Long id) {
    return itemService.getItemById(id); // ✅ Works fine
}
```
---

### **💡 Summary: When to Use What?**
| Feature | RestClient (Blocking) | WebClient (Non-Blocking) |
|---------|----------------------|--------------------------|
| **Type** | Synchronous (Blocking) | Asynchronous (Non-Blocking) |
| **Execution** | Waits for response before proceeding | Doesn't wait, does other work |
| **Performance** | Slower (blocks thread) | Faster (doesn't block) |
| **Scalability** | Less scalable | Highly scalable |
| **Use Case** | Simple apps, sequential calls | Microservices, parallel API calls |

🔹 **Use RestClient** for simple synchronous calls.  
🔹 **Use WebClient** when we need high performance and non-blocking execution.

---

### **🔥 Quick Recap**
- **Blocking:** Thread waits for API response → **RestClient**.
- **Non-Blocking:** Thread doesn’t wait, does other tasks → **WebClient**.
- **Spring WebFlux automatically subscribes** to `Mono<T>` in controllers.
- **`subscribe()` is only needed in standalone WebClient calls.**