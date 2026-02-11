# Setup and Deployment Guide

This guide will help you set up, run, and deploy the Task Management System.

## Table of Contents
1. [Local Development Setup](#local-development-setup)
2. [Running the Application](#running-the-application)
3. [Testing the API](#testing-the-api)
4. [Database Configuration](#database-configuration)
5. [Docker Deployment](#docker-deployment)
6. [Production Deployment](#production-deployment)

---

## Local Development Setup

### Prerequisites

Make sure you have the following installed:

- **Java 17 or higher**
  - Check version: `java -version`
  - Download: https://adoptium.net/

- **Maven 3.6 or higher**
  - Check version: `mvn -version`
  - Download: https://maven.apache.org/download.cgi

### Step 1: Clone the Repository

```bash
git clone <your-repository-url>
cd task-management-system
```

### Step 2: Install Dependencies

```bash
mvn clean install
```

This will download all required dependencies and build the project.

---

## Running the Application

### Option 1: Using Maven

```bash
mvn spring-boot:run
```

### Option 2: Using JAR file

```bash
# Build the JAR
mvn clean package

# Run the JAR
java -jar target/task-management-system-1.0.0.jar
```

### Verify the Application is Running

The application will start on port 8080. You should see output similar to:

```
Started TaskManagementSystemApplication in 5.123 seconds
```

Access the application:
- Application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

---

## Testing the API

### Using Swagger UI (Recommended for Beginners)

1. Open http://localhost:8080/swagger-ui.html
2. First, register a user using the `/api/auth/signup` endpoint
3. Login using `/api/auth/login` to get a JWT token
4. Click "Authorize" button and enter: `Bearer YOUR_TOKEN_HERE`
5. Now you can test all endpoints interactively

### Using Postman

1. Import the `postman_collection.json` file into Postman
2. The collection includes all API endpoints
3. Start with the Authentication folder
4. The Login request will automatically save the token for other requests

### Using cURL

See the `API_GUIDE.md` file for detailed cURL examples.

---

## Database Configuration

### Default: H2 In-Memory Database

The application uses H2 by default. No setup required!

**Access H2 Console:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: (leave empty)

### Switching to PostgreSQL

1. **Install PostgreSQL**
   - Download: https://www.postgresql.org/download/

2. **Create Database**
```sql
CREATE DATABASE taskdb;
CREATE USER taskuser WITH PASSWORD 'taskpass';
GRANT ALL PRIVILEGES ON DATABASE taskdb TO taskuser;
```

3. **Update pom.xml**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

4. **Update application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=taskuser
spring.datasource.password=taskpass
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

5. **Rebuild and Run**
```bash
mvn clean package
mvn spring-boot:run
```

### Switching to MySQL

1. **Install MySQL**
   - Download: https://dev.mysql.com/downloads/

2. **Create Database**
```sql
CREATE DATABASE taskdb;
CREATE USER 'taskuser'@'localhost' IDENTIFIED BY 'taskpass';
GRANT ALL PRIVILEGES ON taskdb.* TO 'taskuser'@'localhost';
FLUSH PRIVILEGES;
```

3. **Update pom.xml**
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

4. **Update application.properties**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
spring.datasource.username=taskuser
spring.datasource.password=taskpass
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## Docker Deployment

### Build and Run with Docker

1. **Build the Application**
```bash
mvn clean package
```

2. **Build Docker Image**
```bash
docker build -t task-management-system .
```

3. **Run Container**
```bash
docker run -p 8080:8080 task-management-system
```

### Using Docker Compose

```bash
docker-compose up -d
```

This will start the application and optionally a PostgreSQL database.

To use PostgreSQL with Docker Compose:
1. Uncomment the postgres service in `docker-compose.yml`
2. Update `application.properties` for PostgreSQL
3. Run: `docker-compose up -d`

---

## Production Deployment

### Deploy to Heroku

1. **Install Heroku CLI**
   - Download: https://devcenter.heroku.com/articles/heroku-cli

2. **Login to Heroku**
```bash
heroku login
```

3. **Create Application**
```bash
heroku create your-app-name
```

4. **Add PostgreSQL**
```bash
heroku addons:create heroku-postgresql:hobby-dev
```

5. **Deploy**
```bash
git push heroku main
```

6. **Open Application**
```bash
heroku open
```

### Deploy to AWS Elastic Beanstalk

1. **Install EB CLI**
```bash
pip install awsebcli
```

2. **Initialize EB**
```bash
eb init -p java-17 task-management-system
```

3. **Create Environment**
```bash
eb create task-management-env
```

4. **Deploy**
```bash
mvn clean package
eb deploy
```

### Deploy to Digital Ocean

1. **Create Droplet** (Ubuntu 22.04)

2. **SSH into Droplet**
```bash
ssh root@your-droplet-ip
```

3. **Install Java**
```bash
apt update
apt install openjdk-17-jdk
```

4. **Upload JAR**
```bash
scp target/task-management-system-1.0.0.jar root@your-droplet-ip:/opt/
```

5. **Create Systemd Service**
```bash
sudo nano /etc/systemd/system/taskmanager.service
```

Add:
```
[Unit]
Description=Task Management System
After=syslog.target

[Service]
User=root
ExecStart=/usr/bin/java -jar /opt/task-management-system-1.0.0.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

6. **Start Service**
```bash
systemctl daemon-reload
systemctl enable taskmanager
systemctl start taskmanager
```

---

## Environment Variables for Production

Create `application-prod.properties`:

```properties
# Database
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

# Server
server.port=${PORT:8080}

# File Upload
file.upload-dir=${UPLOAD_DIR:/app/uploads}

# Logging
logging.level.com.taskmanager=INFO
```

Run with production profile:
```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## Troubleshooting

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

Or change the port in `application.properties`:
```properties
server.port=8081
```

### Database Connection Issues

- Check if database is running
- Verify credentials in `application.properties`
- Check firewall settings
- Ensure database driver is in classpath

### Out of Memory Error

Increase heap size:
```bash
java -Xmx512m -jar app.jar
```

---

## Monitoring and Logs

### View Logs

```bash
# Docker
docker logs <container-id>

# Systemd
journalctl -u taskmanager -f
```

### Enable Actuator (Optional)

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Access health endpoint: http://localhost:8080/actuator/health

---

## Next Steps

1. Set up CI/CD pipeline (GitHub Actions, Jenkins)
2. Configure monitoring (Prometheus, Grafana)
3. Set up logging aggregation (ELK Stack)
4. Implement rate limiting
5. Add caching with Redis
6. Set up backup strategies

---

## Support

For issues or questions:
- Check the main README.md
- Review API_GUIDE.md for API usage
- Open an issue on GitHub
