# EducaGestor360 API

Comprehensive Educational Management Platform API built with Spring Boot, MySQL, and JWT authentication.

## 🚀 Características

- **Autenticación y Autorización de Usuarios**: Autenticación basada en JWT con control de acceso por roles
- **Gestión de Estudiantes**: Operaciones CRUD completas para registros de estudiantes y seguimiento académico
- **Gestión de Profesores**: Perfiles de profesores, asignación de cursos y seguimiento de rendimiento
- **Gestión de Cursos**: Creación de cursos, programación y gestión de inscripciones
- **Sistema de Inscripciones**: Inscripción de estudiantes en cursos con seguimiento de estado
- **Gestión de Calificaciones**: Sistema integral de calificaciones con análisis
- **Documentación API**: Documentación interactiva Swagger/OpenAPI
- **Seguridad**: Encriptación de contraseñas BCrypt y seguridad de tokens JWT
- **Registro de Auditoría**: Rastro de auditoría completo para todas las operaciones

## 🛠 Stack Tecnológico

- **Backend**: Java 17, Spring Boot 3.2.0
- **Database**: MySQL 8.0+
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Testing**: JUnit 5, Spring Boot Test

## 📋 Prerrequisitos

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## 🔧 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd educagestor-api
```

### 2. Configuración de Base de Datos

#### Para MySQL:

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

```yaml
# Para SQL Server (busca la sección con "on-profile: sqlserver")
url: jdbc:sqlserver://tu-host:1433;databaseName=tu-base-datos;encrypt=false;trustServerCertificate=true
username: tu-usuario
password: tu-contraseña
```

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

La API estará disponible en `http://localhost:8080/api`

## 📚 Documentación API

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

## 🔐 Autenticación

### Roles de Usuario

- **ADMIN**: Acceso completo al sistema, puede gestionar todas las entidades
- **TEACHER**: Puede gestionar cursos, estudiantes y calificaciones para cursos asignados
- **STUDENT**: Acceso limitado, puede ver su propia información y calificaciones

### Flujo de Autenticación

1. **Registrar/Iniciar Sesión**: Usa `/api/auth/login` o `/api/auth/register`
2. **Obtener Token JWT**: Recibe token de acceso y token de actualización
3. **Usar Token**: Incluye en el header Authorization: `Bearer <token>`
4. **Actualizar Token**: Usa `/api/auth/refresh` cuando el token expire

## 🌐 Endpoints API

### Autenticación

- `POST /api/auth/login` - Inicio de sesión de usuario
- `POST /api/auth/register` - Registro de usuario
- `POST /api/auth/refresh` - Actualizar token JWT
- `POST /api/auth/logout` - Cerrar sesión de usuario

### Gestión de Usuarios

- `GET /api/users/profile` - Obtener perfil del usuario actual
- `PUT /api/users/profile` - Actualizar perfil del usuario actual
- `GET /api/users/{id}` - Obtener usuario por ID (Admin)
- `GET /api/users` - Listar todos los usuarios (Admin)

### Gestión de Estudiantes

- `POST /api/students` - Registrar estudiante
- `GET /api/students` - Listar estudiantes (paginado)
- `GET /api/students/{id}` - Obtener estudiante por ID
- `PUT /api/students/{id}` - Actualizar estudiante
- `DELETE /api/students/{id}` - Eliminar estudiante (desactiva)
- `GET /api/students/{id}/academic-history` - Obtener historial académico

### Gestión de Profesores

- `POST /api/teachers` - Registrar profesor
- `GET /api/teachers` - Listar profesores (paginado)
- `GET /api/teachers/{id}` - Obtener profesor por ID
- `PUT /api/teachers/{id}` - Actualizar profesor
- `GET /api/teachers/{id}/courses` - Obtener cursos del profesor

### Gestión de Cursos

- `POST /api/courses` - Crear curso
- `GET /api/courses` - Listar cursos
- `GET /api/courses/{id}` - Obtener detalles del curso
- `PUT /api/courses/{id}` - Actualizar curso
- `POST /api/courses/{id}/assign-teacher` - Asignar profesor
- `GET /api/courses/{id}/students` - Obtener estudiantes inscritos

### Gestión de Inscripciones

- `POST /api/enrollments` - Inscribir estudiante en curso
- `GET /api/enrollments/student/{studentId}` - Obtener inscripciones del estudiante
- `DELETE /api/enrollments/{id}` - Cancelar inscripción
- `GET /api/enrollments/course/{courseId}` - Obtener inscripciones del curso

### Gestión de Calificaciones

- `POST /api/grades` - Registrar calificación
- `GET /api/grades/student/{studentId}` - Obtener calificaciones del estudiante
- `GET /api/grades/course/{courseId}` - Obtener calificaciones del curso
- `PUT /api/grades/{id}` - Actualizar calificación

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

La aplicación utiliza las siguientes entidades principales:

- **Users**: Información base del usuario y autenticación
- **Students**: Datos específicos del estudiante y estado académico
- **Teachers**: Perfiles de profesores e información de empleo
- **Courses**: Detalles del curso, horarios y capacidad (compatible con MySQL/SQL Server)
- **Enrollments**: Relaciones estudiante-curso
- **Grades**: Registros de calificaciones individuales y evaluaciones

## 🔒 Características de Seguridad

- **Encriptación de Contraseñas**: Hash BCrypt para almacenamiento seguro de contraseñas
- **Autenticación JWT**: Autenticación basada en tokens sin estado
- **Acceso Basado en Roles**: Control de permisos de grano fino
- **Configuración CORS**: Compartición de recursos de origen cruzado configurable
- **Validación de Entrada**: Validación integral de solicitudes
- **Protección contra Inyección SQL**: Protección de consultas JPA/Hibernate (generalmente bueno, pero siempre revisa queries nativas si las usas)

## 📝 Logging

La aplicación incluye logging integral:

- **Eventos de Autenticación**: Intentos de inicio de sesión, generación de tokens
- **Operaciones de Negocio**: Operaciones CRUD, cambios de inscripción
- **Seguimiento de Errores**: Manejo de excepciones y respuestas de error
- **Rastro de Auditoría**: Acciones del usuario y cambios del sistema

## 🚀 Despliegue

### Configuración de Producción

1. Actualizar `application.yml` para el entorno de producción
2. Configurar conexión a base de datos de producción
3. Establecer clave secreta JWT segura
4. Habilitar HTTPS/SSL (recomendado)
5. Configurar niveles de logging

### Despliegue con Docker

```dockerfile
# Ejemplo de Dockerfile
FROM openjdk:17-jdk-slim
COPY target/educagestor-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contribuir

1. Hacer fork del repositorio
2. Crear una rama de característica (`git checkout -b feature/caracteristica-increible`)
3. Confirmar tus cambios (`git commit -m 'Agregar característica increíble'`)
4. Empujar a la rama (`git push origin feature/caracteristica-increible`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 📞 Soporte

Para soporte y preguntas:

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
