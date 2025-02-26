In Spring WebFlux's functional programming model, the `ServerResponse` class is pivotal for crafting HTTP responses. It offers a fluent API to define response attributes such as status codes, headers, cookies, and the response body. 

**Setting HTTP Status Codes**

To specify the HTTP status code for a response, `ServerResponse` provides several methods: 

- **Predefined Status Methods**: For common HTTP statuses,  we can use methods like `ok()` for 200 OK, `created(URI location)` for 201 Created, `noContent()` for 204 No Content, etc. 

  ```java
  import org.springframework.web.reactive.function.server.ServerResponse;
  import reactor.core.publisher.Mono;

  public Mono<ServerResponse> handleRequest() {
      return ServerResponse.ok().build(); // Returns a 200 OK response
  }
  ```

- **Custom Status Codes**: For other status codes, use the `status(HttpStatus status)` method: 

  ```java
  import org.springframework.http.HttpStatus;
  import org.springframework.web.reactive.function.server.ServerResponse;
  import reactor.core.publisher.Mono;

  public Mono<ServerResponse> handleRequest() {
      return ServerResponse.status(HttpStatus.ACCEPTED).build(); // Returns a 202 Accepted response
  }
  ```

**Adding Headers**

To include HTTP headers in the response, utilize the `header(String headerName, String... headerValues)` method: 

```java
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public Mono<ServerResponse> handleRequest() {
    return ServerResponse.ok()
            .header("Custom-Header", "HeaderValue")
            .build();
}
```


For setting common headers, such as `Content-Type`, dedicated methods are available: 

```java
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public Mono<ServerResponse> handleRequest() {
    return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .build();
}
```

**Setting Cookies**

To add cookies to the response, use the `cookie(ResponseCookie cookie)` method: 

```java
import org.springframework.http.ResponseCookie;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public Mono<ServerResponse> handleRequest() {
    ResponseCookie cookie = ResponseCookie.from("user-token", "abc123")
                                          .httpOnly(true)
                                          .secure(true)
                                          .build();

    return ServerResponse.ok()
            .cookie(cookie)
            .build();
}
```

**Defining the Response Body**

To include a body in the response, methods like `body(BodyInserter<?, ? super ClientHttpResponse> inserter)` or `bodyValue(Object body)` are used: 

```java
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public Mono<ServerResponse> handleRequest() {
    String responseBody = "Hello, World!";
    return ServerResponse.ok()
            .bodyValue(responseBody);
}
```


For reactive data types, such as `Mono` or `Flux`, the `body` method is appropriate: 

```java
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public Mono<ServerResponse> handleRequest() {
    Mono<String> responseBody = Mono.just("Hello, Reactive World!");
    return ServerResponse.ok()
            .body(responseBody, String.class);
}
```

**Comprehensive Example**

Combining these elements, here's a handler function that sets a status code, adds headers and cookies, and includes a response body: 

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class MyHandler {

    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        ResponseCookie cookie = ResponseCookie.from("session-id", "xyz789")
                                              .httpOnly(true)
                                              .secure(true)
                                              .build();

        return ServerResponse.status(HttpStatus.ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Custom-Header", "HeaderValue")
                .cookie(cookie)
                .bodyValue("{\"message\": \"Request accepted\"}");
    }
}
```


In this example, the handler responds with a 202 Accepted status, sets the `Content-Type` to `application/json`, adds a custom header and a cookie, and includes a JSON-formatted string as the body. 

For more detailed information, refer to the [Spring Framework documentation on functional endpoints](https://docs.spring.io/spring-framework/reference/web/webflux-functional.html). 