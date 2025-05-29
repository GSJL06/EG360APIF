@echo off
echo ========================================
echo  EducaGestor360 API - Development Setup
echo ========================================
echo.

echo Checking Java version...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo.
echo Checking Maven...
mvn -version
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)
 
echo.
echo Checking Database connection (MySQL/SQL Server)...
echo Please ensure your chosen database (MySQL or SQL Server) is running and accessible.
echo Configure connection details in application.yml and activate the correct profile (mysql or sqlserver).
echo For example, run with: mvn spring-boot:run -Dspring.profiles.active=mysql
echo.
 
echo.
echo Building the application...
mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo.
echo Starting EducaGestor360 API...
echo The application will be available at:
echo - API Base URL: http://localhost:8080/api
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo - API Docs: http://localhost:8080/api-docs
echo.
echo Press Ctrl+C to stop the application
echo.

mvn spring-boot:run

pause
