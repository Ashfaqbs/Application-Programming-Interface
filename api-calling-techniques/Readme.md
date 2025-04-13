### 🔁 Normal REST API (`/user/{id}`)
> Like: `GET /user/123`

When we call this:
- our Spring `RestTemplate` or WebClient sends a request.
- Server gives the response and **closes** the connection.
- One-time communication. That’s it.

✅ Good for:
- Simple reads (like getting user info).
- Operations where no real-time/ongoing data is needed.

---

### 🔄 What About APIs That *Continuously* Send Data?

These are **not typical REST APIs**. They fall into one of these categories:

#### 1. **Polling** (Normal or Long Polling)
we (the client) **keep calling** the server repeatedly:
- Example: `/events/latest` every 5 seconds.
- Server gives new data *if available*.
- **Client controls the loop**, not the server.
- Heavy on network/resources if frequent.

#### 2. **SSE (Server-Sent Events)**
we hit `/sse/stream` **once**, server:
- **Keeps the connection open**.
- **Pushes updates** as they happen (`data: ...\n\n` format).
- Efficient because no need to reconnect.
- One-way: server ➝ client only.

#### 3. **WebSockets**
- Full **two-way** communication (duplex).
- One-time handshake → then both client/server can send data anytime.
- Used in real-time apps (chat, gaming, stock updates).

#### 4. **Webhooks**
- Different from above. Instead of client asking,
- Server **calls the client** when event happens (we expose an endpoint).
- Passive — we wait to be notified.

---

### 🔥 So what makes an API *"continuously sending data"*?

It’s either:
- The server is coded to **push updates** continuously (SSE, WebSockets).
- Or the client is coded to **keep asking** (polling).

The *underlying protocol* (HTTP vs persistent connection) and *data push direction* define this behavior.

---

### 👇 Quick Compare Table

| Type         | Who Starts? | Keeps Connection? | Push or Pull? | Real-Time? | Bi-directional? |
|--------------|-------------|-------------------|---------------|------------|------------------|
| REST (Normal) | Client      | ❌ No              | Pull          | ❌ No       | ❌ No             |
| Polling       | Client      | ❌ No              | Pull          | ⚠️ Partial  | ❌ No             |
| Long Polling  | Client      | ✅ Until response  | Pull          | ✅ Yes      | ❌ No             |
| SSE           | Client      | ✅ Yes             | Push (1-way)  | ✅ Yes      | ❌ No             |
| WebSocket     | Client      | ✅ Yes             | Push+Pull     | ✅ Yes      | ✅ Yes            |
| Webhook       | Server      | ❌ No              | Push (1-way)  | ✅ Yes      | ❌ No             |

---


- 🔹 **One-Time Content APIs**  
- 🔸 **Streamed/Dynamic Content APIs**  

And we’ll explore **use-cases**, **who is source**, **who is caller**, and **what is happening**.

---

## 🔹 One-Time Content APIs (Static Request-Response)

### ✅ What it means:
- **Client asks once**, gets data back, **done**.
- Every call is **independent**.
- No need to keep the connection alive.

### 📍Used for:
- Getting user profile (`/user/{id}`)
- Fetching static config or metadata (`/settings`)
- Saving form data (POST requests)
- Authenticating login

### 🔧 Tech Used:
- Regular **REST APIs** (`GET`, `POST`, etc.)
- Tools: `RestTemplate`, `WebClient`, `HttpClient`, Postman etc.

### 📥 Interaction Flow:
| Role    | Who is it?     | What they do                        |
|---------|----------------|-------------------------------------|
| 🔹 Source | Server         | Holds the data                      |
| 🔸 Caller | Client (our SpringBoot app) | Sends HTTP request, gets response |

---

## 🔸 Dynamic / Streaming Content APIs

These are **used when data keeps changing**, and we want:
- **Real-time** updates
- **Low-latency** or event-based communication

---

### ✅ What it means:
- Either the **client keeps asking** (polling),
- Or the **server keeps sending** (stream/push),
- Or **server informs** the client when there's something new (webhook).

---

### 📍Used for:
- Live dashboards
- Stock price updates
- IoT sensors sending data
- Notifications
- Chat systems

---

### 🔄 Types of Stream/Dynamic APIs

| Type             | Who initiates | Keeps connection? | Communication | Push/Pull | Use Case Example |
|------------------|---------------|-------------------|---------------|-----------|------------------|
| **Polling**       | Client        | ❌ (repeats)       | 1-way         | Pull      | Refreshing new messages every 5s |
| **Long Polling**  | Client        | ✅ (till data/timeout) | 1-way         | Pull      | Facebook message indicator |
| **SSE**           | Client        | ✅ (single connection) | 1-way         | Push      | Live notifications |
| **WebSocket**     | Client        | ✅                 | 2-way         | Push + Pull | Multiplayer games, Chat |
| **Webhook**       | Server        | ❌ (calls our exposed API) | 1-way         | Push      | Payment Gateway informing our system |

---

### 🔧 Who is the Source vs Caller?

| Type         | Source (Data Origin) | Caller/Initiator | Notes |
|--------------|----------------------|------------------|-------|
| REST         | Server               | Client           | Client just asks, server responds |
| Polling      | Server               | Client           | Client keeps asking |
| Long Polling | Server               | Client           | Client waits for server to respond with new data |
| SSE          | Server               | Client           | Client connects, server pushes |
| WebSocket    | Both (Bi-directional)| Client           | Once connected, both can push data |
| Webhook      | Server               | Server           | Server sends data to client endpoint |

---

### 💡 Key Differentiation Phrase

- 🔹 **Static/One-time API** = “Request-Driven Content”  
- 🔸 **Streaming/Dynamic API** = “Event-Driven or Data-Driven Stream”

---

## 🧠 Summary

| Aspect              | One-Time APIs                   | Stream/Dynamic APIs                           |
|---------------------|----------------------------------|-----------------------------------------------|
| Nature              | One request, one response        | Continuous or event-based                     |
| Data Change         | Rarely changes or needs refresh | Updates frequently / needs to be tracked live |
| Client-Server Role  | Client asks, server answers      | Mixed: Polling (client), SSE/Webhook (server) |
| Connection Type     | Short-lived                     | Long-lived (SSE, WS), or repeat (polling)     |
| Examples            | User info, login, form submit    | Chat, alerts, dashboard, live feed            |

---