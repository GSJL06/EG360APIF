# EducaGestor360 API

Plataforma Integral de Gestión Educativa API construida con Spring Boot, MySQL y autenticación JWT.

**✅ Versión Actual: v1.0.2 - Completamente Funcional con Docker y Swagger UI Operativo**

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
- **Base de Datos**: MySQL 8.0+ (Principal), SQL Server (Alternativo)
- **Seguridad**: Spring Security, JWT (JSON Web Tokens)
- **Documentación**: Swagger/OpenAPI 3 ✅ **FUNCIONANDO**
- **Containerización**: Docker & Docker Compose ✅ **FUNCIONANDO**
- **Herramienta de Construcción**: Maven
- **Testing**: JUnit 5, Spring Boot Test

## 📋 Prerrequisitos

### Para Ejecución Local:

- Java 17 o superior
- Maven 3.6 o superior
- **Base de Datos** (una de las siguientes):
  - **H2** (recomendado para desarrollo - incluido)
  - **MySQL 8.0+** (para producción)
  - **PostgreSQL 12+** (alternativa)

### Para Ejecución con Docker:

- Docker Desktop (Windows/Mac) o Docker Engine (Linux)
- Docker Compose v2.0 o superior

### Para Pruebas de API:

- **Postman** (recomendado)
- **curl** (incluido en la mayoría de sistemas)

## 🔧 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/GSJL06/EG360APIF.git
cd educagestor-api
```

### 2. Configuración de Base de Datos

#### 🚀 Para Desarrollo Rápido (H2 - Recomendado):

**¡Inicio más rápido sin configuración de base de datos!**

```bash
# Construir la aplicación
mvn clean package -DskipTests

# Ejecutar con H2 (base de datos en memoria)
java -jar target/educagestor-api-1.0.0.jar --spring.profiles.active=h2
```

**Características de H2:**

- ✅ **Sin configuración**: No requiere instalación de base de datos
- ✅ **Datos de prueba**: Se crean automáticamente usuarios y datos de ejemplo
- ✅ **Consola web**: Disponible en http://localhost:8080/api/h2-console
- ✅ **Ideal para desarrollo**: Perfecto para pruebas y desarrollo

**URLs con H2:**

- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui/index.html
- **H2 Console**: http://localhost:8080/api/h2-console

#### Para MySQL (Producción):

```sql
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS educagestor_db;

-- Crear usuario (Credenciales actualizadas)
CREATE USER IF NOT EXISTS 'educagestor_user1'@'localhost' IDENTIFIED BY 'educagestor_pass';

-- Otorgar privilegios
GRANT ALL PRIVILEGES ON educagestor_db.* TO 'educagestor_user1'@'localhost';
FLUSH PRIVILEGES;
```

**✅ Configuración Verificada y Funcionando:**

- Base de Datos: `educagestor_db`
- Usuario: `educagestor_user1`
- Contraseña: `educagestor_pass`
- Host: `localhost:3306`

### 3. Configurar Aplicación

La aplicación está preconfigurada para MySQL. La configuración está en `src/main/resources/application-mysql.yml`:

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

#### 🚀 Inicio Rápido (Recomendado - H2):

```bash
# Construir la aplicación
mvn clean package -DskipTests

# Ejecutar con H2 (sin configuración de BD)
java -jar target/educagestor-api-1.0.0.jar --spring.profiles.active=h2
```

#### Inicio con MySQL:

```bash
# Construir la aplicación
mvn clean package -DskipTests

# Ejecutar con perfil MySQL
java -jar target/educagestor-api-1.0.0.jar --spring.profiles.active=mysql
```

#### Métodos Alternativos:

**Usando Maven directamente:**

```bash
mvn clean install
mvn spring-boot:run -Dspring.profiles.active=mysql
```

**Usando Docker (Recomendado para Desarrollo):**

```bash
# Construir y ejecutar con Docker Compose
docker-compose up --build

# Ejecutar en segundo plano
docker-compose up -d --build

# Ver logs en tiempo real
docker-compose logs -f educagestor-api

# Detener contenedores
docker-compose down
```

**Para Desarrollo:**

```bash
# Ejecutar en modo desarrollo con recarga automática
mvn spring-boot:run -Dspring.profiles.active=mysql
```

La API estará disponible en `http://localhost:8080/api`

## 🐳 Configuración Docker

### Arquitectura Docker

La aplicación incluye configuración completa de Docker con:

