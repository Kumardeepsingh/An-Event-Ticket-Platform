# 🎟️ Event Ticket Platform

A **full‑stack event ticketing platform** built with **Spring Boot 4 (Java
21), React 19 + TypeScript, PostgreSQL, and Keycloak**.

Organizers create events and ticket types, attendees browse and purchase
tickets, and staff check attendees in at the door by scanning a QR code or
entering a ticket ID — all protected by role-based access control backed by
a real OpenID Connect identity provider.

This isn't a CRUD tutorial clone — it's built around a few genuinely tricky
problems: preventing tickets from being oversold under concurrent purchases,
generating tamper-resistant QR codes that can't be reused after check-in,
and delegating authentication to an external identity provider instead of
rolling a custom login system.

------------------------------------------------------------------------

# 🚀 Key Features

## 👤 Attendee Features

-   Browse and full-text search published events (Postgres `tsvector` search
    over event name/venue)
-   View event details and available ticket types
-   Purchase tickets with real-time inventory checks (no overselling)
-   View purchased tickets and download the associated QR code

## 🎫 Organizer Features

-   Create, update, and delete events with nested ticket types in a single
    request
-   Manage ticket type pricing and inventory (`totalAvailable`)
-   Scoped strictly to events the organizer owns

## 🛂 Staff / Check-in Features

-   Validate tickets at the door via **QR scan** or **manual ticket ID**
    entry
-   Ticket validation is single-use — a ticket that's already been validated
    is rejected on re-scan

## 🔐 Secure, Delegated Authentication

-   Authentication and identity management fully delegated to **Keycloak**
    (OAuth2 / OpenID Connect) — the Spring app never issues or stores
    passwords
-   Spring Security acts as an **OAuth2 Resource Server**, validating JWTs
    issued by Keycloak
-   Role-based authorization (`ORGANIZER`, `STAFF`, `ATTENDEE`) driven by
    Keycloak realm roles
-   New users are provisioned just-in-time in the local database from JWT
    claims on first authenticated request

## 📱 Modern Frontend

-   React 19 + TypeScript SPA built with Vite
-   Styled with Tailwind CSS 4 and accessible Radix UI primitives
-   Camera-based QR scanning in the browser for staff check-in
-   OIDC login flow via `react-oidc-context`

------------------------------------------------------------------------

# 🏗️ System Architecture

    Frontend (React + Vite + TypeScript)
            │
            │ REST API (JWT bearer token)
            ▼
    Backend (Spring Boot — OAuth2 Resource Server)
            │
            ├── UserProvisioningFilter (JIT-creates local User from JWT claims)
            │
            ├── Controllers → Services → Repositories (Spring Data JPA)
            │
            ▼
    PostgreSQL Database

    Authentication:
    User → React (react-oidc-context) → Keycloak login → JWT issued
         → JWT sent as Bearer token → Spring Security validates against Keycloak issuer

Note: the Spring Boot backend never issues tokens itself — it only
**validates** JWTs that Keycloak already issued.

------------------------------------------------------------------------

# 🛠️ Tech Stack

## Backend

-   Java 21
-   Spring Boot 4.0.2 (modular starters: `webmvc`, `security`,
    `security-oauth2-resource-server`, `data-jpa`, `validation`,
    `h2console`)
-   Spring Security — OAuth2 Resource Server
-   Spring Data JPA / Hibernate (with JPA auditing for `createdAt`/`updatedAt`)
-   MapStruct — DTO ↔ entity mapping
-   Lombok
-   Google ZXing — QR code generation
-   Maven (with wrapper)

## Frontend

-   React 19 + TypeScript (~5.7)
-   Vite 6
-   Tailwind CSS 4
-   Radix UI (dialog, dropdown, select, popover, switch, avatar, etc.)
-   React Router 7
-   react-oidc-context + oidc-client-ts (Keycloak OIDC login)
-   jwt-decode
-   @yudiel/react-qr-scanner (camera-based QR scanning)
-   json-server (local mock API for frontend-only development)

## Database

