# 🚀 User Management API

A production-ready REST API for user management built with **Java + Spring Boot**, focusing on clean architecture, security, and real-world backend practices.

---

## 📌 Overview

This project simulates a real backend system where users can be managed securely with proper authentication, authorization, validation, and pagination.

It was designed to demonstrate backend fundamentals expected from a junior developer in a professional environment.

---

## ✨ Key Features

* 🔐 User registration with validation
* 🔑 Authentication with encrypted passwords (BCrypt)
* 🛡️ Role-based authorization (USER / ADMIN)
* 👤 Get current authenticated user
* 📄 Paginated user listing (ADMIN only)
* ✏️ Update user with business rules
* ❌ Delete users with authorization checks
* ⚠️ Global exception handling with standardized responses
* 🧠 Clean separation of concerns (Controller / Service / Repository)

---

## 🧠 Business Rules Implemented

* Users can only update their own profile
* Admins can manage regular users
* Admin accounts cannot be modified by others
* Duplicate emails are not allowed
* Password must meet complexity requirements

---

## 🛠️ Tech Stack

* **Java 25**
* **Spring Boot**
* **Spring Security**
* **Spring Data JPA**
* **PostgreSQL**
* **Maven**

---

## 📂 Architecture

Feature-based structure focused on scalability:

```id="9ldf2k"
user/
 ├── controller      # HTTP layer
 ├── service         # business logic
 ├── repository      # data access
 ├── dtos            # request/response models
 ├── exceptions      # custom exceptions
 ├── validator       # input validation logic
```

---

## 🔐 Security

* Password hashing with BCrypt
* Endpoint protection via Spring Security
* Role-based access control
* Centralized exception handling to prevent sensitive data leaks

---

## 📡 API Endpoints

### 🔑 Authentication

* `POST /api/auth/register` → Register new user
* `POST /api/auth/login` → Authenticate user

---

### 👤 Users

* `GET /api/users/me` → Get current user
* `GET /api/users?page=0&size=10` → List users (**ADMIN only**)
* `PATCH /api/users/{id}` → Update user (with rules)
* `DELETE /api/users/{id}` → Delete user (**ADMIN only**)

---

## 📄 Pagination Example

```json id="c3x1bk"
{
  "content": [...],
  "page": 0,
  "pageSize": 10,
  "numberOfElements": 10,
  "totalElements": 100,
  "totalPages": 10
}
```

---

## ⚠️ Error Response Example

```json id="9y0twb"
{
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied",
  "path": "/api/users",
  "timestamp": "2026-04-15T12:00:00"
}
```

---

## ▶️ Running the Project

### 1. Clone the repository

```id="7cfdj7"
git clone https://github.com/D-michel-dev/user-management-api.git
```

### 2. Configure environment variables

Set your database credentials:

```id="9zq3e1"
SPRING_DATASOURCE_URL=...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
```

### 3. Run the application

```id="o5u0bb"
./mvnw spring-boot:run
```

---

## 🧪 Future Improvements

* JWT authentication
* Docker containerization
* API documentation (Swagger / OpenAPI)
* Unit and integration tests

---

## 👨‍💻 About This Project

This project focuses on demonstrating:

* Clean backend architecture
* Real-world API design
* Security best practices
* Maintainable and scalable code

---

## 👤 Author

Douglas Michel
https://www.linkedin.com/in/douglas-michel-dev/
https://github.com/D-Michel-dev/

---
