**API (Application Programming Interface)** is a **contract** (set of rules and protocols) that allows one software application to interact with another. It defines *what* a service offers and *how* it can be consumed—typically via requests and responses over a network. APIs decouple functionality and promote **modularity**, **reusability**, and **interoperability** between systems.

---

## 🔹 **Brief History**

* **1960s–1980s**: APIs were primarily internal—used by libraries and operating systems (e.g., POSIX APIs).
* **1990s**: Component-based APIs (like COM, CORBA).
* **2000s**: Rise of **Web APIs** (SOAP, XML-RPC).
* **Mid-2000s onward**: RESTful APIs dominate due to simplicity and HTTP alignment.
* **2015+**: GraphQL, gRPC, and async APIs gain traction for specific scalability and efficiency needs.

---

## 🔹 **Types of APIs**

| Type          | Protocol/Tech         | Use Case                               | Data Format      |
| ------------- | --------------------- | -------------------------------------- | ---------------- |
| REST API      | HTTP (stateless)      | Web services, CRUD apps                | JSON, XML        |
| GraphQL       | HTTP (flexible query) | Complex UIs, multiple nested resources | JSON             |
| gRPC          | HTTP/2 + Protobuf     | Internal microservices, low-latency    | Protocol Buffers |
| SOAP API      | XML over HTTP/SOAP    | Legacy enterprise systems              | XML              |
| WebSocket API | TCP (full-duplex)     | Real-time chat, gaming                 | JSON/Text/Binary |
| OpenAPI       | REST spec (Swagger)   | API documentation, code generation     | YAML/JSON        |

---

## 🔹 **Definitions with Simple Example**

### REST API Example:

```http
GET /users/123
→ 200 OK
{
  "id": 123,
  "name": "Ashfaq"
}
```

### GraphQL API Example:

```graphql
query {
  user(id: 123) {
    id
    name
  }
}
```

### gRPC (IDL-based)

```proto
service UserService {
  rpc GetUser (UserRequest) returns (UserResponse);
}
```

---

## 🔹 **When to Use What?**

| Scenario                                    | Use API Type |
| ------------------------------------------- | ------------ |
| CRUD-based applications                     | REST         |
| Need to reduce over-fetching/under-fetching | GraphQL      |
| High-performance internal services          | gRPC         |
| Enterprise integrations with legacy systems | SOAP         |
| Real-time updates (chat, trading)           | WebSocket    |

---

## 🔹 **Pros and Cons**

### REST

**Pros**:

* Simple
* Stateless
* Cacheable
* Widespread adoption

**Cons**:

* Over/Under-fetching
* Multiple round-trips for nested resources

---

### GraphQL

**Pros**:

* Single request for nested/related data
* Declarative querying
* Strong typing (Schema-first)

**Cons**:

* Caching is hard
* Performance pitfalls with complex queries
* Learning curve

---

### gRPC

**Pros**:

* Very fast (HTTP/2, binary)
* Bi-directional streaming
* Ideal for inter-service communication

**Cons**:

* Not human-readable
* Browser support lacking (needs proxy)
* Harder to debug without tooling

---

### SOAP

**Pros**:

* Strict contract
* Built-in security (WS-Security)
* Enterprise support (e.g., transactions)

**Cons**:

* Verbose XML
* Complex tooling
* Heavyweight

---

### WebSocket

**Pros**:

* Full-duplex
* Low latency
* Ideal for push-based systems

**Cons**:

* Stateful connection
* Hard to scale horizontally
* Requires fallback handling

---

## 🔹 **Designing Good APIs: Key Principles**

1. **Consistency**

   * Use standard naming conventions (`/users`, `/orders`)
   * Follow HTTP verbs appropriately (`GET`, `POST`, `PUT`, `DELETE`)

2. **Statelessness**

   * Each request should carry all necessary context

3. **Versioning**

   * Avoid breaking changes (`/v1/users`)

4. **Validation & Error Handling**

   * Standardize error formats (`400`, `404`, `422`, `500`)

5. **Security**

   * Use OAuth2/JWT for authentication
   * HTTPS everywhere

6. **Rate Limiting & Throttling**

   * Avoid abuse, protect backend

7. **Documentation**

   * Use OpenAPI/Swagger or GraphQL introspection

8. **Monitoring**

   * Collect logs, metrics, tracing (e.g., using Prometheus, Zipkin)

9. **Idempotency**

   * `PUT` and `DELETE` should be idempotent (same result on repeated calls)

10. **Pagination & Filtering**

    * Avoid large payloads (`?page=1&size=20`, `?status=active`)

---

## 🔹 Summary Table

| Feature     | REST        | GraphQL     | gRPC         |
| ----------- | ----------- | ----------- | ------------ |
| Readability | High        | Medium      | Low          |
| Performance | Medium      | Medium-High | High         |
| Tooling     | Rich        | Rich        | Moderate     |
| Flexibility | Low         | High        | Low          |
| Best for    | Public APIs | Complex UIs | Internal RPC |

---