- **Dockerfile multi-stage** para optimización de imagen
- **Docker Compose** para orquestación de servicios
- **Configuración de red** para comunicación entre contenedores
- **Health checks** para monitoreo de estado
- **Volúmenes persistentes** para datos

### Variables de Entorno Docker

El contenedor utiliza las siguientes variables de entorno:

```yaml
environment:
  SPRING_PROFILES_ACTIVE: docker,mysql
  DB_HOST: host.docker.internal # Para conectar a MySQL en host
  DB_PORT: 3306
  DB_NAME: educagestor_db
  DB_USERNAME: educagestor_user1
  DB_PASSWORD: educagestor_pass
  JWT_SECRET: mySecretKey123456789012345678901234567890
```

### URLs Docker

Cuando la aplicación corre en Docker, las URLs son:

- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html ✅ **FUNCIONANDO**
- **API Docs**: http://localhost:8080/api/v3/api-docs
- **Health Check**: http://localhost:8080/api/actuator/health

### Comandos Docker Útiles

```bash
# Ver estado de contenedores
docker-compose ps

# Ver logs específicos
docker-compose logs educagestor-api

# Reconstruir imagen sin cache
docker-compose build --no-cache educagestor-api

# Ejecutar comando dentro del contenedor
docker-compose exec educagestor-api bash

# Limpiar recursos Docker
docker-compose down --volumes --remove-orphans
```

## � Configuración Docker

### Arquitectura Docker

La aplicación incluye configuración completa de Docker con:

- **Dockerfile multi-stage** para optimización de imagen
- **Docker Compose** para orquestación de servicios
- **Configuración de red** para comunicación entre contenedores
- **Health checks** para monitoreo de estado
- **Volúmenes persistentes** para datos

### Variables de Entorno Docker

El contenedor utiliza las siguientes variables de entorno:

```yaml
environment:
  SPRING_PROFILES_ACTIVE: docker,mysql
  DB_HOST: host.docker.internal # Para conectar a MySQL en host
  DB_PORT: 3306
  DB_NAME: educagestor_db
  DB_USERNAME: educagestor_user1
  DB_PASSWORD: educagestor_pass
  JWT_SECRET: mySecretKey123456789012345678901234567890
```

### URLs Docker

Cuando la aplicación corre en Docker, las URLs son:

- **API Base**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html ✅ **FUNCIONANDO**
- **API Docs**: http://localhost:8080/api/v3/api-docs
- **Health Check**: http://localhost:8080/api/actuator/health

### Comandos Docker Útiles

```bash
# Ver estado de contenedores
docker-compose ps

# Ver logs específicos
docker-compose logs educagestor-api

# Reconstruir imagen sin cache
docker-compose build --no-cache educagestor-api

# Ejecutar comando dentro del contenedor
docker-compose exec educagestor-api bash

# Limpiar recursos Docker
docker-compose down --volumes --remove-orphans
```

## �📚 Documentación API

✅ **Documentación Swagger Completamente Funcional**

Una vez que la aplicación esté ejecutándose, accede a la documentación interactiva de la API en:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html ✅ **FUNCIONANDO**
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs ✅ **FUNCIONANDO**

**Características Disponibles en Swagger UI:**

- 🔐 Soporte de autenticación JWT con Bearer token
- 📋 7 categorías de API: Autenticación, Gestión de Usuarios, Gestión de Estudiantes, Gestión de Profesores, Gestión de Cursos, Gestión de Calificaciones, Gestión de Inscripciones
- 📖 Documentación completa de endpoints con esquemas de solicitud/respuesta
- 🧪 Interfaz de testing interactiva
- 📊 Documentación de validación de datos y restricciones

### 🔐 Usuarios de Prueba Predeterminados

La aplicación viene con usuarios de prueba preconfigurados:

| Usuario    | Contraseña | Rol     | Descripción                            |
| ---------- | ---------- | ------- | -------------------------------------- |
| `admin`    | `admin123` | ADMIN   | Acceso completo al sistema             |
| `teacher1` | `admin123` | TEACHER | Profesor de Ciencias de la Computación |
| `teacher2` | `admin123` | TEACHER | Profesor de Matemáticas                |
| `student1` | `admin123` | STUDENT | Estudiante de prueba 1                 |
| `student2` | `admin123` | STUDENT | Estudiante de prueba 2                 |
| `student3` | `admin123` | STUDENT | Estudiante de prueba 3                 |

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

## 🧪 Pruebas

### Pruebas Unitarias

Ejecutar la suite de pruebas:

```bash
mvn test
```

### Pruebas de API

Probar el endpoint de autenticación:

