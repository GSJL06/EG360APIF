# 📮 EducaGestor360 API - Guía de Pruebas con Postman

Esta guía te ayudará a probar todos los endpoints de la API EducaGestor360 usando Postman.

## 🚀 Configuración Inicial

### 1. Información Base

- **URL Base:** `http://localhost:8080/api`
- **Autenticación:** Bearer Token (JWT)
- **Content-Type:** `application/json`

### 2. Variables de Entorno en Postman

Crea las siguientes variables en tu entorno de Postman:

| Variable  | Valor                                        |
| --------- | -------------------------------------------- |
| `baseUrl` | `http://localhost:8080/api`                  |
| `token`   | `{{token}}` (se actualizará automáticamente) |

## 🔐 Autenticación

### 1. Registro de Usuario

**POST** `{{baseUrl}}/auth/register`

**Headers:**

```
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "username": "testuser",
  "email": "testuser@educagestor360.com",
  "password": "test123",
  "firstName": "Test",
  "lastName": "User",
  "phoneNumber": "+1234567890",
  "roles": ["STUDENT"]
}
```

**⚠️ IMPORTANTE:** Los roles deben ser exactamente: `"ADMIN"`, `"TEACHER"`, o `"STUDENT"` (en mayúsculas).

**Script Post-response (Tests):**

```javascript
if (pm.response.code === 201) {
  const response = pm.response.json();
  pm.environment.set("token", response.token);
  pm.test("Registration successful", function () {
    pm.expect(response.token).to.not.be.undefined;
  });
}
```

### 2. Login de Usuario

**POST** `{{baseUrl}}/auth/login`

**Headers:**

```
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "username": "testadmin",
  "password": "admin123"
}
```

**Script Post-response (Tests):**

```javascript
if (pm.response.code === 200) {
  const response = pm.response.json();
  pm.environment.set("token", response.token);
  pm.test("Login successful", function () {
    pm.expect(response.token).to.not.be.undefined;
    pm.expect(response.username).to.eql("testadmin");
  });
}
```

## 👥 Gestión de Usuarios

### 3. Obtener Todos los Usuarios

**GET** `{{baseUrl}}/users`

**Headers:**

```
Authorization: Bearer {{token}}
```

**Query Parameters (Opcionales):**

- `page`: 0 (número de página)
- `size`: 20 (tamaño de página)
- `sortBy`: username (campo de ordenamiento)
- `sortDir`: asc (dirección de ordenamiento)

## 🎓 Gestión de Estudiantes

### 4. Obtener Todos los Estudiantes

**GET** `{{baseUrl}}/students`

**Headers:**

```
Authorization: Bearer {{token}}
```

### 5. Registrar Nuevo Estudiante

**POST** `{{baseUrl}}/students`

**Headers:**

```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "studentId": "STU004",
  "academicStatus": "ACTIVE",
  "enrollmentDate": "2024-01-15",
  "expectedGraduationDate": "2026-12-15",
  "gpa": 3.5,
  "totalCredits": 45,
  "major": "Computer Science",
  "advisor": "Dr. Smith",
  "emergencyContactName": "Jane Doe",
  "emergencyContactPhone": "+1234567890",
  "emergencyContactRelationship": "Mother",
  "user": {
    "username": "newstudent",
    "email": "newstudent@educagestor360.com",
    "password": "student123",
    "firstName": "New",
    "lastName": "Student",
    "phoneNumber": "+1234567890",
    "roles": ["STUDENT"]
  }
}
```

## 👨‍🏫 Gestión de Profesores

### 6. Obtener Todos los Profesores

**GET** `{{baseUrl}}/teachers`

**Headers:**

```
Authorization: Bearer {{token}}
```

### 7. Registrar Nuevo Profesor

**POST** `{{baseUrl}}/teachers`

**Headers:**

```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "employeeId": "EMP003",
  "department": "Computer Science",
  "position": "Assistant Professor",
  "hireDate": "2024-01-01",
  "salary": 75000.0,
  "officeLocation": "Building A, Room 301",
  "officeHours": "Mon-Wed-Fri 2:00-4:00 PM",
  "specializations": ["Java Programming", "Database Design"],
  "isActive": true,
  "user": {
    "username": "newteacher",
    "email": "newteacher@educagestor360.com",
    "password": "teacher123",
    "firstName": "New",
    "lastName": "Teacher",
    "phoneNumber": "+1234567890",
    "roles": ["TEACHER"]
  }
}
```

## 📚 Gestión de Cursos

### 8. Obtener Todos los Cursos

**GET** `{{baseUrl}}/courses`

**Headers:**

```
Authorization: Bearer {{token}}
```

