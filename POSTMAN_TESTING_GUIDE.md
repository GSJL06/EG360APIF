# 📋 Guía Completa de Pruebas con Postman - EducaGestor360 API

Esta guía te ayudará a configurar y probar todos los endpoints de la API EducaGestor360 usando Postman.

**✅ ACTUALIZADO: Todos los endpoints funcionando con H2 y archivos de prueba incluidos**

## 🚀 Configuración Inicial

### 1. Prerrequisitos

- **Postman** instalado (Desktop o Web)
- **API ejecutándose** en `http://localhost:8080`

### 2. Verificar que la API esté corriendo

Antes de comenzar, asegúrate de que la API esté ejecutándose:

```bash
# Ejecutar la aplicación con H2 (recomendado para pruebas)
java -jar target/educagestor-api-1.0.0.jar --spring.profiles.active=h2
```

**URLs de verificación:**
- API Base: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui/index.html

## 📁 Configuración de Postman

### 3. Crear Colección y Environment

#### A. Crear Nueva Colección
1. Abrir Postman
2. Clic en "New" → "Collection"
3. Nombre: `EducaGestor360 API`
4. Descripción: `API completa para gestión educativa`

#### B. Crear Environment
1. Clic en "Environments" → "Create Environment"
2. Nombre: `EducaGestor360 Local`
3. Agregar variables:

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| `base_url` | `http://localhost:8080/api` | `http://localhost:8080/api` |
| `admin_token` | | (se llenará automáticamente) |
| `teacher_token` | | (se llenará automáticamente) |
| `student_token` | | (se llenará automáticamente) |

### 4. Script de Tests Automático

Para todos los endpoints de autenticación, agregar este script en la pestaña **"Tests"**:

```javascript
if (pm.response.code === 200) {
    const response = pm.response.json();
    
    // Guardar token según el rol del usuario
    if (response.roles && response.roles.includes("ADMIN")) {
        pm.environment.set("admin_token", response.token);
        console.log("Admin token guardado");
    } else if (response.roles && response.roles.includes("TEACHER")) {
        pm.environment.set("teacher_token", response.token);
        console.log("Teacher token guardado");
    } else if (response.roles && response.roles.includes("STUDENT")) {
        pm.environment.set("student_token", response.token);
        console.log("Student token guardado");
    }
    
    // Guardar información del usuario
    pm.environment.set("current_user_id", response.id);
    pm.environment.set("current_username", response.username);
    
    // Mostrar información en consola
    console.log("Usuario autenticado:", response.username);
    console.log("Roles:", response.roles);
}
```

## 🔐 Endpoints de Autenticación

### 5. Registro de Usuarios

#### A. Register Admin
- **Método**: `POST`
- **URL**: `{{base_url}}/auth/register`
- **Headers**: 
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "username": "admin",
    "email": "admin@educagestor360.com",
    "password": "admin123",
    "firstName": "System",
    "lastName": "Administrator",
    "roles": ["ADMIN"]
  }
  ```
- **Tests**: Agregar el script de arriba

#### B. Register Teacher
- **Método**: `POST`
- **URL**: `{{base_url}}/auth/register`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "username": "teacher1",
    "email": "teacher1@educagestor360.com",
    "password": "teacher123",
    "firstName": "John",
    "lastName": "Smith",
    "roles": ["TEACHER"]
  }
  ```

#### C. Register Student
- **Método**: `POST`
- **URL**: `{{base_url}}/auth/register`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "username": "student1",
    "email": "student1@educagestor360.com",
    "password": "student123",
    "firstName": "Alice",
    "lastName": "Brown",
    "roles": ["STUDENT"]
  }
  ```

### 6. Login de Usuarios

#### D. Login Admin
- **Método**: `POST`
- **URL**: `{{base_url}}/auth/login`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- **Tests**: Agregar el script de arriba

## 👥 Endpoints Protegidos

### 7. Configuración de Autorización

Para todos los endpoints protegidos:
1. Ir a la pestaña **"Authorization"**
2. Type: `Bearer Token`
3. Token: `{{admin_token}}` (o `{{teacher_token}}`, `{{student_token}}` según corresponda)

### 8. Gestión de Usuarios (ADMIN)

#### E. Get All Users
- **Método**: `GET`
- **URL**: `{{base_url}}/users`
- **Authorization**: `Bearer {{admin_token}}`

#### F. Get User by ID
- **Método**: `GET`
- **URL**: `{{base_url}}/users/1`
- **Authorization**: `Bearer {{admin_token}}`

### 9. Gestión de Profesores (ADMIN)

#### G. Create Teacher Profile
- **Método**: `POST`
- **URL**: `{{base_url}}/teachers`
- **Authorization**: `Bearer {{admin_token}}`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "employeeId": "T001",
    "user": {
      "username": "prof_smith",
      "email": "prof.smith@educagestor360.com",
      "firstName": "John",
      "lastName": "Smith",
      "phoneNumber": "+1234567891"
    },
    "department": "Computer Science",
    "specialization": "Software Engineering",
    "qualifications": "PhD in Computer Science, 10 years experience",
    "hireDate": "2020-01-15",
    "officeLocation": "Building A, Room 201",
    "officeHours": "Monday-Friday 2:00-4:00 PM",
    "employmentStatus": "ACTIVE"
  }
  ```

