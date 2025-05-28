# 🚀 EducaGestor360 API - Guía Rápida para Windows

## 📋 **Requisitos Previos**

### ✅ **Opción 1: Desarrollo Local**
1. **Java 17+** - [Descargar Adoptium](https://adoptium.net/)
2. **Maven 3.6+** - [Descargar Maven](https://maven.apache.org/download.cgi)
3. **PostgreSQL 12+** - [Descargar PostgreSQL](https://www.postgresql.org/download/windows/)
4. **Git** (opcional) - [Descargar Git](https://git-scm.com/download/win)

### ✅ **Opción 2: Docker (Más Fácil)**
1. **Docker Desktop** - [Descargar Docker](https://www.docker.com/products/docker-desktop/)

---

## 🎯 **Inicio Rápido - Opción Docker (Recomendado)**

### 1. Clonar el Proyecto
```powershell
git clone <repository-url>
cd educagestor-api
```

### 2. Ejecutar con Docker
```powershell
# Abrir PowerShell como Administrador
.\scripts\deploy.ps1
```

### 3. ¡Listo! 🎉
- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **pgAdmin**: http://localhost:5050

---

## 🛠 **Inicio Rápido - Desarrollo Local**

### 1. Configurar Base de Datos
```powershell
# Abrir PowerShell como Administrador
.\scripts\setup-database.ps1
```

### 2. Iniciar Aplicación
```powershell
.\scripts\start-dev.ps1
```

### 3. Probar API
```powershell
.\scripts\test-api.ps1
```

---

## 📝 **Scripts Disponibles para Windows**

| Script | Descripción | Uso |
|--------|-------------|-----|
| `setup-database.ps1` | Configura PostgreSQL | `.\scripts\setup-database.ps1` |
| `start-dev.ps1` | Inicia desarrollo local | `.\scripts\start-dev.ps1` |
| `deploy.ps1` | Despliega con Docker | `.\scripts\deploy.ps1` |
| `test-api.ps1` | Prueba endpoints API | `.\scripts\test-api.ps1` |
| `start-dev.bat` | Alternativa CMD | `scripts\start-dev.bat` |

---

## 🔐 **Usuarios de Prueba**

```
👨‍💼 Administrador:
- Usuario: admin
- Contraseña: admin123

👨‍🏫 Profesor:
- Usuario: teacher1
- Contraseña: teacher123

👨‍🎓 Estudiante:
- Usuario: student1
- Contraseña: student123
```

---

## 🌐 **URLs Importantes**

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **API Base** | http://localhost:8080/api | Endpoints REST |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentación interactiva |
| **API Docs** | http://localhost:8080/api-docs | Especificación OpenAPI |
| **Health Check** | http://localhost:8080/api/actuator/health | Estado de la aplicación |
| **pgAdmin** | http://localhost:5050 | Administrador de BD |

---

## 🔧 **Comandos Útiles**

### Docker
```powershell
# Ver logs
docker-compose logs -f educagestor-api

# Parar aplicación
docker-compose down

# Reiniciar aplicación
docker-compose restart

# Ver contenedores
docker-compose ps
```

### Maven (Desarrollo Local)
```powershell
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Crear JAR
mvn clean package

# Ejecutar aplicación
mvn spring-boot:run
```

### PostgreSQL
```powershell
# Conectar a base de datos
psql -h localhost -U educagestor_user -d educagestor_db

# Ver tablas
\dt

# Salir
\q
```

---

## 🚨 **Solución de Problemas Comunes**

### ❌ **Error: "Java no encontrado"**
```powershell
# Verificar instalación
java -version

# Si no funciona, agregar a PATH:
# 1. Buscar "Variables de entorno" en Windows
# 2. Agregar ruta de Java a PATH
# Ejemplo: C:\Program Files\Eclipse Adoptium\jdk-17.0.x\bin
```

### ❌ **Error: "Maven no encontrado"**
```powershell
# Verificar instalación
mvn -version

# Si no funciona, descargar Maven y agregar a PATH
# Ejemplo: C:\apache-maven-3.9.x\bin
```

### ❌ **Error: "PostgreSQL no conecta"**
```powershell
# Verificar servicio PostgreSQL
# 1. Abrir "Servicios" (services.msc)
# 2. Buscar "postgresql-x64-xx"
# 3. Iniciar servicio si está parado

# O reinstalar PostgreSQL con configuración por defecto
```

### ❌ **Error: "Docker no funciona"**
```powershell
# Verificar Docker Desktop
# 1. Abrir Docker Desktop
# 2. Verificar que esté ejecutándose
# 3. Reiniciar si es necesario

# Verificar desde PowerShell
docker --version
docker-compose --version
```

### ❌ **Error: "Puerto 8080 ocupado"**
```powershell
# Encontrar proceso usando puerto 8080
netstat -ano | findstr :8080

# Terminar proceso (reemplazar PID)
taskkill /PID <PID> /F

# O cambiar puerto en application.yml
```

---

## 📞 **Obtener Ayuda**

### 🔍 **Verificar Estado**
```powershell
# Probar API
.\scripts\test-api.ps1

# Ver logs (Docker)
docker-compose logs -f educagestor-api

# Ver logs (Local)
# Los logs aparecen en la consola donde ejecutaste start-dev.ps1
```

### 📚 **Documentación**
- **README.md** - Documentación completa
- **Swagger UI** - http://localhost:8080/swagger-ui.html
- **Postman Collection** - `postman/EducaGestor360-API.postman_collection.json`

### 🐛 **Reportar Problemas**
1. Ejecutar `.\scripts\test-api.ps1`
2. Copiar logs de error
3. Incluir versión de Java, Maven, PostgreSQL
4. Describir pasos para reproducir el problema

---

## 🎉 **¡Éxito!**

Si ves esto en tu navegador, ¡todo está funcionando!

**Swagger UI**: http://localhost:8080/swagger-ui.html

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    }
  }
}
```

**¡Felicidades! Tu API EducaGestor360 está lista para usar! 🚀**
