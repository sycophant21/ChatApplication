# ChatApplication

> HTTP Servlets doing the work WebSockets were invented to replace.

A client-server **chat application** built entirely on Java HTTP Servlets with JSON messaging — no WebSockets, no fancy frameworks. Just plain HTTP doing its best impression of real-time.

## What it does

- Supports multiple clients connecting to a central server
- Clients register themselves on first connect via `ClientRegistrarServlet`
- Messages are sent to the server via `MessageSenderServlet` and polled by recipients via `MessageReceiverServlet`
- The server (`MessageManager`) queues and routes messages between registered clients
- All payloads are JSON-encoded `Message` objects with sender, recipient, and content fields

## How it works

### Architecture

```
ChatClient  ──POST /register──►  ChatServer
            ──POST /send    ──►  MessageManager (stores in queue)
            ◄─GET  /receive ──   MessageManager (drains queue for client)
```

The client runs as a Servlet webapp that exposes its own endpoints for the user. A background thread (or manual polling) calls `/receive` on the server to fetch pending messages for the current user. `Status.java` wraps response codes for consistent error handling.

### Modules

- **ChatServer**: `Main.java` bootstraps the Jetty/Tomcat context. `MessageManager` holds a per-user message queue.
- **ChatClient**: `Main.java` starts the client webapp. `ClientRegistrarServlet` registers with the server on startup. `MessageSenderServlet` accepts outgoing messages from the UI. `MessageReceiverServlet` polls the server for incoming messages.

## Tech stack

- **Java**
- **Java Servlets** (javax.servlet)
- **JSON** for message serialization
- **Maven** (both modules have `pom.xml`)

## Getting started

### Prerequisites

- Java 8+
- Maven 3.6+
- Apache Tomcat 9+ (or any Servlet 4.0 container)

### Build

```bash
# Build both modules
cd ChatServer && mvn clean package
cd ../ChatClient && mvn clean package
```

### Run

1. Deploy `ChatServer/target/*.war` to Tomcat first.
2. Update the server URL in the client config (look for the server host constant in `ClientRegistrarServlet.java`).
3. Deploy `ChatClient/target/*.war` to the same or a different Tomcat instance.
4. Open the client app in a browser and start chatting.

## Project structure

```
ChatServer/src/main/java/
├── Main.java             # server entry point
└── MessageManager.java   # per-user message queue

ChatClient/src/main/java/
├── Main.java                    # client entry point
├── Message.java                 # JSON message model
├── Status.java                  # response status wrapper
├── ClientRegistrarServlet.java  # registers client on startup
├── MessageSenderServlet.java    # handles outgoing messages
└── MessageReceiverServlet.java  # polls server for new messages
```
