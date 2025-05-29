#!/bin/bash

echo "=========================================="
echo " EducaGestor360 API - Deployment Script"
echo "=========================================="
echo

# Set variables
APP_NAME="educagestor-api"
VERSION="1.0.0"
DOCKER_IMAGE="$APP_NAME:$VERSION"

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo "Checking prerequisites..."

if ! command_exists docker; then
    echo "ERROR: Docker is not installed"
    exit 1
fi

if ! command_exists docker-compose; then
    echo "ERROR: Docker Compose is not installed"
    exit 1
fi

echo "✓ Docker and Docker Compose are installed"

# Build the application
echo
echo "Building the application..."
if ! mvn clean package -DskipTests; then
    echo "ERROR: Maven build failed"
    exit 1
fi
echo "✓ Application built successfully"

# Build Docker image
echo
echo "Building Docker image..."
if ! docker build -t $DOCKER_IMAGE .; then
    echo "ERROR: Docker image build failed"
    exit 1
fi
echo "✓ Docker image built successfully: $DOCKER_IMAGE"

# Stop existing containers
echo
echo "Stopping existing containers..."
docker-compose down

# Start the application
echo
echo "Starting the application..."
if ! docker-compose up -d; then
    echo "ERROR: Failed to start application"
    exit 1
fi

echo "✓ Application started successfully"

# Wait for application to be ready
echo
echo "Waiting for application to be ready..."
sleep 30

# Check application health
echo "Checking application health..."
if curl -f http://localhost:8080/api/actuator/health >/dev/null 2>&1; then
    echo "✓ Application is healthy"
else
    echo "WARNING: Application health check failed"
    echo "Check logs with: docker-compose logs educagestor-api"
fi

echo
echo "=========================================="
echo " Deployment completed!"
echo "=========================================="
echo
echo "Application URLs:"
echo "- API Base: http://localhost:8080/api"
echo "- Swagger UI: http://localhost:8080/swagger-ui.html"
echo "- API Docs: http://localhost:8080/api-docs"
echo "- Health Check: http://localhost:8080/api/actuator/health"
echo
echo "Database (ensure your chosen DB - MySQL or SQL Server - is running and configured):"
echo "- Example MySQL: localhost:3306"
echo "- Example SQL Server: localhost:1433"
echo
echo "Useful commands:"
echo "- View logs: docker-compose logs -f educagestor-api"
echo "- Stop application: docker-compose down"
echo "- Restart application: docker-compose restart"
echo "- View containers: docker-compose ps"
echo
