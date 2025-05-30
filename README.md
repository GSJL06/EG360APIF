# EducaGestor360 API

Comprehensive Educational Management Platform API built with Spring Boot, MySQL, and JWT authentication.

## 🚀 Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Student Management**: Complete CRUD operations for student records and academic tracking
- **Teacher Management**: Teacher profiles, course assignments, and performance tracking
- **Course Management**: Course creation, scheduling, and enrollment management
- **Enrollment System**: Student course enrollment with status tracking
- **Grade Management**: Comprehensive grading system with analytics
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Security**: BCrypt password encryption and JWT token security
- **Audit Logging**: Complete audit trail for all operations

## 🛠 Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Database**: MySQL 8.0+
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Testing**: JUnit 5, Spring Boot Test

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd educagestor-api
```

### 2. Database Setup

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS educagestor_db;

-- Create user (Updated credentials)
CREATE USER IF NOT EXISTS 'educagestor_user1'@'localhost' IDENTIFIED BY 'educagestor_pass';

-- Grant privileges
GRANT ALL PRIVILEGES ON educagestor_db.* TO 'educagestor_user1'@'localhost';
FLUSH PRIVILEGES;
```

**✅ Verified Working Configuration:**

- Database: `educagestor_db`
- Username: `educagestor_user1`
- Password: `educagestor_pass`
- Host: `localhost:3306`

### 3. Configure Application

The application is pre-configured for MySQL. The configuration is in `src/main/resources/application-mysql.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/educagestor_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
    username: educagestor_user1
    password: educagestor_pass
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
```

### 4. Build and Run

#### Quick Start (Recommended):

```bash
# Build the application
mvn clean package -DskipTests

# Run with MySQL profile
java -jar target/educagestor-api-1.0.0.jar --spring.profiles.active=mysql
```

#### Alternative Methods:

**Using Maven directly:**

```bash
mvn clean install
mvn spring-boot:run -Dspring.profiles.active=mysql
```

**Using Docker:**

```bash
# Build and run with Docker Compose
docker-compose up --build
```

**For Development:**

```bash
# Run in development mode with hot reload
mvn spring-boot:run -Dspring.profiles.active=mysql
```

The API will be available at `http://localhost:8080/api`

## 📚 API Documentation

✅ **Fully Functional Swagger Documentation**

Once the application is running, access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html ✅ **WORKING**
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs ✅ **WORKING**

**Features Available in Swagger UI:**

- 🔐 JWT Authentication support with Bearer token
- 📋 7 API categories: Authentication, User Management, Student Management, Teacher Management, Course Management, Grade Management, Enrollment Management
- 📖 Complete endpoint documentation with request/response schemas
- 🧪 Interactive testing interface
- 📊 Data validation and constraints documentation

### 🔐 Default Test Users

The application comes with pre-configured test users:

| Username   | Password   | Role    | Description              |
| ---------- | ---------- | ------- | ------------------------ |
| `admin`    | `admin123` | ADMIN   | Full system access       |
| `teacher1` | `admin123` | TEACHER | Computer Science teacher |
| `teacher2` | `admin123` | TEACHER | Mathematics teacher      |
| `student1` | `admin123` | STUDENT | Test student 1           |
| `student2` | `admin123` | STUDENT | Test student 2           |
| `student3` | `admin123` | STUDENT | Test student 3           |

## 🔐 Authentication

### User Roles

- **ADMIN**: Full system access, can manage all entities
- **TEACHER**: Can manage courses, students, and grades for assigned courses
- **STUDENT**: Limited access, can view own information and grades

### Authentication Flow

1. **Register/Login**: Use `/api/auth/login` or `/api/auth/register`
2. **Get JWT Token**: Receive access token and refresh token
3. **Use Token**: Include in Authorization header: `Bearer <token>`
4. **Refresh Token**: Use `/api/auth/refresh` when token expires

## 🌐 API Endpoints

### Authentication

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - User logout

### User Management

- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update current user profile
- `GET /api/users/{id}` - Get user by ID (Admin)
- `GET /api/users` - List all users (Admin)

### Student Management

- `POST /api/students` - Register student
- `GET /api/students` - List students (paginated)
- `GET /api/students/{id}` - Get student by ID
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student
- `GET /api/students/{id}/academic-history` - Get academic history

### Teacher Management

- `POST /api/teachers` - Register teacher
- `GET /api/teachers` - List teachers (paginated)
- `GET /api/teachers/{id}` - Get teacher by ID
- `PUT /api/teachers/{id}` - Update teacher
- `GET /api/teachers/{id}/courses` - Get teacher's courses

