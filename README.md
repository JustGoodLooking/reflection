# 📚 Reflection - Telegram Bot for Daily & Yearly Planning

A personal Telegram bot built with Spring Boot, PostgreSQL, Redis, and Docker to help users manage daily and yearly tasks. This project is designed as a **system design practice** and gradually implements architecture patterns like async processing, caching, rate limiting, and scheduled reminders.

---

## ✨ Features

- ✅ Add / view daily and yearly plans via Telegram
- ✅ PostgreSQL schema for user-task management
- ✅ Redis integration (cache, rate limit, deduplication)
- ✅ Scheduled daily reminder via `@Scheduled`
- 🕒 Async processing with RabbitMQ (optional stage)
- 📊 System observability with Prometheus + Grafana (planned)
- 🐳 Docker-based local development

---

## 📦 Tech Stack

- **Backend**: Spring Boot (Java)
- **Database**: PostgreSQL
- **Cache / Rate Limiting**: Redis
- **Queue / Async**: RabbitMQ
- **Infrastructure / Deployment**: Docker, GitHub Actions, (Railway / AWS EC2)
- **Monitoring (optional)**: Prometheus, Grafana, AWS CloudWatch

## 🛣️ System Design Roadmap

This project grows by design — each phase adds real-world architecture layers. The focus is not just features, but **how to build them well**.

| Phase       | Goal | Tech Stack | Redis Usage | Redis Tips |
|-------------|------|------------|--------------|------------|
| **Phase 1** | Basic CRUD: Telegram → Spring → PostgreSQL | Spring Boot, PostgreSQL | Not used | Keep it simple for MVP debugging |
| **Phase 2** | Daily reminders | `@Scheduled` + DB query | `reminder:userId:2025-05-12` for deduplication | Use `SET NX EX` to prevent duplicates |
| **Phase 3** | Cache + Rate limiting | Redis TTL, ZSet, counter | `dailyPlan:userId:date`, `user:{id}:cmd_count` | Consider warm-up cache; fallback on limit fail |
| **Phase 4** | Async message processing | RabbitMQ + JSON tasks | Optional: use Redis for locking / tracking | Add idempotent check in consumer |
| **Phase 5** | Cloud deploy & CI/CD | GitHub Actions, EC2, RDS | Track build/deploy status via Redis | Set short TTL to avoid stale data |

Each phase simulates real backend design decisions: queueing, deduplication, session state, logging separation, etc.
