# Nim Game API

A minimal REST API implementation of the Misère variant of the classic game Nim, where players take turns removing 1–3 tokens, and the player forced to take the **last token loses**

## Features

- Two computer strategies (`random-computer`, `smart-computer`)
- Misère rule logic (last token loses)
- Stateless REST interface with in-memory game sessions
- Swagger UI for interactive API testing
- Cache with auto-expiry and max size
- Profile-based strategy switching

## Technologies

- Java 21
- Spring Boot 3.x
- Spring Web, Validation, Caffeine Cache
- MapStruct for DTO mapping
- springdoc-openapi for Swagger UI
- JUnit 5 for testing

## Getting Started

### Prerequisites

- Java 21
- Gradle (or use the wrapper: `./gradlew`)

### Run locally

```bash
./gradlew bootRun
````

### Default Swagger UI

* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## How to Play

1. **Create a new game**
   `POST /v1/games/create`
   Optional: Choose starting player (`HUMAN` or `COMPUTER`)

2. **Submit a move**
   `POST /v1/games/{id}/turn` with `takeTokens: 1-3`

3. **Check current game state (optional)**
   `GET /v1/games/{id}`

Game ends when the last token is taken. The player who takes it **loses**.

## Tests

Run all tests:

```bash
./gradlew test
```

Includes:

* Service & rule logic tests
* Strategy-specific tests (smart/random)
* Controller tests

## Configuration

See `application.yml` for available options:

```yaml
spring:
  cache:
    caffeine:
      spec: maximumSize=100,expireAfterAccess=4h
  profiles:
    active:
    - smart-computer  # or random-computer

game:
  starting-tokens: 13
  default-starting-player: COMPUTER  # or HUMAN
```

## Authentication & Access Control

This project **does not implement any authentication or access restrictions**.
All API endpoints are publicly accessible for demonstration and testing purposes.

## Monitoring & Actuator

This project **does not use Spring Boot Actuator**.
No runtime metrics, health endpoints, or environment diagnostics are exposed.

## Dependency Vulnerability Check

This project uses the [OWASP Dependency-Check](https://owasp.org/www-project-dependency-check/) plugin to identify known security vulnerabilities in third-party libraries via the National Vulnerability Database (NVD).

### Run security scan

```bash
./gradlew dependencyCheckAnalyze -DNVD_API_KEY=<your-api-key>
```

## License

This project is licensed under **CC BY-NC 4.0** — free to use and modify **for non-commercial purposes only**.
See [`LICENSE`](./LICENSE) for details.