### 9. Crear Nuevo Curso

**POST** `{{baseUrl}}/courses`

**Headers:**

```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "courseCode": "CS301",
  "courseName": "Data Structures",
  "description": "Advanced data structures and algorithms",
  "credits": 3,
  "department": "Computer Science",
  "semester": "FALL",
  "year": 2024,
  "maxEnrollment": 30,
  "currentEnrollment": 0,
  "schedule": "MWF 10:00-11:00 AM",
  "location": "Room 201",
  "prerequisites": ["CS101"],
  "isActive": true
}
```

## 📝 Gestión de Inscripciones

### 10. Obtener Todas las Inscripciones

**GET** `{{baseUrl}}/enrollments`

**Headers:**

```
Authorization: Bearer {{token}}
```

### 11. Inscribir Estudiante en Curso

**POST** `{{baseUrl}}/enrollments`

**Headers:**

```
Authorization: Bearer {{token}}
```

**Query Parameters:**

- `studentId`: 1
- `courseId`: 1

## 📊 Gestión de Calificaciones

### 12. Obtener Todas las Calificaciones

**GET** `{{baseUrl}}/grades`

**Headers:**

```
Authorization: Bearer {{token}}
```

### 13. Registrar Nueva Calificación

**POST** `{{baseUrl}}/grades`

**Headers:**

```
Authorization: Bearer {{token}}
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "studentId": 1,
  "courseId": 1,
  "assignmentName": "Midterm Exam",
  "gradeType": "EXAM",
  "gradeValue": 88.5,
  "maxPoints": 100.0,
  "weight": 0.3,
  "gradeDate": "2024-03-15",
  "comments": "Good performance on midterm",
  "isExtraCredit": false
}
```

## 🔍 Endpoints de Consulta Específica

### 14. Obtener Calificaciones por Estudiante

**GET** `{{baseUrl}}/grades/student/1`

**Headers:**

```
Authorization: Bearer {{token}}
```

### 15. Obtener Inscripciones por Curso

**GET** `{{baseUrl}}/enrollments/course/1`

**Headers:**

```
Authorization: Bearer {{token}}
```

### 16. Buscar Profesores por Departamento

**GET** `{{baseUrl}}/teachers/department/Computer Science`

**Headers:**

```
Authorization: Bearer {{token}}
```

## 📖 Documentación API

### 17. Acceder a Swagger UI

**GET** `{{baseUrl}}/swagger-ui.html`

**Nota:** Este endpoint se abre en el navegador, no en Postman.

## 🧪 Scripts de Prueba Automatizada

### Script para Colección Completa

Agrega este script a nivel de colección para pruebas automatizadas:

```javascript
pm.test("Response time is less than 2000ms", function () {
  pm.expect(pm.response.responseTime).to.be.below(2000);
});

pm.test("Status code is successful", function () {
  pm.expect(pm.response.code).to.be.oneOf([200, 201, 204]);
});

pm.test("Response has JSON content-type", function () {
  pm.expect(pm.response.headers.get("Content-Type")).to.include(
    "application/json"
  );
});
```

## 🚨 Códigos de Estado Esperados

| Endpoint         | Método   | Código Exitoso | Códigos de Error |
| ---------------- | -------- | -------------- | ---------------- |
| `/auth/register` | POST     | 201            | 400, 409         |
| `/auth/login`    | POST     | 200            | 400, 401         |
| `/users`         | GET      | 200            | 401, 403         |
| `/students`      | GET/POST | 200/201        | 400, 401, 403    |
| `/teachers`      | GET/POST | 200/201        | 400, 401, 403    |
| `/courses`       | GET/POST | 200/201        | 400, 401, 403    |
| `/enrollments`   | GET/POST | 200/201        | 400, 401, 403    |
| `/grades`        | GET/POST | 200/201        | 400, 401, 403    |

## 💡 Consejos para Pruebas

1. **Orden de Pruebas:** Ejecuta primero el login para obtener el token
2. **Variables:** Usa variables de entorno para reutilizar tokens y IDs
3. **Validaciones:** Agrega tests para validar la estructura de respuesta
4. **Datos de Prueba:** Usa datos consistentes para pruebas repetibles
5. **Cleanup:** Considera eliminar datos de prueba después de las pruebas

## 🔧 Solución de Problemas

### Token Expirado

Si recibes error 401, ejecuta nuevamente el endpoint de login.

### Permisos Insuficientes

Asegúrate de usar un usuario con rol ADMIN para endpoints administrativos.

### Datos Duplicados

Algunos endpoints pueden fallar si intentas crear datos duplicados (ej: mismo username).
