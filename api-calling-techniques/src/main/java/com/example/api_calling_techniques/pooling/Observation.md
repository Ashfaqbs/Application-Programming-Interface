## 🧾 Async Polling in Spring Boot — Observations & Explanation

---

### ✅ Goal
Implement **non-blocking async polling** using `@Scheduled` and `@Async` in Spring Boot, backed by a custom thread pool.

---

### 🔍 Observation 1: **No `@Async`, No Executor Bean**
```java
@Scheduled(fixedRate = 5000)
public void pollAsync() {
    // logic
}
```
**Thread Output:** `scheduling-1`

**Explanation:**
- Only `@Scheduled` is active.
- Spring uses its internal scheduler thread pool.
- This is **not async**, but it still “works” because `@Scheduled` is managing thread execution on its own.

✅ **Result:** Executes at interval, but blocks scheduler thread if logic is heavy.

---

### 🔍 Observation 2: **Only Added Executor Bean, No `@EnableAsync`**
```java
@Bean
public Executor taskExecutor() {
    return Executors.newCachedThreadPool();
}
```
```java
@Scheduled(fixedRate = 5000)
@Async
public void pollAsync() { ... }
```

**Thread Output:** Still `scheduling-1`

**Explanation:**
- we defined a thread pool, but **Spring isn’t told to use it** because `@EnableAsync` is missing.
- So `@Async` is ignored.
- Execution still happens on Spring's scheduler (`@Scheduled`) thread pool.

❌ **Result:** Not async. The executor exists but isn’t linked to `@Async`.

---

### 🔍 Observation 3: **Only Added `@EnableAsync`, No Executor Bean**
```java
@EnableAsync
```
```java
@Async
public void pollAsync() { ... }
```

**Thread Output:** `SimpleAsyncTaskExecutor-1`

**Explanation:**
- `@EnableAsync` activates async behavior.
- No executor bean provided, so Spring falls back to its **default**, lightweight `SimpleAsyncTaskExecutor`.

✅ **Result:** Async works. But thread pool is unbounded and not production-grade.

---

### ✅ Final Setup: Added Both Correctly

```java
@Configuration
@EnableAsync
public class AppConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-exec-");
        executor.initialize();
        return executor;
    }
}
```

```java
@SpringBootApplication
@EnableScheduling
public class App { ... }

@Service
public class PollingService {
    @Scheduled(fixedRate = 5000)
    @Async
    public void pollAsync() {
        System.out.println("Running on thread: " + Thread.currentThread().getName());
        // Async WebClient call...
    }
}
```

**Thread Output:** `async-exec-1`

**Explanation:**
- `@EnableAsync` activates async processing
- Custom `Executor` bean provides proper thread pool
- `@Async` now runs on the defined thread pool

✅ **Result:** True async + controlled threading ✅ Production ready

---

### 🧠 Summary Table

| Case | `@EnableAsync` | Custom `Executor` Bean | Async Execution Works? | Thread Name |
|------|----------------|------------------------|-------------------------|-------------|
| ❌ Neither            | ❌ No         | ❌ No                 | ❌ No                    | `scheduling-1` |
| ❌ Only Executor Bean | ❌ No         | ✅ Yes                | ❌ No                    | `scheduling-1` |
| ✅ Only `@EnableAsync`| ✅ Yes        | ❌ No                 | ✅ Yes (default pool)    | `SimpleAsyncTaskExecutor-1` |
| ✅ Both               | ✅ Yes        | ✅ Yes                | ✅ Yes                   | `async-exec-1` |

---