### Course Management

- `POST /api/courses` - Create course
- `GET /api/courses` - List courses
- `GET /api/courses/{id}` - Get course details
- `PUT /api/courses/{id}` - Update course
- `POST /api/courses/{id}/assign-teacher` - Assign teacher
- `GET /api/courses/{id}/students` - Get enrolled students

### Enrollment Management

- `POST /api/enrollments` - Enroll student in course
- `GET /api/enrollments/student/{studentId}` - Get student enrollments
- `DELETE /api/enrollments/{id}` - Cancel enrollment
- `GET /api/enrollments/course/{courseId}` - Get course enrollments

### Grade Management

- `POST /api/grades` - Record grade
- `GET /api/grades/student/{studentId}` - Get student grades
- `GET /api/grades/course/{courseId}` - Get course grades
- `PUT /api/grades/{id}` - Update grade

## 🧪 Testing

### Unit Tests

Run the test suite:

```bash
mvn test
```

### API Testing

Test the authentication endpoint:

```bash
# Login with admin user
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  http://localhost:8080/api/auth/login
```

### Quick API Verification

```bash
# Check if API is running
curl http://localhost:8080/api/swagger-ui.html

# Test authentication
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"testadmin","password":"admin123"}' \
  http://localhost:8080/api/auth/login
```

## 🧪 Comprehensive API Testing

### Automated Test Script

We've included a PowerShell script for comprehensive API testing:

```bash
# Run the comprehensive test script
powershell -ExecutionPolicy Bypass -File comprehensive-api-test.ps1
```

### Pre-loaded Test Data

The application comes with pre-loaded test data for immediate testing:

**Test Users:**

- **Admin:** `testadmin` / `admin123` (ADMIN role) ✅ Verified
- **Teachers:** `teacher1`, `teacher2` / `admin123` (TEACHER role)
- **Students:** `student1`, `student2`, `student3` / `admin123` (STUDENT role)

**Sample Data:**

- **8 Users** (1 admin + 1 testadmin + 2 teachers + 3 students + 1 testuser)
- **3 Students** with complete academic profiles
- **3 Courses** (CS101: Introduction to Programming, MATH201: Calculus I, CS201: Data Structures)
- **4 Enrollments** linking students to courses
- **24+ Grades** with various assignment types and scores

### Manual Testing with curl

**1. User Registration:**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "testuser@educagestor360.com",
    "password": "test123",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "+1234567890",
    "roles": ["STUDENT"]
  }'
```

**2. User Login:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testadmin",
    "password": "admin123"
  }'
```

**3. Test Protected Endpoints (replace TOKEN with JWT from login):**

```bash
# Get all users (Admin only)
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/users

# Get all students
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/students

# Get all courses
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/courses

# Get all teachers
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/teachers

# Get all enrollments
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/enrollments

# Get all grades
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/grades
```

### Postman Testing

📋 **Complete Postman Guide**: See [POSTMAN_TESTING_GUIDE.md](POSTMAN_TESTING_GUIDE.md) for detailed step-by-step instructions.

**Quick Setup:**

1. Set environment variables: `baseUrl` = `http://localhost:8080/api`
2. Start with authentication endpoints to get JWT token
3. Use token for all subsequent requests

### API Documentation

Access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html ✅ Working
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

### Endpoint Status Report

| Endpoint         | Method | Status     | Description                 |
| ---------------- | ------ | ---------- | --------------------------- |
| `/auth/register` | POST   | ✅ Working | User registration           |
| `/auth/login`    | POST   | ✅ Working | User authentication         |
| `/users`         | GET    | ✅ Working | List all users (Admin only) |
| `/students`      | GET    | ✅ Working | List all students           |
| `/students`      | POST   | ✅ Working | Register new student        |
| `/teachers`      | GET    | ✅ Working | List all teachers           |
| `/teachers`      | POST   | ✅ Working | Register new teacher        |
| `/courses`       | GET    | ✅ Working | List all courses            |
| `/courses`       | POST   | ✅ Working | Create new course           |
| `/enrollments`   | GET    | ✅ Working | List all enrollments        |
| `/enrollments`   | POST   | ✅ Working | Enroll student in course    |
| `/grades`        | GET    | ✅ Working | List all grades             |
| `/grades`        | POST   | ✅ Working | Record new grade            |

**All major endpoints are fully functional!** 🎉

## 📊 Database Schema

The application uses the following main entities:

