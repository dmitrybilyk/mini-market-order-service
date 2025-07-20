# 🛒 Mini Market Order Service

A Spring Boot REST API that allows clients to submit BUY/SELL market orders for any stock symbol, fetches a price from a mock feed (WireMock), persists order & execution info to PostgreSQL, and applies per-account rate-limiting.

---

## 📦 Tech Stack

| Layer         | Technology                        |
|---------------|-----------------------------------|
| Language      | Java 21                           |
| Framework     | Spring Boot 3                     |
| Database      | PostgreSQL 16                     |
| Mock Service  | WireMock                          |
| Testing       | JUnit 5, Mockito, Testcontainers  |
| Docs          | Springdoc OpenAPI                 |
| Packaging     | Docker, bootBuildImage            |

---

## 🚀 Features

- ✅ Submit market orders (BUY or SELL)
- ✅ Real-time price lookup (stubbed)
- ✅ Store orders and executions
- ✅ Per-account rate limit: 10 requests/sec
- ✅ Docker Compose setup (App + DB + Mock API)
- ✅ Swagger UI & OpenAPI documentation
- ✅ 80%+ unit test coverage
- ✅ Integration tests with Testcontainers

---

## 🐳 Dockerized Run

### 🔧 1. Build Docker Image

```bash
./gradlew clean bootBuildImage
```

This creates the image `orders-service:0.0.1-SNAPSHOT`.

---

### ▶ 2. Run Docker Compose

```bash
docker compose up --build
```

This will start:
- ✅ `orders-service` (Spring Boot)
- ✅ `postgres` (with schema auto-created)
- ✅ `wiremock` (mock price feed)

---

## 🌐 API Endpoints

### 📤 POST `/orders` — Create Order

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "accountId": "acc-123",
    "symbol": "AAPL",
    "side": "BUY",
    "quantity": 10,
    "status": "CREATED",
    "createdAt": "2025-07-20T12:34:56Z"
}'
```

🟢 Sample Response:

```json
{
  "symbol": "AAPL",
  "price": 210.550000
}
```

---

### 📥 GET `/orders` — Get All Orders

```bash
curl -X GET http://localhost:8080/orders \
  -H "Accept: application/json"
```

---

### 📥 GET `/orders?accountId=acc-123` — Get Orders by Account

```bash
curl -X GET "http://localhost:8080/orders?accountId=acc-123" \
  -H "Accept: application/json"
```

---

## 📊 API Docs

After starting the service:

> 🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## ✅ Test Execution

### 🔬 Unit Tests

```bash
./gradlew test
```

### 🧪 Integration Tests (Testcontainers)

```bash
./gradlew integrationTest
```

---

## 🧾 Code Coverage

### Generate coverage report using JaCoCo:

```bash
./gradlew jacocoTestReport
```

📂 View report:

```
build/reports/jacoco/test/html/index.html
```

---

## 📂 Project Structure

```
.
├── src
│   ├── main
│   │   ├── java/com/minimarket/orders/orderservice
│   │   │   ├── controller
│   │   │   ├── model
│   │   │   ├── repository
│   │   │   ├── ratelimiter
│   │   │   ├── service
│   │   └── resources
│   │       └── application.yml
│   └── test
│       ├── unit
│       └── integration
├── Dockerfile
├── docker-compose.yml
├── README.md
└── build.gradle.kts
```

---

## ⚙️ Docker Compose Config

```yaml
version: "3.8"

services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: mini_market
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  wiremock:
    image: wiremock/wiremock:3.4.2
    ports:
      - "8085:8080"
    volumes:
      - ./wiremock:/home/wiremock
    command: --global-response-templating

  order-service:
    image: orders-service:0.0.1-SNAPSHOT
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/mini_market
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: password
      WIREMOCK_HOST: wiremock
      WIREMOCK_PORT: 8080
      SPRING_PROFILES_ACTIVE: docker
    depends_on:
      - postgres
      - wiremock

volumes:
  postgres_data:
```

---

## 🧩 WireMock Stub (`./wiremock/mappings/price-feed.json`)

```json
{
  "request": {
    "method": "GET",
    "urlPattern": "/price\\?symbol=AAPL"
  },
  "response": {
    "status": 200,
    "jsonBody": {
      "symbol": "AAPL",
      "price": 210.55
    },
    "headers": {
      "Content-Type": "application/json"
    }
  }
}
```

---

## 🧨 Troubleshooting

| Problem | Solution |
|--------|----------|
| `Connection refused: wiremock` | Make sure `WIREMOCK_HOST=wiremock` is used and containers are in same network. |
| `wiremock.host` is null | Confirm `@Value` is used in constructor, not field. |
| `postgres connection refused` | Make sure PostgreSQL is up and healthy in Compose logs. |
| `priceFeed null` | Ensure correct JSON mapping + stub setup in `/wiremock/mappings`. |

---

## 📋 License

MIT or your custom license.