#### H. Get All Teachers
- **Método**: `GET`
- **URL**: `{{base_url}}/teachers`
- **Authorization**: `Bearer {{admin_token}}`

### 10. Gestión de Estudiantes (ADMIN/TEACHER)

#### I. Create Student Profile
- **Método**: `POST`
- **URL**: `{{base_url}}/students`
- **Authorization**: `Bearer {{admin_token}}`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "studentId": "S001",
    "user": {
      "username": "alice_brown",
      "email": "alice.brown@educagestor360.com",
      "firstName": "Alice",
      "lastName": "Brown",
      "phoneNumber": "+1234567893"
    },
    "dateOfBirth": "2000-05-15",
    "address": "123 Main St, City, State 12345",
    "emergencyContact": "Mary Brown",
    "emergencyPhone": "+1234567899",
    "enrollmentDate": "2024-09-01",
    "academicStatus": "ACTIVE"
  }
  ```

#### J. Get All Students
- **Método**: `GET`
- **URL**: `{{base_url}}/students`
- **Authorization**: `Bearer {{admin_token}}`

## 📚 Gestión de Cursos

### 11. Cursos (ADMIN/TEACHER)

#### K. Create Course
- **Método**: `POST`
- **URL**: `{{base_url}}/courses`
- **Authorization**: `Bearer {{admin_token}}`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "description": "Basic concepts of computer science and programming",
    "credits": 3,
    "startDate": "2025-01-15",
    "endDate": "2025-05-15",
    "maxStudents": 30,
    "schedule": "Monday, Wednesday, Friday 10:00-11:30",
    "classroom": "Room 101",
    "teacherId": 1,
    "courseStatus": "ACTIVE"
  }
  ```

#### L. Get All Courses
- **Método**: `GET`
- **URL**: `{{base_url}}/courses`
- **Authorization**: `Bearer {{admin_token}}`

## 📝 Gestión de Inscripciones

### 12. Inscripciones (ADMIN/TEACHER)

#### M. Enroll Student in Course
- **Método**: `POST`
- **URL**: `{{base_url}}/enrollments?studentId=1&courseId=1`
- **Authorization**: `Bearer {{admin_token}}`
- **Nota**: Este endpoint usa parámetros de consulta, no body JSON

#### N. Get All Enrollments
- **Método**: `GET`
- **URL**: `{{base_url}}/enrollments`
- **Authorization**: `Bearer {{admin_token}}`

## 📊 Gestión de Calificaciones

### 13. Calificaciones (ADMIN/TEACHER)

#### O. Create Grade
- **Método**: `POST`
- **URL**: `{{base_url}}/grades`
- **Authorization**: `Bearer {{admin_token}}`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "studentId": 1,
    "courseId": 1,
    "assignmentName": "Midterm Exam",
    "gradeType": "EXAM",
    "gradeValue": 85.5,
    "maxPoints": 100.0,
    "gradeDate": "2025-03-15",
    "weight": 0.3,
    "comments": "Good understanding of concepts, minor errors in implementation"
  }
  ```

#### P. Get All Grades
- **Método**: `GET`
- **URL**: `{{base_url}}/grades`
- **Authorization**: `Bearer {{admin_token}}`

## 🔄 Orden Recomendado de Pruebas

### Secuencia de Pruebas:

1. **Registro de usuarios** (A, B, C)
2. **Login de admin** (D) - ¡Importante para obtener token!
3. **Crear perfil de profesor** (G)
4. **Crear perfil de estudiante** (I)
5. **Crear curso** (K)
6. **Inscribir estudiante** (M)
7. **Crear calificación** (O)
8. **Consultar datos** (E, H, J, L, N, P)

## ✅ Verificación de Resultados

### Respuestas Esperadas:

- **Registro/Login exitoso**: Status 200, JWT token en respuesta
- **Endpoints protegidos**: Status 200 con datos JSON
- **Errores de autorización**: Status 401/403
- **Datos inválidos**: Status 400 con mensaje de error

### Troubleshooting:

- **401 Unauthorized**: Verificar que el token esté incluido en Authorization header
- **403 Forbidden**: El usuario no tiene permisos para ese endpoint
- **404 Not Found**: Verificar la URL del endpoint
- **500 Internal Error**: Verificar que la aplicación esté corriendo

## 🎯 Próximos Pasos

Una vez completadas las pruebas básicas:

1. **Explorar Swagger UI**: http://localhost:8080/api/swagger-ui/index.html
2. **Probar endpoints adicionales**: PUT, DELETE, etc.
3. **Validar reglas de negocio**: Intentar operaciones no permitidas
4. **Probar con diferentes roles**: Teacher y Student tokens

¡Todos los endpoints están completamente funcionales y listos para usar! 🚀