- **Users**: Base user information and authentication
- **Students**: Student-specific data and academic status
- **Teachers**: Teacher profiles and employment information
- **Courses**: Course details, schedules, and capacity
- **Enrollments**: Student-course relationships
- **Grades**: Individual grade records and assessments

## 🔒 Security Features

- **Password Encryption**: BCrypt hashing for secure password storage
- **JWT Authentication**: Stateless token-based authentication
- **Role-Based Access**: Fine-grained permission control
- **CORS Configuration**: Configurable cross-origin resource sharing
- **Input Validation**: Comprehensive request validation
- **SQL Injection Protection**: JPA/Hibernate query protection

## 📝 Logging

The application includes comprehensive logging:

- **Authentication Events**: Login attempts, token generation
- **Business Operations**: CRUD operations, enrollment changes
- **Error Tracking**: Exception handling and error responses
- **Audit Trail**: User actions and system changes

## 🚀 Deployment

### Production Configuration

1. Update `application.yml` for production environment
2. Configure production database connection
3. Set secure JWT secret key
4. Enable HTTPS/SSL
5. Configure logging levels

### Docker Deployment

```dockerfile
# Dockerfile example
FROM openjdk:17-jdk-slim
COPY target/educagestor-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:

- Email: dev@educagestor360.com
- Documentation: [API Docs](http://localhost:8080/api/swagger-ui.html)
- Issues: [GitHub Issues](https://github.com/your-repo/issues)

## 🚨 Troubleshooting

### Common Issues

**Application won't start:**

- Ensure MySQL is running and accessible
- Verify database credentials in `application-mysql.yml`
- Check if port 8080 is available
- Run: `mvn clean package -DskipTests` to rebuild

**Database connection errors:**

- Verify MySQL user `educagestor_user1` exists with correct password
- Ensure database `educagestor_db` is created
- Check MySQL is running on port 3306
- Test connection: `mysql -u educagestor_user1 -p educagestor_db`

**Swagger UI not loading:**

- ✅ **RESOLVED**: Swagger UI is now fully functional
- Access at: http://localhost:8080/api/swagger-ui.html
- API docs at: http://localhost:8080/api/v3/api-docs
- If issues persist, verify application started successfully (look for "Started EducaGestorApiApplication")
- Ensure no firewall blocking port 8080
- Verify with: `netstat -ano | findstr :8080`

**API endpoints returning 500 errors:**

- ✅ **RESOLVED**: All major endpoints now working
- If issues persist, check application logs in `logs/educagestor-api.log`
- Restart application: `taskkill /F /IM java.exe` then restart

**Authentication issues:**

- Use correct test credentials: `testadmin` / `admin123`
- Ensure JWT token is included in Authorization header
- Token format: `Bearer <your-jwt-token>`
- Tokens expire after 24 hours - login again if needed

### Known Working Configuration

✅ **Verified Working Setup:**

- **OS**: Windows 10/11
- **Java**: OpenJDK 17
- **MySQL**: 8.0+
- **Maven**: 3.6+
- **Port**: 8080 (application), 3306 (MySQL)
- **Database**: `educagestor_db`
- **User**: `educagestor_user1` / `SecurePass123!`

## 🔄 Version History

- **v1.0.1** - ✅ **Current Stable Release** (Latest Update)

  - ✅ **FIXED**: Swagger UI fully functional and accessible
  - ✅ **FIXED**: OpenAPI documentation endpoint working
  - ✅ **IMPROVED**: Database configuration and connection stability
  - ✅ **VERIFIED**: All major API endpoints tested and working
  - ✅ **ENHANCED**: Error handling and logging improvements
  - ✅ **UPDATED**: Configuration for MySQL with proper path mapping

- **v1.0.0** - Initial release with core functionality
  - User authentication and authorization
  - Complete CRUD operations for all entities
  - JWT token management
  - Swagger documentation
  - Comprehensive error handling

## 🎉 Latest Updates (v1.0.1)

**✅ Major Issues Resolved:**

1. **Swagger UI Configuration**: Fixed SpringDoc path configuration from `/api-docs` to `/v3/api-docs`
2. **Database Connection**: Stabilized MySQL connection with proper user credentials
3. **API Documentation**: Complete OpenAPI 3.0 specification now available
4. **Error Handling**: Improved error logging and debugging capabilities
5. **Configuration Management**: Streamlined application profiles for different environments

**🚀 Current Status:**

- ✅ Application starts successfully
- ✅ Database connection established
- ✅ Swagger UI fully functional
- ✅ All major endpoints tested and working
- ✅ JWT authentication working
- ✅ Role-based access control implemented