-   PostgreSQL (runtime)
-   H2 (in-memory, test scope only)

## Authentication & Identity

-   Keycloak
-   OpenID Connect / OAuth2

## DevOps & Infrastructure

-   Docker — the backend and frontend each have their own multi-stage
    `Dockerfile` (Maven/JRE for the backend, Node/Nginx for the frontend)
-   Docker Compose — `docker-compose.prod.yml` runs the full stack (Postgres,
    Keycloak, backend, frontend) as containers; `Backend/docker-compose.yml`
    remains for running just the infrastructure alongside natively-run
    backend/frontend during day-to-day development
-   Keycloak realm-as-code — `keycloak/realm-export.json` is auto-imported on
    startup, so the realm, roles, OIDC client, and demo users are
    reproducible from git instead of manual admin-console clicking

------------------------------------------------------------------------

# 📡 API Overview

All endpoints are versioned under `/api/v1`.

| Method | Path | Purpose | Auth |
|---|---|---|---|
| POST | `/events` | Create an event with nested ticket types | `ROLE_ORGANIZER` |
| GET | `/events` | Paginated list of the organizer's own events | `ROLE_ORGANIZER` |
| GET | `/events/{eventId}` | Get an event owned by the caller | authenticated + ownership |
| PUT | `/events/{eventId}` | Update an event and diff/merge its ticket types | authenticated + ownership |
| DELETE | `/events/{eventId}` | Delete an event | authenticated + ownership |
| GET | `/published-events` | Public, paginated list of published events (`?q=` triggers full-text search) | public |
| GET | `/published-events/{eventId}` | Public detail of a published event | public |
| POST | `/events/{eventId}/ticket-types/{ticketTypeId}/tickets` | Purchase a ticket (pessimistic-locked inventory check) | authenticated |
| GET | `/tickets` | Paginated list of the caller's purchased tickets | authenticated |
| GET | `/tickets/{ticketId}` | Get a ticket owned by the caller | authenticated |
| GET | `/tickets/{ticketId}/qr-codes` | Returns the ticket's QR code as a PNG image | authenticated |
| POST | `/ticket-validations` | Validate a ticket by QR scan or manual ticket ID | `ROLE_STAFF` |

------------------------------------------------------------------------

# ⚙️ Notable Engineering Details

-   **Oversell protection under concurrency** — ticket purchases take a
    `PESSIMISTIC_WRITE` database lock on the `TicketType` row before
    counting existing tickets against `totalAvailable`, so two simultaneous
    purchase requests for the last ticket can't both succeed.
-   **Single-use QR validation** — each `QrCode` encodes a random UUID (not
    the ticket ID itself) into a PNG via ZXing. At check-in, a ticket that
    already has a valid validation record is rejected instead of silently
    re-validated, preventing re-entry with a photographed QR code.
-   **Full-text event search** — the public events endpoint uses native
    PostgreSQL `to_tsvector`/`plainto_tsquery` search over event name and
    venue rather than a naive `LIKE` query.
-   **JIT user provisioning** — a custom `OncePerRequestFilter` creates the
    local `User` row from JWT claims the first time a Keycloak-authenticated
    user hits the API, so there's no separate "register" endpoint or
    signup flow to keep in sync with Keycloak.
-   **Clean layering** — Controller → Service → Repository, with MapStruct
    generating DTO↔entity mappers so persistence entities are never
    serialized directly over the wire.
-   **Centralized error handling** — a `@RestControllerAdvice` maps every
    domain exception (not-found, sold-out, QR generation failure, etc.) and
    validation errors to consistent JSON error responses with appropriate
    HTTP status codes.

------------------------------------------------------------------------

