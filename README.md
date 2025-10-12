# Dummy Project (Angular + Spring Boot + Oracle + Observability)

A minimal full‑stack template:

- **Frontend:** Angular (served by Nginx in Docker)
- **Backend:** Spring Boot (Java 21, JPA, Liquibase)
- **DB:** Oracle XE (Docker)
- **Observability (dev):** Grafana + Loki + Promtail (container logs in a nice UI)

> Built to run with a single `docker compose up -d --build`.
>
> **OS:** Designed/tested on Linux (Docker Engine). It should work on Windows WSL2. macOS users may need a different Promtail setup because Docker Desktop stores logs differently.

---

## Prerequisites

- Docker Engine + Docker Compose plugin
- Git
- (Optional) DBeaver for DB browsing

Verify:
```bash
docker --version
docker compose version
git --version
```

---

## 1) Clone the repository

```bash
git clone https://github.com/sargis-sulyan/dummy-project.git
cd dummy-project
```

---

## 2) Create your `.env` file (do not commit)

This holds your local secrets and DB connection info. Use the example below as a starting point.

Create **`.env`** in the repo root:

```dotenv
# Oracle bootstrap (only used on first DB init)
ORACLE_PASSWORD=Oracle123
APP_USER=APP_USER
APP_USER_PASSWORD=AppUser123

# Spring Datasource (used by the backend)
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle:1521/XEPDB1
SPRING_DATASOURCE_USERNAME=APP_USER
SPRING_DATASOURCE_PASSWORD=AppUser123
```

> Keep `.env` out of git. The repo’s `.gitignore` is already set accordingly.

---

## 3) Start the whole stack

```bash
docker compose up -d --build
```

Services:
- **Frontend (Angular+Nginx):** http://localhost
- **Backend (Spring Boot):** http://localhost:8080
- **Backend API:** http://localhost:8080/api/users
- **Grafana:** http://localhost:3000 (user: `admin`, pass: `admin`)
- **Oracle XE:** exposed on `localhost:1521` (service name `XEPDB1`)

> First run will initialize Oracle (can take ~1–3 minutes). Backend waits for Oracle to become healthy and runs Liquibase migrations to create the schema.

---

## 4) First run checklist

### A) Verify containers
```bash
docker ps
```

You should see containers for `oracle`, `backend`, `frontend`, `loki`, `grafana`, `promtail`.

### B) Test API
```bash
# List users
curl http://localhost:8080/api/users

# Add a user
curl -X POST http://localhost:8080/api/users   -H 'Content-Type: application/json'   -d '{"name":"Alice","email":"alice@example.com"}'
```

### C) Open the UI
Visit http://localhost and check the Users page. Add a new user and confirm it appears.

---

## 5) Database access in DBeaver (Optional)

1. New Connection → **Oracle** (thin driver)
2. **Host:** `localhost`
3. **Port:** `1521`
4. **Service name:** `XEPDB1`
5. **Username:** `APP_USER`
6. **Password:** (value from `.env`, e.g. `AppUser123`)

Open the connection → expand `APP_USER` → see **Tables**, **Sequences**. You should find `USERS` and `USERS_SEQ` created by Liquibase.

> Data persists: the Oracle data files are stored in a **named Docker volume** (`oracle_data`). Do **not** run `docker compose down -v` unless you want to delete the database volume.

---

## 6) Observability (Grafana + Loki + Promtail)

- Promtail scrapes Docker container logs and pushes to Loki.
- In Grafana (http://localhost:3000, `admin/admin`), add a **Loki** datasource:
    - **URL:** `http://loki:3100`
    - Save & Test → should be green.
- Go to **Explore** → choose **Loki**. Start with a broad query:
  ```
  {container=~".*"}
  ```
  Narrow down to the backend container (often contains `backend` in the name).

### Backend app logs
The backend uses Logback with JSON output (console). Actions like **creating a user** are logged at `INFO` level. If you don’t see logs:
- Ensure time range in Grafana includes the last few minutes.
- Check backend logs directly:
  ```bash
  docker compose logs -f backend
  ```

---

## 7) Project structure (short)

```
dummy-project/
├─ backend/                # Spring Boot app (Java 21)
│  ├─ src/main/java/...
│  ├─ src/main/resources/
│  │  ├─ application.yml
│  │  ├─ db/changelog/db.changelog-master.yaml
│  │  └─ logback-spring.xml
│  ├─ build.gradle
│  └─ Dockerfile
├─ frontend/               # Angular app (built and served by Nginx)
│  ├─ src/...
│  ├─ angular.json
│  ├─ package.json
│  ├─ nginx.conf
│  └─ Dockerfile
├─ promtail-config.yml     # Promtail config for Docker log scraping
├─ docker-compose.yml      # Orchestration (DB + backend + frontend + logs stack)
└─ .env                    # Local-only secrets (not committed)
```

---

## 8) Common commands

```bash
# View logs
docker compose logs -f backend
docker compose logs -f oracle
docker compose logs -f frontend
docker compose logs -f promtail
docker compose logs -f loki
docker compose logs -f grafana

# Restart a single service
docker compose restart backend

# Rebuild after code changes
docker compose build backend && docker compose up -d

# Stop stack (keeps volumes)
docker compose down

# Stop and REMOVE volumes (wipes Oracle data!)
docker compose down -v
```

---

## 9) Troubleshooting

- **Backend says “cannot connect to database” on first boot**  
  Oracle may still be initializing. Wait until `oracle` is **healthy**:
  ```bash
  docker compose logs -f oracle
  ```
  Then `docker compose restart backend`.

- **I don’t see logs in Grafana**
    - Add Loki datasource (`http://loki:3100`).
    - Use a wide time range or **Live** view.
    - Query `{container=~".*"}` and filter by `container` label.

- **Liquibase keeps re-running or fails**  
  Ensure `spring.jpa.hibernate.ddl-auto=none` (it is). Let Liquibase own the schema.

- **Want a clean DB reset**
  ```bash
  docker compose down -v
  docker compose up -d --build
  ```

---

## 10) Security notes

- Secrets belong in `.env` (local) or an external secret manager for real deployments.
- Do **not** commit `.env`.
- For production, consider removing/promoting the log stack and enforcing dedicated observability infrastructure.

---

## License

MIT (or choose your license)