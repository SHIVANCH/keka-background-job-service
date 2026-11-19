# Background job HTTP service (Keka interview)

Small Spring Boot service: submit jobs with **HIGH / MEDIUM / LOW** priority and track status.

| Priority | Behaviour |
|----------|-----------|
| **HIGH** | Saved, then processed **immediately** on a mock worker thread |
| **MEDIUM** | Saved, **queued** for background worker (later processing) |
| **LOW** | Saved as **CREATED** only — no worker |

## Run

```bash
mvn spring-boot:run
```

Server: `http://localhost:8080`

## API

**Submit job**

```bash
curl -s -X POST http://localhost:8080/jobs \
  -H "Content-Type: application/json" \
  -d '{"payload":"hello","priority":"HIGH"}'
```

**Track status / outcome**

```bash
curl -s http://localhost:8080/jobs/<jobId>
```

Statuses: `CREATED`, `QUEUED`, `RUNNING`, `COMPLETED`, `FAILED`