# 📂 Project Structure

    An-Event-Ticket-Platform
    │
    ├── Backend
    │   ├── src/main/java/com/kumar/tickets
    │   │   ├── controllers        # EventController, TicketController, TicketValidationController, ...
    │   │   ├── services           # service interfaces
    │   │   ├── services/impl      # service implementations
    │   │   ├── repositories       # Spring Data JPA repositories
    │   │   ├── domain/enities     # JPA entities (Event, Ticket, TicketType, User, QrCode, TicketValidation) + enums
    │   │   ├── domain/dtos        # request/response DTOs
    │   │   ├── mappers            # MapStruct mappers
    │   │   ├── config             # SecurityConfig, JwtAuthenticationConverter, JpaConfiguration, QrCodeConfig
    │   │   ├── filters            # UserProvisioningFilter (JIT user creation)
    │   │   ├── util                # JwtUtil
    │   │   └── exceptions         # domain exception hierarchy
    │   │
    │   ├── src/main/resources
    │   │   └── application.properties
    │   │
    │   ├── docker-compose.yml     # Postgres, Keycloak, Adminer (infra-only, for native dev)
    │   ├── Dockerfile             # multi-stage: Maven build -> JRE runtime
    │   └── pom.xml
    │
    ├── Frontend
    │   ├── src
    │   │   ├── components
    │   │   ├── pages
    │   │   ├── services
    │   │   └── routes
    │   ├── public
    │   ├── Dockerfile             # multi-stage: Node build -> Nginx runtime
    │   ├── nginx.conf             # SPA fallback + /api proxy to the backend container
    │   ├── package.json
    │   └── vite.config.ts
    │
    ├── keycloak
    │   └── realm-export.json      # realm/roles/client/demo users, auto-imported on startup
    │
    ├── docker-compose.prod.yml    # full stack: db, keycloak, backend, frontend
    ├── .env.example               # template for docker-compose.prod.yml secrets/config
    ├── LICENSE
    └── README.md

------------------------------------------------------------------------

# 🐳 Running with Docker

There are two ways to run this project with Docker, depending on what you're doing.

## Option A: Infrastructure only (for native backend/frontend development)

`Backend/docker-compose.yml` starts just the infrastructure the app depends
on. The Spring Boot app and frontend run natively against these services.

    cd Backend
    docker compose up -d

| Service | Purpose | Port |
|---|---|---|
| PostgreSQL | Application database | 5432 |
| Keycloak | Identity provider (OAuth2 / OIDC) | 9090 (admin: `admin`/`admin`) |
| Adminer | Database management UI | 8888 |

> The bundled `application.properties` points at local Postgres/Keycloak
> with development-only credentials — fine for running the project locally,
> not meant for production use.

## Option B: The full stack, containerized

`docker-compose.prod.yml` builds and runs the backend and frontend as
containers too, alongside Postgres and Keycloak — no local Java/Node/Maven
toolchain required.

    cp .env.example .env
    docker compose -f docker-compose.prod.yml up -d --build

| Service | Purpose | Port |
|---|---|---|
| frontend | React SPA served by Nginx, proxies `/api/*` to the backend container | 8081 |
| backend | Spring Boot API | 8080 |
| keycloak | Identity provider — auto-imports `keycloak/realm-export.json` on first boot | 9090 |
| db / keycloak-db | Postgres for the app / for Keycloak's own state | (internal only) |

The realm import creates three demo accounts (password `changeme123` for
all): `organizer`, `staff`, and `attendee` — log in as any of them at
`http://localhost:8081` to exercise the corresponding role's flows without
touching the Keycloak admin console.

------------------------------------------------------------------------

# 🔑 Keycloak Authentication

Keycloak is the system of record for identity. It issues JWTs on login and
Spring Security validates them on every request — the Spring app itself has
no login endpoint or password storage.

    User → React Frontend → Keycloak Login Page → JWT Issued
         → Bearer token sent to Spring Boot API → Token validated against
           Keycloak's issuer + JWKS

Realm roles prefixed `ROLE_` (e.g. `ROLE_ORGANIZER`, `ROLE_STAFF`) are read
from the JWT's `realm_access.roles` claim and mapped to Spring Security
authorities by a custom `JwtAuthenticationConverter`.

The realm itself (`event-ticket-platform`) is defined as code in
`keycloak/realm-export.json` — roles, the `event-ticket-platform-app` OIDC
client, and demo users are all auto-imported the first time Keycloak starts
against an empty database when running the full stack via
`docker-compose.prod.yml` (see "Running with Docker" above).

