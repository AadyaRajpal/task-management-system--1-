# API Usage Guide

This guide provides examples of how to use the Task Management System API.

## Base URL
```
http://localhost:8080
```

## 1. Authentication

### Register a New User

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "email": "alice@example.com",
    "password": "password123",
    "firstName": "Alice",
    "lastName": "Johnson"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "alice",
  "email": "alice@example.com",
  "roles": ["ROLE_USER"]
}
```

### Login

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "alice",
  "email": "alice@example.com",
  "roles": ["ROLE_USER"]
}
```

## 2. Projects

**Note:** All project endpoints require authentication. Include the JWT token in the Authorization header.

### Create a Project

**Request:**
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "E-Commerce Platform",
    "description": "Building a new e-commerce platform with microservices",
    "status": "ACTIVE",
    "memberIds": [2, 3],
    "startDate": "2024-02-01T00:00:00",
    "endDate": "2024-06-30T00:00:00"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "E-Commerce Platform",
  "description": "Building a new e-commerce platform with microservices",
  "status": "ACTIVE",
  "owner": {
    "id": 1,
    "username": "alice",
    "email": "alice@example.com"
  },
  "members": [...],
  "startDate": "2024-02-01T00:00:00",
  "endDate": "2024-06-30T00:00:00",
  "createdAt": "2024-02-11T10:30:00",
  "updatedAt": "2024-02-11T10:30:00"
}
```

### Get All Your Projects

**Request:**
```bash
curl -X GET http://localhost:8080/api/projects \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Project by ID

**Request:**
```bash
curl -X GET http://localhost:8080/api/projects/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update Project

**Request:**
```bash
curl -X PUT http://localhost:8080/api/projects/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "E-Commerce Platform Updated",
    "description": "Updated description",
    "status": "IN_PROGRESS",
    "memberIds": [2, 3, 4]
  }'
```

### Delete Project

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/projects/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 3. Tasks

### Create a Task

**Request:**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Implement User Authentication",
    "description": "Implement JWT-based authentication for the API",
    "projectId": 1,
    "assigneeId": 2,
    "status": "TODO",
    "priority": "HIGH",
    "dueDate": "2024-02-20T17:00:00"
  }'
```

**Response:**
```json
{
  "id": 1,
  "title": "Implement User Authentication",
  "description": "Implement JWT-based authentication for the API",
  "status": "TODO",
  "priority": "HIGH",
  "project": {...},
  "assignee": {...},
  "reporter": {...},
  "dueDate": "2024-02-20T17:00:00",
  "createdAt": "2024-02-11T10:35:00",
  "updatedAt": "2024-02-11T10:35:00"
}
```

### Get My Tasks

**Request:**
```bash
curl -X GET http://localhost:8080/api/tasks/my-tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Project Tasks

**Request:**
```bash
curl -X GET http://localhost:8080/api/tasks/project/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update Task

**Request:**
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Implement User Authentication",
    "description": "Updated description with more details",
    "projectId": 1,
    "assigneeId": 2,
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2024-02-20T17:00:00"
  }'
```

### Delete Task

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/tasks/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 4. Comments

### Add Comment to Task

**Request:**
```bash
curl -X POST http://localhost:8080/api/comments/task/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "content": "I have started working on this task. Will update by EOD."
  }'
```

**Response:**
```json
{
  "id": 1,
  "content": "I have started working on this task. Will update by EOD.",
  "task": {...},
  "author": {...},
  "createdAt": "2024-02-11T11:00:00",
  "updatedAt": "2024-02-11T11:00:00"
}
```

### Get Task Comments

**Request:**
```bash
curl -X GET http://localhost:8080/api/comments/task/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Delete Comment

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/comments/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 5. Notifications

### Get All Notifications

**Request:**
```bash
curl -X GET http://localhost:8080/api/notifications \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Unread Notifications

**Request:**
```bash
curl -X GET http://localhost:8080/api/notifications/unread \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get Unread Count

**Request:**
```bash
curl -X GET http://localhost:8080/api/notifications/unread/count \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response:**
```json
5
```

### Mark Notification as Read

**Request:**
```bash
curl -X PUT http://localhost:8080/api/notifications/1/read \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Mark All as Read

**Request:**
```bash
curl -X PUT http://localhost:8080/api/notifications/read-all \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 6. File Attachments

### Upload File to Task

**Request:**
```bash
curl -X POST http://localhost:8080/api/files/task/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/your/document.pdf"
```

**Response:**
```json
{
  "id": 1,
  "fileName": "document.pdf",
  "filePath": "uuid-generated-name.pdf",
  "fileType": "application/pdf",
  "fileSize": 524288,
  "task": {...},
  "uploadedBy": {...},
  "uploadedAt": "2024-02-11T12:00:00"
}
```

### Get Task Attachments

**Request:**
```bash
curl -X GET http://localhost:8080/api/files/task/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Download File

**Request:**
```bash
curl -X GET http://localhost:8080/api/files/download/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -o downloaded-file.pdf
```

### Delete Attachment

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/files/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 7. WebSocket Connection

### JavaScript Example

```javascript
// Using SockJS and STOMP
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to task updates for a specific project
    stompClient.subscribe('/topic/project/1/tasks', function(message) {
        const task = JSON.parse(message.body);
        console.log('New task:', task);
    });
    
    // Subscribe to personal notifications
    stompClient.subscribe('/queue/notifications', function(message) {
        const notification = JSON.parse(message.body);
        console.log('New notification:', notification);
    });
});
```

## Error Handling

All endpoints return appropriate HTTP status codes:

- `200 OK` - Success
- `201 Created` - Resource created successfully
- `204 No Content` - Success with no response body
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required or invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

**Error Response Format:**
```json
{
  "status": 400,
  "message": "Error description",
  "timestamp": "2024-02-11T10:30:00"
}
```

**Validation Error Format:**
```json
{
  "username": "Username is required",
  "email": "Email should be valid"
}
```
