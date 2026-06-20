# Run & Test Commands

Commands to build, run, and test the ad event click processor locally.
Run from the project root unless noted otherwise.

---

## 1. Build the project

```bash
cd ad-event-click-processor
mvn clean install
```

Compile only:

```bash
mvn clean compile
```

---

## 2. Start infrastructure (Docker)

```bash
docker compose up -d
```

Verify containers are running:

```bash
docker compose ps
```

Expected services:

| Service     | Port |
|-------------|------|
| LocalStack  | 4566 |
| Redis       | 6379 |
| PostgreSQL  | 5432 |

---

## 3. Start the consumer (Terminal 1)

Start **before** click-api so the database schema is created (`ddl-auto: update`).

```bash
mvn spring-boot:run -pl click-consumer
```

Consumer runs on port **8081**.

---

## 4. Start the API (Terminal 2)

```bash
mvn spring-boot:run -pl click-api
```

API runs on port **8080**.

---

## 5. Send a click event

```bash
curl -X POST http://localhost:8080/api/v1/clicks \
  -H "Content-Type: application/json" \
  -d '{"adId":"AD-123","userId":"USER-456"}'
```

Expected response: **HTTP 202 Accepted** (no body).

Send a few more sample events:

```bash
curl -X POST http://localhost:8080/api/v1/clicks \
  -H "Content-Type: application/json" \
  -d '{"adId":"NIKE-001","userId":"USER-1"}'

curl -X POST http://localhost:8080/api/v1/clicks \
  -H "Content-Type: application/json" \
  -d '{"adId":"NIKE-001","userId":"USER-2"}'

curl -X POST http://localhost:8080/api/v1/clicks \
  -H "Content-Type: application/json" \
  -d '{"adId":"APPLE-001","userId":"USER-1"}'
```

---

## 6. Test deduplication (Redis)

Send the **same** `adId` + `userId` twice within 24 hours.
The first request is processed; the second is skipped as a duplicate.

```bash
curl -X POST http://localhost:8080/api/v1/clicks \
  -H "Content-Type: application/json" \
  -d '{"adId":"AD-123","userId":"USER-456"}'

curl -X POST http://localhost:8080/api/v1/clicks \
  -H "Content-Type: application/json" \
  -d '{"adId":"AD-123","userId":"USER-456"}'
```

Check consumer logs for:

- `Received click event:` (first)
- `Duplicate click event skipped:` (second)

Inspect Redis dedup keys:

```bash
docker compose exec redis redis-cli KEYS 'click:*'
```

---

## 7. Verify PostgreSQL data

List all persisted events:

```bash
docker compose exec postgres psql -U adtech -d ad_events -c "SELECT * FROM click_events;"
```

Count events per ad:

```bash
docker compose exec postgres psql -U adtech -d ad_events -c \
  "SELECT ad_id, COUNT(*) AS click_count FROM click_events GROUP BY ad_id ORDER BY click_count DESC;"
```

Interactive psql session:

```bash
docker compose exec -it postgres psql -U adtech -d ad_events
```

Useful SQL inside psql:

```sql
SELECT * FROM click_events ORDER BY processed_timestamp DESC LIMIT 10;
\q
```

---

## 8. Query analytics endpoint

```bash
curl http://localhost:8080/api/v1/analytics/top-ads
```

Example response:

```json
[
  { "adId": "NIKE-001", "clickCount": 2 },
  { "adId": "APPLE-001", "clickCount": 1 }
]
```

---

## 9. Optional: inspect SQS queue (requires AWS CLI)

```bash
aws --endpoint-url=http://localhost:4566 sqs list-queues --region us-east-1

aws --endpoint-url=http://localhost:4566 sqs get-queue-attributes \
  --queue-url http://localhost:4566/000000000000/click-events \
  --attribute-names ApproximateNumberOfMessages \
  --region us-east-1
```

---

## 10. Stop everything

Stop Spring Boot apps: press **Ctrl+C** in each terminal running `mvn spring-boot:run`.

Or kill by port:

```bash
lsof -ti:8080 | xargs kill
lsof -ti:8081 | xargs kill
```

Stop Docker infrastructure:

```bash
docker compose down
```

Stop Docker and remove Postgres volume (clears all DB data):

```bash
docker compose down -v
```

Stop everything in one command:

```bash
docker compose down && lsof -ti:8080,8081 | xargs kill 2>/dev/null
```

---

## Quick reference

| Component       | URL / Connection                                      |
|-----------------|-------------------------------------------------------|
| Click API       | http://localhost:8080                                 |
| Click Consumer  | http://localhost:8081                                 |
| PostgreSQL      | `jdbc:postgresql://localhost:5432/ad_events`          |
| Postgres creds  | user `adtech`, password `adtech`                      |
| Redis           | `localhost:6379`                                      |
| LocalStack SQS  | `http://localhost:4566`                               |

| Endpoint                      | Method | Description              |
|-------------------------------|--------|--------------------------|
| `/api/v1/clicks`              | POST   | Ingest a click event     |
| `/api/v1/analytics/top-ads`   | GET    | Top 10 ads by click count|
