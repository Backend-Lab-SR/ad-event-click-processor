# Ad Event Click Processor

A proof-of-concept distributed ad click event processing system built using Spring Boot and an AWS-style event-driven architecture (emulated locally using LocalStack).

## Overview

This project simulates a real-world ad-tech click processing pipeline where user ad clicks are ingested via a REST API, published to a message queue, processed asynchronously by a consumer, deduplicated using Redis, and persisted in PostgreSQL.

It demonstrates key distributed systems concepts such as:

* Event-driven architecture
* Asynchronous processing
* Idempotency and deduplication
* Message queues (AWS SQS via LocalStack)
* Multi-service modular design

## Architecture

```
click-api
   |
   |  (REST API)
   v
SQS Queue (LocalStack)
   |
   v
click-consumer
   |
   +--> Redis (deduplication)
   |
   +--> PostgreSQL (persistence)
```

## Technology Stack

* **Java**: 21
* **Build Tool**: Maven (multi-module)
* **Framework**: Spring Boot 3.2.x
* **Message Queue**: AWS SQS (LocalStack)
* **Cache**: Redis
* **Database**: PostgreSQL
* **Containerization**: Docker & Docker Compose
* **Testing**: JUnit 5 (optional extension)

## Project Structure

```
ad-event-click-processor/
├── pom.xml                      # Parent Maven project
├── common/                      # Shared models (DTOs)
├── click-api/                   # REST API service (event producer)
├── click-consumer/             # Async consumer service
```

### Modules

#### common

Contains shared domain objects such as:

* ClickEvent

#### click-api

Responsible for:

* Accepting click events via REST API
* Publishing events to SQS queue

#### click-consumer

Responsible for:

* Consuming events from SQS
* Deduplicating events using Redis
* Persisting processed events into PostgreSQL

## Prerequisites

* Java 21+
* Maven 3.8+
* Docker & Docker Compose

## Local Infrastructure Setup

Start required dependencies:

```bash
docker-compose up -d
```

This starts:

* LocalStack (SQS)
* Redis
* PostgreSQL

## Building the Project

```bash
mvn clean install
```

## Running the Application

Run API service:

```bash
mvn spring-boot:run -pl click-api
```

Run Consumer service:

```bash
mvn spring-boot:run -pl click-consumer
```

## Example API Usage

### Ingest Click Event

```http
POST /api/v1/clicks
```

```json
{
  "adId": "AD-123",
  "userId": "USER-456"
}
```

## Key Features

* REST-based event ingestion
* Asynchronous processing via SQS
* Redis-based deduplication (idempotency)
* PostgreSQL persistence of processed events
* Multi-module Maven architecture
* Local AWS simulation using LocalStack

## Future Enhancements (Not Implemented)

* Dead Letter Queue (DLQ)
* Retry mechanisms
* Metrics & monitoring (Prometheus/Grafana)
* Load testing generator
* Kubernetes deployment
* Distributed tracing

## License

This project is a POC and is provided as-is.
