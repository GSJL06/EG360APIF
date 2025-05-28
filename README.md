# EducaGestor360 API

Comprehensive Educational Management Platform API built with Spring Boot, PostgreSQL, and JWT authentication.

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
- **Database**: PostgreSQL
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Testing**: JUnit 5, Spring Boot Test

## 📋 Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
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
CREATE DATABASE educagestor_db;

-- Create user
CREATE USER educagestor_user WITH PASSWORD 'educagestor_pass';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE educagestor_db TO educagestor_user;
```

### 3. Configure Application

Update `src/main/resources/application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/educagestor_db
    username: your_db_username
    password: your_db_password
```

### 4. Build and Run

#### For Windows (PowerShell - Recommended):

```powershell
# Setup database (first time only)
.\scripts\setup-database.ps1

# Start development server
.\scripts\start-dev.ps1

# Or deploy with Docker
.\scripts\deploy.ps1

# Test the API
.\scripts\test-api.ps1
```

#### For Windows (Command Prompt):

```cmd
# Start development server
scripts\start-dev.bat

# Or use Maven directly
mvn clean install
mvn spring-boot:run
```

#### For Linux/Mac:

```bash
# Setup database (first time only)
chmod +x scripts/init-database.sql
psql -U postgres -f scripts/init-database.sql

# Start development server
chmod +x scripts/start-dev.sh
./scripts/start-dev.sh

# Or deploy with Docker
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```

The API will be available at `http://localhost:8080/api`

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

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

Run the test suite:

```bash
mvn test
```

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
- Documentation: [API Docs](http://localhost:8080/swagger-ui.html)
- Issues: [GitHub Issues](https://github.com/your-repo/issues)

## 🔄 Version History

- **v1.0.0** - Initial release with core functionality
  - User authentication and authorization
  - Complete CRUD operations for all entities
  - JWT token management
  - Swagger documentation
  - Comprehensive error handling
