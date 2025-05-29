#!/bin/bash

echo "========================================"
echo " EducaGestor360 API - Development Setup"
echo "========================================"
echo

echo "Checking Java version..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi
java -version

echo
echo "Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi
mvn -version
 
echo
echo "Checking Database connection (MySQL/SQL Server)..."
echo "Please ensure your chosen database (MySQL or SQL Server) is running and accessible."
echo "Configure connection details in application.yml and activate the correct profile (mysql or sqlserver)."
echo "For example, run with: mvn spring-boot:run -Dspring.profiles.active=mysql"
echo
 
echo
echo "Building the application..."
if ! mvn clean compile; then
    echo "ERROR: Build failed"
    exit 1
fi

echo
echo "Starting EducaGestor360 API..."
echo "The application will be available at:"
echo "- API Base URL: http://localhost:8080/api"
echo "- Swagger UI: http://localhost:8080/swagger-ui.html"
echo "- API Docs: http://localhost:8080/api-docs"
echo
echo "Press Ctrl+C to stop the application"
echo

mvn spring-boot:run
