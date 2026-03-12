# 🎟️ Event Ticket Platform

A **full‑stack Event Ticket Management Platform** built with **Spring
Boot, React, TypeScript, PostgreSQL, and Keycloak**.\
The platform allows users to browse events, book tickets, and manage
their bookings through a secure authentication system.

This project demonstrates **modern full‑stack development**, **secure
authentication using OAuth2 / OpenID Connect**, **containerized services
with Docker**, and **scalable backend architecture**.

It was built as a portfolio project to demonstrate skills in:

-   Full‑stack development
-   Secure authentication and authorization
-   REST API design
-   Containerization with Docker
-   Modern frontend development

------------------------------------------------------------------------

# 🚀 Key Features

## 👤 User Features

-   Browse available events
-   View event details
-   Purchase or reserve tickets
-   QR-based ticket verification
-   Secure login and authentication via Keycloak

## 🎫 Event Management

-   Create and manage events
-   Manage ticket inventory
-   View bookings and ticket allocations

## 🔐 Secure Authentication

-   OAuth2 / OpenID Connect authentication
-   Identity and Access Management with **Keycloak**
-   JWT token-based authentication
-   Protected backend APIs using **Spring Security**

## 📱 Modern UI

-   Responsive interface
-   Built with **React + TypeScript**
-   Styled using **TailwindCSS**
-   Accessible UI components using **Radix UI**

------------------------------------------------------------------------

# 🏗️ System Architecture

    Frontend (React + Vite + TypeScript)
            │
            │ REST API (HTTPS)
            ▼
    Backend (Spring Boot)
            │
            │ JPA / Hibernate
            ▼
    PostgreSQL Database

    Authentication:
    React → Keycloak → JWT → Spring Security

------------------------------------------------------------------------

# 🛠️ Tech Stack

## Frontend

-   React 19
-   TypeScript
-   Vite
-   Tailwind CSS
-   React Router
-   Radix UI
-   JWT Decode
-   QR Scanner

## Backend

-   Java 21
-   Spring Boot
-   Spring Security
-   OAuth2 Resource Server
-   Spring Data JPA
-   MapStruct
-   Lombok
-   Maven

## Database

-   PostgreSQL

## Authentication & Identity

-   Keycloak
-   OpenID Connect
-   OAuth2

## DevOps & Infrastructure

-   Docker
-   Docker Compose
-   Adminer (Database UI)
-   JSON Server (Mock APIs)

------------------------------------------------------------------------

# 📂 Project Structure

    Event-Ticket-Platform
    │
    ├── Backend
    │   ├── src/main/java
    │   │   ├── controllers
    │   │   ├── services
    │   │   ├── repositories
    │   │   ├── models
    │   │   └── security
    │   │
    │   ├── src/main/resources
    │   │   └── application.yml
    │   │
    │   └── pom.xml
    │
    ├── Frontend
    │   ├── src
    │   │   ├── components
    │   │   ├── pages
    │   │   ├── services
    │   │   └── routes
    │   │
    │   ├── public
    │   ├── package.json
    │   └── vite.config.ts
    │
    ├── docker-compose.yml
    │
    └── README.md

------------------------------------------------------------------------

# 🐳 Running Services with Docker

The project uses **Docker Compose** to start required infrastructure
services:

-   PostgreSQL database
-   Keycloak identity server
-   Adminer database UI

## Start Docker Services

``` bash
docker compose up -d
```

This will start:

  Service      Purpose
  ------------ ---------------------------
  PostgreSQL   Main application database
  Keycloak     Authentication server
  Adminer      Database management UI

------------------------------------------------------------------------

# 🔑 Keycloak Authentication

The platform uses **Keycloak for authentication and authorization**.

Keycloak provides:

-   User management
-   Role-based access control
-   Secure OAuth2 authentication
-   JWT token generation

### Authentication Flow

    User Login
       │
       ▼
    React Frontend
       │
       ▼
    Keycloak Login Page
       │
       ▼
    JWT Token Issued
       │
       ▼
    Spring Boot Backend (Token Validation)

------------------------------------------------------------------------

# 🗄️ Database

The application uses **PostgreSQL** as the primary database.

Entities include:

-   Users
-   Events
-   Tickets
-   Bookings

The backend uses **Spring Data JPA** for ORM and database access.

------------------------------------------------------------------------

# ⚙️ Local Development Setup

## 1️⃣ Clone the Repository

    git clone https://github.com/YOUR_USERNAME/event-ticket-platform.git
    cd event-ticket-platform

------------------------------------------------------------------------

# ▶️ Run Backend

### Requirements

-   Java 21
-   Maven

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

This project demonstrates strong knowledge of:

-   Full‑stack application development
-   Secure authentication using OAuth2 and OpenID Connect
-   RESTful API design
-   Spring Boot backend architecture
-   React frontend development
-   Database integration with PostgreSQL
-   Containerized development using Docker
-   Identity and access management with Keycloak

------------------------------------------------------------------------

# 💡 Future Improvements

Possible improvements for future development:

-   Online payment integration
-   Email notifications for bookings
-   Admin dashboard for event organizers
-   Real-time ticket availability
-   Cloud deployment (AWS / Kubernetes)
-   CI/CD pipeline

------------------------------------------------------------------------

# 👨‍💻 Author

**Kumardeep Singh**

IT / Software Development Student\
Interested in:

-   Software Engineering
-   Cybersecurity
-   Full‑Stack Development
-   Cloud Technologies

GitHub:

https://github.com/YOUR_USERNAME

------------------------------------------------------------------------

# 📄 License

This project is licensed under the **MIT License**.

You are free to use and modify the project for learning purposes.
