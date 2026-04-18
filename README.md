# 🏖️ Tourism Make Easy

A microservices-based resort booking platform built with a modern monorepo architecture.

---

## Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Monorepo** | [Nx](https://nx.dev) | 22.6.5 |
| **Package Manager** | [pnpm](https://pnpm.io) | 9.x |
| **Backend Services** | Java + Spring Boot | JDK 25, Spring Boot 3.4.5 |
| **Build Tool** | Maven (wrapper included) | 3.9.9 |
| **Database** | PostgreSQL | 17 (Alpine) |
| **Containerization** | Docker + Docker Compose | 29.x / 5.x |
| **Code Quality** | Prettier (Java + XML plugins), ESLint | — |
| **Libraries** | Lombok 1.18.42, MapStruct 1.6.3 | — |
| **Frontend (planned)** | Next.js (web), React Native (mobile) | — |
| **BFF (planned)** | NestJS | — |

---

## Project Structure

```
tourism-make-easy/
├── apps/                              # Frontend clients (future)
│   ├── web/                           #   Next.js web app
│   └── mobile/                        #   React Native mobile app
├── libs/                              # Shared TypeScript libraries (future)
├── services/                          # Backend microservices
│   ├── bff/                           #   NestJS orchestrator (future)
│   └── booking/                       #   Java/Maven multi-module core
│       ├── pom.xml                    #     Spring Boot parent POM
│       ├── common-lib/                #     Shared DTOs & utilities
│       ├── central-service/           #     Core routing (port 8080)
│       ├── inventory-service/         #     Room availability (port 8081)
│       ├── reservation-service/       #     Booking & payment (port 8082)
│       └── resort-service/            #     Resort details (port 8083)
├── docker/                            # Docker infrastructure
│   ├── docker-compose.yml             #   Full-stack dev environment
│   ├── Dockerfile.service             #   Multi-stage Java service image
│   ├── init-databases.sh              #   DB initialization script
│   ├── .env.dev                       #   Dev environment config
│   ├── .env.stage                     #   Staging config (gitignored)
│   └── .env.prod                      #   Production config (gitignored)
├── nx.json                            # Nx workspace configuration
├── pom.xml                            # Root Maven aggregator POM
├── package.json                       # Root Node.js dependencies
└── pnpm-workspace.yaml                # pnpm workspace config
```

---

## Prerequisites

Before you begin, ensure you have the following installed:

| Tool | Required Version | Installation |
|------|-----------------|--------------|
| **Git** | 2.x+ | [git-scm.com](https://git-scm.com) |
| **Java JDK** | 25+ | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/) |
| **Node.js** | 20+ | [nodejs.org](https://nodejs.org) |
| **pnpm** | 9.x | `npm install -g pnpm` |
| **Docker Desktop** | Latest | [docker.com](https://www.docker.com/products/docker-desktop/) |

> **Note:** Maven does NOT need to be installed. The project includes a Maven wrapper (`./mvnw`) that auto-downloads the correct version.

### Verify Installation

```bash
java --version        # Should show 25.x
node --version        # Should show 20.x+
pnpm --version        # Should show 9.x
docker --version      # Should show 29.x+
docker compose version # Should show 5.x+
```

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd tourism-make-easy
```

### 2. Install Node.js Dependencies

```bash
pnpm install
```

### 3. Choose Your Development Mode

#### Option A: Docker (Recommended for Frontend Developers)

**Zero Java setup needed.** Starts PostgreSQL + all backend services in containers.

```bash
cd docker
docker compose --env-file .env.dev up -d --build
```

Wait ~2 minutes for first build, then all APIs are available:

| Service | URL |
|---------|-----|
| Central Service | http://localhost:8080 |
| Inventory Service | http://localhost:8081 |
| Reservation Service | http://localhost:8082 |
| Resort Service | http://localhost:8083 |

#### Option B: Local (Recommended for Backend Developers)

Start only PostgreSQL in Docker, run Java services locally for faster iterations:

```bash
# Start PostgreSQL
cd docker
docker compose --env-file .env.dev up postgres -d

# Go back to project root and build all modules
cd ..
./mvnw clean install -DskipTests

# Run a specific service
./mvnw spring-boot:run -pl services/booking/central-service
```

---

## Available API Endpoints

### Central Service (port 8080)

#### Resorts

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/resorts` | List all resorts |
| `GET` | `/api/resorts/{id}` | Get resort by ID |
| `GET` | `/api/resorts/search?location=kerala` | Search by location |
| `GET` | `/api/resorts/search?maxPrice=20000` | Filter by max price |

#### Bookings

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/bookings` | Create a booking |
| `GET` | `/api/bookings` | List all bookings |
| `GET` | `/api/bookings/{bookingId}` | Get booking by ID |
| `DELETE` | `/api/bookings/{bookingId}` | Cancel a booking |

#### Sample Booking Request

```json
POST http://localhost:8080/api/bookings
Content-Type: application/json

{
  "resortId": 1,
  "guestName": "John Doe",
  "guestEmail": "john@example.com",
  "checkIn": "2026-05-01",
  "checkOut": "2026-05-04",
  "guests": 2
}
```

#### Health Check (all services)

```
GET http://localhost:{port}/health
GET http://localhost:{port}/actuator/health
```

---

## Docker Commands

```bash
# Navigate to docker directory first
cd docker

# Start all services (dev environment)
docker compose --env-file .env.dev up -d --build

# Start only PostgreSQL (for local backend dev)
docker compose --env-file .env.dev up postgres -d

# View logs
docker compose logs -f                    # All services
docker compose logs -f central-service    # Specific service

# Stop all services
docker compose down

# Stop and wipe all data (fresh start)
docker compose down -v

# Rebuild a specific service after code changes
docker compose --env-file .env.dev up -d --build central-service

# Switch environment
docker compose --env-file .env.stage up -d --build
```

---

## Development Guide

### Building the Project

```bash
# Build all modules
./mvnw clean install

# Build without tests (faster)
./mvnw clean install -DskipTests

# Build a specific service + its dependencies
./mvnw clean install -DskipTests -pl services/booking/central-service -am

# Run tests for a specific service
./mvnw test -pl services/booking/central-service
```

### Using Nx

```bash
# Build a specific project via Nx
npx nx build central-service

# Run tests via Nx
npx nx test central-service

# Start a service via Nx
npx nx serve central-service

# View the dependency graph
npx nx graph
```

---

## Adding New Components

### Adding a New Java Microservice

1. **Create the module directory:**

```bash
mkdir -p services/booking/my-new-service/src/main/java/com/tourism/service/booking/mynew
mkdir -p services/booking/my-new-service/src/main/resources
mkdir -p services/booking/my-new-service/src/test/java/com/tourism/service/booking/mynew
```

2. **Create `pom.xml`** (use an existing service as template):

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.tourism.service.booking</groupId>
    <artifactId>booking-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <relativePath>../pom.xml</relativePath>
  </parent>

  <artifactId>my-new-service</artifactId>
  <name>my-new-service</name>
  <description>Description of your service</description>

  <dependencies>
    <dependency>
      <groupId>com.tourism.service.booking</groupId>
      <artifactId>common-lib</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

3. **Register the module** in `services/booking/pom.xml`:

```xml
<modules>
  <!-- existing modules -->
  <module>my-new-service</module>
</modules>
```

4. **Create `project.json`** for Nx integration:

```json
{
  "name": "my-new-service",
  "$schema": "../../../node_modules/nx/schemas/project-schema.json",
  "projectType": "application",
  "sourceRoot": "./services/booking/my-new-service/src",
  "targets": {
    "build": {
      "executor": "@jnxplus/nx-maven:run-task",
      "outputs": ["{projectRoot}/target", "{options.outputDirLocalRepo}"],
      "options": { "task": "install -DskipTests=true" }
    },
    "test": {
      "executor": "@jnxplus/nx-maven:run-task",
      "options": { "task": "test" },
      "dependsOn": ["^build"]
    },
    "serve": {
      "executor": "@jnxplus/nx-maven:run-task",
      "options": { "task": "spring-boot:run" },
      "dependsOn": ["^build"]
    }
  },
  "tags": ["scope:booking", "type:service"],
  "implicitDependencies": ["common-lib"]
}
```

5. **Create the Spring Boot application class**, `application.yml`, and health controller (copy from an existing service).

6. **Add to Docker Compose** — add a new service block in `docker/docker-compose.yml` and a new database in `docker/init-databases.sh`.

7. **Add environment variables** — update all `.env.*` files with the new service port and database name.

### Adding a New Frontend App

1. **Create the app** inside `apps/`:

```bash
# Next.js web app
cd apps
npx -y create-next-app@latest web

# React Native mobile app
cd apps
npx -y react-native@latest init mobile
```

2. The app is automatically picked up by pnpm workspace (`apps/**` pattern in `pnpm-workspace.yaml`).

---

## Environment Configuration

All configurable values are externalized into environment files in the `docker/` directory:

| File | Committed | DDL Mode | Purpose |
|------|-----------|----------|---------|
| `.env.dev` | ✅ Yes | `update` | Local development — auto-creates/updates tables |
| `.env.stage` | ❌ Gitignored | `validate` | Staging — validates schema against entities |
| `.env.prod` | ❌ Gitignored | `none` | Production — schema managed by migrations only |

### Environment Variables Reference

| Variable | Description | Dev Default |
|----------|-------------|-------------|
| `POSTGRES_USER` | Database username | `tme_admin` |
| `POSTGRES_PASSWORD` | Database password | `tme_secret` |
| `POSTGRES_HOST` | Database hostname | `tme-postgres` |
| `POSTGRES_PORT` | Database port | `5432` |
| `CENTRAL_DB` | Central service database | `central_db` |
| `INVENTORY_DB` | Inventory service database | `inventory_db` |
| `RESERVATION_DB` | Reservation service database | `reservation_db` |
| `RESORT_DB` | Resort service database | `resort_db` |
| `*_SERVICE_PORT` | Service ports | `8080`–`8083` |
| `JPA_DDL_AUTO` | Hibernate DDL strategy | `update` |
| `JPA_SHOW_SQL` | Log SQL queries | `false` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `docker` |

---

## Database

Each microservice has its own isolated PostgreSQL database:

| Service | Database | Port |
|---------|----------|------|
| central-service | `central_db` | 8080 |
| inventory-service | `inventory_db` | 8081 |
| reservation-service | `reservation_db` | 8082 |
| resort-service | `resort_db` | 8083 |

### Connect to PostgreSQL Directly

```bash
# Via Docker
docker exec -it tme-postgres psql -U tme_admin -d central_db

# Via local client
psql -h localhost -p 5432 -U tme_admin -d central_db
# Password: tme_secret
```

---

## Code Style & Formatting

The project uses **Prettier** for consistent formatting across Java, XML, and JSON files.

```bash
# Format all files
npx prettier --write .

# Check formatting
npx prettier --check .
```

Configuration: [`.prettierrc`](.prettierrc) — plugins for Java and XML formatting.

Editor config: [`.editorconfig`](.editorconfig) — 2-space indent, UTF-8, trailing whitespace trimming.

---

## Architecture Overview

```
┌─────────────┐     ┌─────────────┐
│   Next.js   │     │React Native │    Frontend Clients
│   (Web)     │     │  (Mobile)   │    (Future — Phase 5)
└──────┬──────┘     └──────┬──────┘
       │                   │
       └────────┬──────────┘
                │
        ┌───────▼───────┐
        │   NestJS BFF  │              Backend for Frontend
        │  (Orchestrator)│              (Future — Phase 4)
        └───────┬───────┘
                │
    ┌───────────┼───────────────┐
    │           │               │
┌───▼───┐  ┌───▼────┐  ┌───────▼───────┐
│Central│  │Resort  │  │ Reservation   │   Java Microservices
│Service│  │Service │  │   Service     │   (Spring Boot 3.4.5)
└───┬───┘  └───┬────┘  └───────┬───────┘
    │          │               │
    │    ┌─────▼─────┐         │
    │    │ Inventory  │         │
    │    │  Service   │         │
    │    └─────┬─────┘         │
    │          │               │
    └──────────┼───────────────┘
               │
        ┌──────▼──────┐
        │ PostgreSQL  │                Database
        │   (Docker)  │
        └─────────────┘
```

---

## Troubleshooting

### Port already in use

```bash
# Find and kill the process using the port
lsof -i :8080 | grep LISTEN
kill -9 <PID>
```

### Docker build fails with credential error

```bash
# Run with explicit PATH
export PATH="/usr/local/bin:$PATH"
cd docker && docker compose --env-file .env.dev up -d --build
```

### Maven build fails

```bash
# Clean everything and rebuild
./mvnw clean install -DskipTests -U
```

### Reset everything (nuclear option)

```bash
# Stop all containers, remove volumes, and rebuild
cd docker
docker compose down -v
docker compose --env-file .env.dev up -d --build
```

---

## Contributing

1. Create a feature branch from `dev`: `git checkout -b feature/your-feature`
2. Make your changes
3. Run `./mvnw clean install` to verify the build
4. Run `npx prettier --write .` to format code
5. Commit with a meaningful message following [Conventional Commits](https://www.conventionalcommits.org/):
   - `feat:` — New feature
   - `fix:` — Bug fix
   - `chore:` — Maintenance
   - `docs:` — Documentation
6. Push and open a Pull Request to `dev`

---

## License

MIT