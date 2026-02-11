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

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## 🚦 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd task-management-system
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access the API Documentation

Once the application is running, visit:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:taskdb`
  - Username: `sa`
  - Password: (leave empty)

## 📚 API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Projects

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/projects` | Create a new project |
| GET | `/api/projects` | Get all user's projects |
| GET | `/api/projects/{id}` | Get project by ID |
| PUT | `/api/projects/{id}` | Update project |
| DELETE | `/api/projects/{id}` | Delete project |

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tasks` | Create a new task |
| GET | `/api/tasks/my-tasks` | Get user's assigned tasks |
| GET | `/api/tasks/project/{projectId}` | Get all tasks for a project |
| GET | `/api/tasks/{id}` | Get task by ID |
| PUT | `/api/tasks/{id}` | Update task |
| DELETE | `/api/tasks/{id}` | Delete task |

### Comments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/comments/task/{taskId}` | Add comment to task |
| GET | `/api/comments/task/{taskId}` | Get task comments |
| DELETE | `/api/comments/{id}` | Delete comment |

### Notifications

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications` | Get all notifications |
| GET | `/api/notifications/unread` | Get unread notifications |
| GET | `/api/notifications/unread/count` | Get unread count |
| PUT | `/api/notifications/{id}/read` | Mark notification as read |
| PUT | `/api/notifications/read-all` | Mark all as read |

### File Attachments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/files/task/{taskId}` | Upload file to task |
| GET | `/api/files/task/{taskId}` | Get task attachments |
| GET | `/api/files/download/{attachmentId}` | Download file |
| DELETE | `/api/files/{id}` | Delete attachment |

## 🔒 Authentication Flow

1. **Sign Up**: POST to `/api/auth/signup` with user details
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

2. **Login**: POST to `/api/auth/login`
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

3. **Response**: You'll receive a JWT token
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

4. **Use Token**: Include the token in the Authorization header for all subsequent requests:
```
Authorization: Bearer <your-token>
```

## 🔌 WebSocket Connection

Connect to WebSocket endpoint for real-time updates:

**Endpoint**: `ws://localhost:8080/ws`

### Subscribe to Topics:

- Task updates: `/topic/project/{projectId}/tasks`
- Task status changes: `/topic/project/{projectId}/tasks/update`
- Task deletions: `/topic/tasks/delete`
- New comments: `/topic/task/{taskId}/comments`
- Personal notifications: `/queue/notifications`

## 🗄️ Database Configuration

The application uses H2 in-memory database by default. To switch to PostgreSQL or MySQL:

### PostgreSQL

1. Add PostgreSQL dependency to `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### MySQL

1. Add MySQL dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## 🐳 Docker Support

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
mvn clean package
docker build -t task-management-system .
docker run -p 8080:8080 task-management-system
```

## 🧪 Testing

Run tests:

```bash
mvn test
```

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

## 🤝 Contributing

This is a portfolio/educational project. Feel free to fork and modify for your own learning purposes.

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

**Your Name**
- GitHub: [@yourusername]
- LinkedIn: [Your LinkedIn]

## 🙏 Acknowledgments

- Spring Boot Documentation
- Baeldung Spring Security Tutorials
- Stack Overflow Community
