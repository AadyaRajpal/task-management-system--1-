# Task Management System

A complete, production-ready Task Management System built with Java and Spring Boot. Features include JWT authentication, role-based access control, real-time notifications via WebSocket, file attachments, and a comprehensive REST API.

## 🚀 Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (USER, ADMIN, MANAGER)
  - Secure password encryption with BCrypt

- **Project Management**
  - Create, update, and delete projects
  - Assign team members to projects
  - Track project status (Active, Completed, On Hold, Cancelled)

- **Task Management**
  - Create and assign tasks within projects
  - Set task priority (Low, Medium, High, Critical)
  - Track task status (To Do, In Progress, In Review, Done)
  - Set due dates and track completion

- **Real-time Notifications**
  - WebSocket-based real-time updates
  - Task assignments, updates, and comments
  - User mentions and notifications

- **Comments & Collaboration**
  - Add comments to tasks
  - Real-time comment notifications
  - Comment history tracking

- **File Attachments**
  - Upload files to tasks
  - Download attachments
  - File metadata tracking

- **API Documentation**
  - Swagger/OpenAPI documentation
  - Interactive API testing interface

## 🛠️ Technology Stack

- **Backend Framework:** Spring Boot 3.2.0
- **Security:** Spring Security with JWT
- **Database:** H2 (easily switchable to PostgreSQL/MySQL)
- **Real-time Communication:** WebSocket with STOMP
- **API Documentation:** SpringDoc OpenAPI
- **Build Tool:** Maven
- **Java Version:** 17

## 📝 Project Structure

```
task-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/taskmanager/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Exception handling
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── security/        # Security configuration
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Test classes
├── pom.xml
└── README.md
```

## 🔐 Security Features

- Password encryption using BCrypt
- JWT token-based authentication
- Role-based access control
- CORS configuration for frontend integration
- Stateless session management

## 🎯 Use Cases

This project demonstrates:

1. **Full-stack Spring Boot development**
2. **RESTful API design**
3. **Database relationships (One-to-Many, Many-to-Many)**
4. **Security implementation**
5. **Real-time communication with WebSockets**
6. **File upload/download handling**
7. **Exception handling**
8. **API documentation**

