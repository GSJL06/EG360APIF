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
echo Checking PostgreSQL connection...
psql -h localhost -U postgres -d postgres -c "SELECT version();" 2>nul
if %errorlevel% neq 0 (
    echo WARNING: Cannot connect to PostgreSQL
    echo Please ensure PostgreSQL is running on localhost:5432
    echo You can continue if you have configured a different database
    echo.
)

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