```bash
# Iniciar sesión con usuario admin
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  http://localhost:8080/api/auth/login
```

### Verificación Rápida de API

```bash
# Verificar si la API está ejecutándose
curl http://localhost:8080/api/swagger-ui.html

# Probar autenticación
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"testadmin","password":"admin123"}' \
  http://localhost:8080/api/auth/login
```

## 🧪 Pruebas Comprehensivas de API

### Script de Pruebas Automatizadas

Hemos incluido un script de PowerShell para pruebas comprehensivas de API:

```bash
# Ejecutar el script de pruebas comprehensivas
powershell -ExecutionPolicy Bypass -File comprehensive-api-test.ps1
```

### Datos de Prueba Precargados

La aplicación viene con datos de prueba precargados para testing inmediato:

**Usuarios de Prueba:**

- **Admin:** `testadmin` / `admin123` (rol ADMIN) ✅ Verificado
- **Profesores:** `teacher1`, `teacher2` / `admin123` (rol TEACHER)
- **Estudiantes:** `student1`, `student2`, `student3` / `admin123` (rol STUDENT)

**Datos de Ejemplo:**

- **8 Usuarios** (1 admin + 1 testadmin + 2 profesores + 3 estudiantes + 1 testuser)
- **3 Estudiantes** con perfiles académicos completos
- **3 Cursos** (CS101: Introducción a la Programación, MATH201: Cálculo I, CS201: Estructuras de Datos)
- **4 Inscripciones** vinculando estudiantes a cursos
- **24+ Calificaciones** con varios tipos de asignaciones y puntajes

### Pruebas Manuales con curl

**1. Registro de Usuario:**

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

**2. Inicio de Sesión de Usuario:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testadmin",
    "password": "admin123"
  }'
```

**3. Probar Endpoints Protegidos (reemplazar TOKEN con JWT del login):**

```bash
# Obtener todos los usuarios (Solo Admin)
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/users

# Obtener todos los estudiantes
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/students

# Obtener todos los cursos
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/courses

# Obtener todos los profesores
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/teachers

# Obtener todas las inscripciones
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/enrollments

# Obtener todas las calificaciones
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/grades
```

### Pruebas con Postman

📋 **Guía Completa de Postman**: Ver [POSTMAN_TESTING_GUIDE.md](POSTMAN_TESTING_GUIDE.md) para instrucciones detalladas paso a paso.

**Configuración Rápida:**

1. Establecer variables de entorno: `baseUrl` = `http://localhost:8080/api`
2. Comenzar con endpoints de autenticación para obtener token JWT
3. Usar token para todas las solicitudes subsecuentes

### Documentación de API

Acceder a la documentación interactiva de la API:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html ✅ Funcionando
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

### Reporte de Estado de Endpoints

| Endpoint         | Método | Estado         | Descripción                            |
| ---------------- | ------ | -------------- | -------------------------------------- |
| `/auth/register` | POST   | ✅ Funcionando | Registro de usuario                    |
| `/auth/login`    | POST   | ✅ Funcionando | Autenticación de usuario               |
| `/users`         | GET    | ✅ Funcionando | Listar todos los usuarios (Solo Admin) |
| `/students`      | GET    | ✅ Funcionando | Listar todos los estudiantes           |
| `/students`      | POST   | ✅ Funcionando | Registrar nuevo estudiante             |
| `/teachers`      | GET    | ✅ Funcionando | Listar todos los profesores            |
| `/teachers`      | POST   | ✅ Funcionando | Registrar nuevo profesor               |
| `/courses`       | GET    | ✅ Funcionando | Listar todos los cursos                |
| `/courses`       | POST   | ✅ Funcionando | Crear nuevo curso                      |
| `/enrollments`   | GET    | ✅ Funcionando | Listar todas las inscripciones         |
| `/enrollments`   | POST   | ✅ Funcionando | Inscribir estudiante en curso          |
| `/grades`        | GET    | ✅ Funcionando | Listar todas las calificaciones        |
| `/grades`        | POST   | ✅ Funcionando | Registrar nueva calificación           |

**¡Todos los endpoints principales están completamente funcionales!** 🎉

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

## 🚨 Solución de Problemas

### Problemas Comunes

**La aplicación no inicia:**

- Asegurar que MySQL esté ejecutándose y accesible
- Verificar credenciales de base de datos en `application-mysql.yml`
- Verificar si el puerto 8080 está disponible
- Ejecutar: `mvn clean package -DskipTests` para reconstruir

**Errores de conexión a base de datos:**