------------------------------------------------------------------------

# 🗄️ Database

PostgreSQL is the primary datastore, accessed via Spring Data JPA.

| Entity | Description |
|---|---|
| `User` | A Keycloak-identified user (organizer, staff, and/or attendee) |
| `Event` | An event owned by an organizer, with a status (`DRAFT`/`PUBLISHED`/`CANCELLED`/`COMPLETED`) |
| `TicketType` | A priced ticket tier belonging to an event, with limited inventory |
| `Ticket` | A purchased ticket for a specific `TicketType` |
| `QrCode` | A generated QR code (PNG, Base64) tied to a ticket, used for check-in |
| `TicketValidation` | A record of a check-in attempt (QR scan or manual), with a status |

------------------------------------------------------------------------

# ⚙️ Local Development Setup

## 1️⃣ Clone the Repository

    git clone https://github.com/Kumardeepsingh/An-Event-Ticket-Platform.git
    cd An-Event-Ticket-Platform

## 2️⃣ Start Everything

The fastest path is the fully containerized stack (see "Running with
Docker" above), which also auto-configures the Keycloak realm for you:

    cp .env.example .env
    docker compose -f docker-compose.prod.yml up -d --build

If you'd rather run the backend/frontend natively for faster edit-reload
cycles, start just the infrastructure instead and skip ahead to "Run
Backend" / "Run Frontend" below:

    cd Backend
    docker compose up -d

With this path, you'll need to manually configure a Keycloak realm
(`event-ticket-platform`) with an `event-ticket-platform-app` client and
`ROLE_ORGANIZER`/`ROLE_STAFF` realm roles, matching `application.properties`
— or import `keycloak/realm-export.json` yourself via the admin console.

------------------------------------------------------------------------

# ▶️ Run Backend

### Requirements

-   Java 21
-   Maven (or use the bundled `./mvnw`)

### Start backend

    cd Backend
    ./mvnw spring-boot:run

Backend will run at:

    http://localhost:8080

------------------------------------------------------------------------

# ▶️ Run Frontend

### Requirements

-   Node.js (v18 or higher)

Install dependencies:

    cd Frontend
    npm install

Start development server:

    npm run dev

Frontend will run at:

    http://localhost:5173

------------------------------------------------------------------------

# 📈 Skills Demonstrated

-   Full‑stack application development (Spring Boot + React/TypeScript)
-   Delegated authentication and RBAC using OAuth2 / OpenID Connect and
    Keycloak
-   RESTful API design with pagination, validation, and centralized error
    handling
-   Concurrency-safe data access (pessimistic locking) for a real-world
    race condition
-   Clean layered backend architecture (Controller/Service/Repository +
    DTO/mapper separation via MapStruct)
-   Database design with Spring Data JPA, JPA auditing, and native
    PostgreSQL full-text search
-   Containerizing a multi-service application (backend, frontend, identity
    provider, two databases) with multi-stage Docker builds and Docker
    Compose
-   Identity/access management as code — a Keycloak realm (roles, OIDC
    client, users) defined in a version-controlled export and auto-imported
    on startup

------------------------------------------------------------------------

# 💡 Future Improvements

-   Expand automated test coverage (controller, service, and repository
    layers currently have no tests beyond a context-load smoke test)
-   CI/CD pipeline
-   Public deployment (a free-tier cloud VM behind HTTPS)
-   Online payment integration
-   Email notifications for ticket purchases
-   Admin dashboard for event organizers
-   Real-time ticket availability updates

------------------------------------------------------------------------

# 👨‍💻 Author

**Kumardeep Singh**

IT / Software Development Student\
Interested in:

-   Software Engineering
-   Cybersecurity
-   Full‑Stack Development
-   Cloud Technologies

GitHub: https://github.com/Kumardeepsingh

------------------------------------------------------------------------

# 📄 License

This project is licensed under the [MIT License](./LICENSE).
