# User Management API

REST API for user management with authentication and authorization using JWT.

This project was built as part of my backend learning journey, focusing on clean architecture, security, and good API practices.

---

## 🧠 Features

- User registration with validation
- Authentication with JWT
- Role-based authorization (USER / ADMIN)
- Get current authenticated user
- Update user data with permission rules
- Delete users (admin only)
- Paginated user listing
- Global error handling
- Custom security error handling (401 / 403)
- API documentation with Swagger

---

## 🔐 Authentication

This API uses JWT for authentication.

After login, you'll receive a token that must be included in requests:


Authorization: Bearer YOUR_TOKEN


---

## 🛠️ Tech Stack

- Java 25
- Spring Boot
- Spring Security
- JWT (Auth0)
- Spring Data JPA
- PostgreSQL
- Swagger (OpenAPI)

---

## 📁 Project Structure

The project follows a **feature-based structure**, where each domain contains its own:

- controller
- service
- repository
- dtos
- exceptions

This keeps things organized and scalable as the project grows.

---

## 📌 Main Endpoints

### 🔓 Public

- `POST /api/users/register` → create account  
- `POST /api/users/login` → authenticate user  

### 🔒 Protected

- `GET /api/users/me` → current user  
- `GET /api/users` → list users (ADMIN only)  
- `PATCH /api/users/{id}` → update user  
- `DELETE /api/users/{id}` → delete user (ADMIN only)  

---

## ⚙️ Running the project

### 1. Clone the repository


git clone https://github.com/D-Michel-dev/user-management-api.git


### 2. Configure environment variables

You should not store secrets in the code. Configure:


DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=


---

### 3. Run the application


./mvnw spring-boot:run


---

## 📖 API Documentation

Swagger UI:


http://localhost:8080/swagger-ui.html


---

## 🧪 Notes

- Passwords are encrypted using BCrypt
- JWT is stateless (no session stored)
- Security is handled via filter + Spring Security context
- Errors follow a consistent JSON structure

---

## 📈 Future Improvements

- Unit and integration tests
- Refresh token mechanism
- Docker support
- Rate limiting

---

## 👨‍💻 About

This project was built to practice backend development and understand how a real-world API works, including authentication, authorization, validation, and error handling.

## 👤 Author

Douglas Michel
https://www.linkedin.com/in/douglas-michel-dev/
https://github.com/D-Michel-dev/
