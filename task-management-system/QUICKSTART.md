# Quick Start Guide

## Get Started in 5 Minutes

### 1. Run the Application

```bash
cd task-management-system
mvn spring-boot:run
```

### 2. Test with Swagger

Open: http://localhost:8080/swagger-ui.html

### 3. Create Your First User

**POST** `/api/auth/signup`
```json
{
  "username": "demo",
  "email": "demo@example.com",
  "password": "demo123",
  "firstName": "Demo",
  "lastName": "User"
}
```

### 4. Login

**POST** `/api/auth/login`
```json
{
  "username": "demo",
  "password": "demo123"
}
```

Copy the token from response.

### 5. Authorize in Swagger

Click "Authorize" button and enter: `Bearer <your-token>`

### 6. Create a Project

**POST** `/api/projects`
```json
{
  "name": "My First Project",
  "description": "Testing the application",
  "status": "ACTIVE"
}
```

### 7. Create a Task

**POST** `/api/tasks`
```json
{
  "title": "My First Task",
  "description": "Testing task creation",
  "projectId": 1,
  "status": "TODO",
  "priority": "HIGH"
}
```

## What's Included

✅ Complete Spring Boot application
✅ JWT Authentication
✅ REST API with Swagger docs
✅ WebSocket real-time notifications
✅ File upload/download
✅ Role-based access control
✅ H2 database (ready to use)
✅ Docker support
✅ Comprehensive tests
✅ Postman collection
✅ Full documentation

## Key Features

- **Projects**: Create and manage projects
- **Tasks**: Create, assign, and track tasks
- **Comments**: Collaborate on tasks
- **Notifications**: Real-time updates
- **Files**: Attach documents to tasks
- **Security**: JWT + Role-based access

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- Spring Security + JWT
- Spring Data JPA
- H2/PostgreSQL/MySQL
- WebSocket
- Swagger/OpenAPI
- Maven

## Project Structure

```
src/main/java/com/taskmanager/
├── config/          # Security, WebSocket config
├── controller/      # REST endpoints
├── dto/            # Request/Response objects
├── model/          # Database entities
├── repository/     # Data access layer
├── security/       # JWT, authentication
└── service/        # Business logic
```

## Important Endpoints

| Endpoint | Description |
|----------|-------------|
| `/api/auth/signup` | Register |
| `/api/auth/login` | Login |
| `/api/projects` | Manage projects |
| `/api/tasks` | Manage tasks |
| `/api/comments/task/{id}` | Add comments |
| `/api/notifications` | Get notifications |
| `/swagger-ui.html` | API documentation |
| `/h2-console` | Database console |

## Default Configuration

- Port: 8080
- Database: H2 in-memory
- JWT Secret: Pre-configured
- File uploads: `./uploads`
- Max file size: 10MB

## Need Help?

- See `README.md` for complete documentation
- Check `API_GUIDE.md` for API examples
- Read `SETUP.md` for deployment guides
- Import `postman_collection.json` for testing

## Common Commands

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Test
mvn test

# Package
mvn clean package

# Docker
docker build -t task-manager .
docker run -p 8080:8080 task-manager
```

## Next Steps

1. ✅ Run the application
2. ✅ Test with Swagger UI
3. ✅ Create projects and tasks
4. 📝 Add your own features
5. 🚀 Deploy to production
6. 💼 Add to your portfolio/resume

## Show Off Your Work

Perfect for:
- GitHub portfolio
- Job applications
- Internship interviews
- CS bachelor projects
- Learning Spring Boot

## Tips for Interviews

Highlight these aspects:
- ✅ Full-stack backend development
- ✅ RESTful API design
- ✅ Security (JWT, encryption)
- ✅ Real-time features (WebSocket)
- ✅ Database design (JPA, relationships)
- ✅ Testing (unit tests)
- ✅ Documentation (Swagger)
- ✅ Docker containerization
- ✅ Production-ready code

Good luck! 🚀