- Verificar que el usuario MySQL `educagestor_user1` existe con la contraseña correcta
- Asegurar que la base de datos `educagestor_db` esté creada
- Verificar que MySQL esté ejecutándose en el puerto 3306
- Probar conexión: `mysql -u educagestor_user1 -p educagestor_db`

**Swagger UI no carga:**

- ✅ **RESUELTO**: Swagger UI ahora está completamente funcional
- Acceder en: http://localhost:8080/api/swagger-ui.html
- Documentos API en: http://localhost:8080/api/v3/api-docs
- Si persisten problemas, verificar que la aplicación inició exitosamente (buscar "Started EducaGestorApiApplication")
- Asegurar que no hay firewall bloqueando el puerto 8080
- Verificar con: `netstat -ano | findstr :8080`

**Endpoints de API devolviendo errores 500:**

- ✅ **RESUELTO**: Todos los endpoints principales ahora funcionan
- Si persisten problemas, verificar logs de aplicación en `logs/educagestor-api.log`
- Reiniciar aplicación: `taskkill /F /IM java.exe` luego reiniciar

**Problemas de autenticación:**

- Usar credenciales de prueba correctas: `testadmin` / `admin123`
- Asegurar que el token JWT esté incluido en el header Authorization
- Formato de token: `Bearer <your-jwt-token>`
- Los tokens expiran después de 24 horas - iniciar sesión nuevamente si es necesario

### Configuración Conocida que Funciona

✅ **Configuración Verificada y Funcionando:**

- **SO**: Windows 10/11
- **Java**: OpenJDK 17
- **MySQL**: 8.0+
- **Maven**: 3.6+
- **Puerto**: 8080 (aplicación), 3306 (MySQL)
- **Base de Datos**: `educagestor_db`
- **Usuario**: `educagestor_user1` / `educagestor_pass`

## 🔄 Historial de Versiones

- **v1.0.2** - ✅ **Versión Estable Actual** (Última Actualización)

  - ✅ **AGREGADO**: Soporte completo para Docker y Docker Compose
  - ✅ **CORREGIDO**: Context-path configurado correctamente para Docker
  - ✅ **MEJORADO**: Configuración multi-entorno (local, docker, mysql, sqlserver)
  - ✅ **AGREGADO**: Health checks y monitoreo de contenedores
  - ✅ **DOCUMENTADO**: Guía completa de Docker y comandos útiles
  - ✅ **VERIFICADO**: Swagger UI funcionando en todos los entornos

- **v1.0.1** - Versión Estable Anterior

  - ✅ **CORREGIDO**: Swagger UI completamente funcional y accesible
  - ✅ **CORREGIDO**: Endpoint de documentación OpenAPI funcionando
  - ✅ **MEJORADO**: Configuración de base de datos y estabilidad de conexión
  - ✅ **VERIFICADO**: Todos los endpoints principales de API probados y funcionando
  - ✅ **MEJORADO**: Manejo de errores y mejoras de logging
  - ✅ **ACTUALIZADO**: Configuración para MySQL con mapeo de rutas apropiado

- **v1.0.0** - Lanzamiento inicial con funcionalidad principal
  - Autenticación y autorización de usuarios
  - Operaciones CRUD completas para todas las entidades
  - Gestión de tokens JWT
  - Documentación Swagger
  - Manejo comprehensivo de errores

## 🎉 Últimas Actualizaciones (v1.0.2)

**✅ Nuevas Características Agregadas:**

1. **Soporte Docker Completo**: Configuración completa de Docker y Docker Compose
2. **Context-Path Docker**: Corregido context-path `/api` para entorno Docker
3. **Configuración Multi-Entorno**: Perfiles optimizados para local, docker, mysql, sqlserver
4. **Health Checks**: Monitoreo automático de estado de contenedores
5. **Documentación Docker**: Guía completa con comandos útiles y troubleshooting

**🐳 Características Docker:**

- ✅ Dockerfile multi-stage optimizado
- ✅ Docker Compose con networking
- ✅ Variables de entorno configurables
- ✅ Health checks automáticos
- ✅ Volúmenes persistentes
- ✅ Logs centralizados

**🚀 Estado Actual:**

- ✅ Aplicación funciona en local y Docker
- ✅ Swagger UI operativo en ambos entornos
- ✅ Base de datos MySQL conectada
- ✅ Todos los endpoints probados y funcionando
- ✅ Autenticación JWT completamente funcional
- ✅ Control de acceso basado en roles implementado
- ✅ Docker deployment listo para producción

---

**¡Tu API EducaGestor360 está lista para usar en producción!** 🚀
