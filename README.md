# EducaGestor360 API

Plataforma Integral de Gestión Educativa API construida con Spring Boot, MySQL/SQL Server, y autenticación JWT.

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
- **Base de Datos**: MySQL / SQL Server
- **Seguridad**: Spring Security, JWT (JSON Web Tokens)
- **Documentación**: Swagger/OpenAPI 3
- **Herramienta de Construcción**: Maven
- **Testing**: JUnit 5, Spring Boot Test

## 📋 Prerrequisitos

- Java 17 o superior
- MySQL 8.0+ o SQL Server 2019+
- Maven 3.6 o superior

## 🔧 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd educagestor-api
```

### 2. Configuración de Base de Datos

#### Para MySQL:

```sql
-- Crear base de datos
CREATE DATABASE educagestor_db;

-- Crear usuario
CREATE USER 'educagestor_user'@'localhost' IDENTIFIED BY 'educagestor_pass';

-- Otorgar privilegios
GRANT ALL PRIVILEGES ON educagestor_db.* TO 'educagestor_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Para SQL Server:

```sql
-- Crear base de datos
CREATE DATABASE educagestor_db;

-- Crear usuario
CREATE LOGIN educagestor_user WITH PASSWORD = 'educagestor_pass';
USE educagestor_db;
CREATE USER educagestor_user FOR LOGIN educagestor_user;

-- Otorgar privilegios
ALTER ROLE db_owner ADD MEMBER educagestor_user;
```

### 3. Configurar Aplicación

Actualiza `src/main/resources/application.yml` con las credenciales de tu base de datos:

#### Para MySQL:

```yaml
spring:
  profiles:
    active: mysql
  datasource:
    url: jdbc:mysql://localhost:3306/educagestor_db
    username: educagestor_user
    password: educagestor_pass
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
```

#### Para SQL Server:

```yaml
spring:
  profiles:
    active: sqlserver
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=educagestor_db
    username: educagestor_user
    password: educagestor_pass
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
  jpa:
    database-platform: org.hibernate.dialect.SQLServerDialect
```

### 4. Construir y Ejecutar

#### Para Windows (PowerShell - Recomendado):

```powershell
# Configurar base de datos (solo la primera vez)
# (Ahora es manual, sigue las instrucciones SQL de arriba para MySQL o SQL Server)
echo "Por favor, configura tu base de datos MySQL o SQL Server manualmente."

# Iniciar servidor de desarrollo
# Ejemplo para MySQL:
mvn spring-boot:run -Dspring.profiles.active=mysql

# O desplegar con Docker
.\scripts\deploy.ps1

# Probar la API
.\scripts\test-api.ps1
```

#### Para Windows (Command Prompt):

```cmd
# Iniciar servidor de desarrollo para MySQL
mvn clean install
mvn spring-boot:run -Dspring.profiles.active=mysql

# O para SQL Server
mvn spring-boot:run -Dspring.profiles.active=sqlserver
```

#### Para Linux/Mac:

```bash
# Configurar base de datos (solo la primera vez)
# (Ahora es manual, sigue las instrucciones SQL de arriba para MySQL o SQL Server)
echo "Por favor, configura tu base de datos MySQL o SQL Server manualmente."

# Iniciar servidor de desarrollo
# Ejemplo para MySQL:
mvn spring-boot:run -Dspring.profiles.active=mysql

# O desplegar con Docker
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```

La API estará disponible en `http://localhost:8080/api`

## 📚 Documentación API

Una vez que la aplicación esté ejecutándose, accede a la documentación interactiva de la API en:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

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

Ejecutar la suite de pruebas:

```bash
mvn test
```

## 📊 Esquema de Base de Datos

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
- Documentación: [API Docs](http://localhost:8080/swagger-ui.html)
- Issues: [GitHub Issues](https://github.com/your-repo/issues)

## 🔄 Historial de Versiones

- **v1.0.0** - Lanzamiento inicial con funcionalidad principal
  - Autenticación y autorización de usuarios
  - Operaciones CRUD completas para todas las entidades
  - Gestión de tokens JWT
  - Documentación Swagger
  - Manejo integral de errores

- **v1.1.0** - Agregado soporte para MySQL y SQL Server, eliminada dependencia de PostgreSQL
  - Soporte multi-base de datos (MySQL/SQL Server)
  - Configuraciones de perfil específicas por base de datos
  - Documentación actualizada (OpenAPI)
  - Mejoras en la gestión de estudiantes (eliminación suave)
