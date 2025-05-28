# EducaGestor360 API - PowerShell Deployment Script
# Optimizado para Windows PowerShell

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " EducaGestor360 API - Deployment Script" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Set variables
$APP_NAME = "educagestor-api"
$VERSION = "1.0.0"
$DOCKER_IMAGE = "$APP_NAME`:$VERSION"

# Function to check if command exists
function Test-CommandExists {
    param($Command)
    $null = Get-Command $Command -ErrorAction SilentlyContinue
    return $?
}

# Check prerequisites
Write-Host "Checking prerequisites..." -ForegroundColor Yellow

if (-not (Test-CommandExists "docker")) {
    Write-Host "ERROR: Docker is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Docker Desktop for Windows" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

if (-not (Test-CommandExists "docker-compose")) {
    Write-Host "ERROR: Docker Compose is not installed" -ForegroundColor Red
    Write-Host "Please install Docker Compose or use Docker Desktop" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✓ Docker and Docker Compose are installed" -ForegroundColor Green

# Check if Maven is available
if (Test-CommandExists "mvn") {
    # Build the application with Maven
    Write-Host ""
    Write-Host "Building the application with Maven..." -ForegroundColor Yellow
    
    try {
        mvn clean package -DskipTests
        if ($LASTEXITCODE -ne 0) {
            throw "Maven build failed"
        }
        Write-Host "✓ Application built successfully with Maven" -ForegroundColor Green
    }
    catch {
        Write-Host "ERROR: Maven build failed" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}
else {
    Write-Host "WARNING: Maven not found. Skipping build step." -ForegroundColor Yellow
    Write-Host "Make sure you have a pre-built JAR file in target/ directory" -ForegroundColor Yellow
    
    # Check if JAR exists
    $jarFiles = Get-ChildItem -Path "target" -Filter "*.jar" -ErrorAction SilentlyContinue
    if (-not $jarFiles) {
        Write-Host "ERROR: No JAR file found in target/ directory" -ForegroundColor Red
        Write-Host "Please install Maven and run the build, or provide a pre-built JAR" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
    Write-Host "✓ Found existing JAR file: $($jarFiles[0].Name)" -ForegroundColor Green
}

# Build Docker image
Write-Host ""
Write-Host "Building Docker image..." -ForegroundColor Yellow

try {
    docker build -t $DOCKER_IMAGE .
    if ($LASTEXITCODE -ne 0) {
        throw "Docker build failed"
    }
    Write-Host "✓ Docker image built successfully: $DOCKER_IMAGE" -ForegroundColor Green
}
catch {
    Write-Host "ERROR: Docker image build failed" -ForegroundColor Red
    Write-Host "Make sure Docker Desktop is running" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Stop existing containers
Write-Host ""
Write-Host "Stopping existing containers..." -ForegroundColor Yellow
docker-compose down

# Start the application
Write-Host ""
Write-Host "Starting the application..." -ForegroundColor Yellow

try {
    docker-compose up -d
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to start application"
    }
    Write-Host "✓ Application started successfully" -ForegroundColor Green
}
catch {
    Write-Host "ERROR: Failed to start application" -ForegroundColor Red
    Write-Host "Check Docker Desktop and try again" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Wait for application to be ready
Write-Host ""
Write-Host "Waiting for application to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Check application health
Write-Host "Checking application health..." -ForegroundColor Yellow

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/actuator/health" -UseBasicParsing -TimeoutSec 10
    if ($response.StatusCode -eq 200) {
        Write-Host "✓ Application is healthy" -ForegroundColor Green
    }
    else {
        Write-Host "WARNING: Application health check returned status: $($response.StatusCode)" -ForegroundColor Yellow
    }
}
catch {
    Write-Host "WARNING: Application health check failed" -ForegroundColor Yellow
    Write-Host "The application might still be starting up" -ForegroundColor Yellow
    Write-Host "Check logs with: docker-compose logs educagestor-api" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " Deployment completed!" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Application URLs:" -ForegroundColor White
Write-Host "- API Base: http://localhost:8080/api" -ForegroundColor Cyan
Write-Host "- Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "- API Docs: http://localhost:8080/api-docs" -ForegroundColor Cyan
Write-Host "- Health Check: http://localhost:8080/api/actuator/health" -ForegroundColor Cyan
Write-Host ""
Write-Host "Database:" -ForegroundColor White
Write-Host "- PostgreSQL: localhost:5432" -ForegroundColor Cyan
Write-Host "- pgAdmin: http://localhost:5050 (admin@educagestor360.com / admin123)" -ForegroundColor Cyan
Write-Host ""
Write-Host "Useful commands:" -ForegroundColor White
Write-Host "- View logs: docker-compose logs -f educagestor-api" -ForegroundColor Yellow
Write-Host "- Stop application: docker-compose down" -ForegroundColor Yellow
Write-Host "- Restart application: docker-compose restart" -ForegroundColor Yellow
Write-Host "- View containers: docker-compose ps" -ForegroundColor Yellow
Write-Host ""

# Ask if user wants to open browser
$openBrowser = Read-Host "Do you want to open Swagger UI in your browser? (y/n)"
if ($openBrowser -eq "y" -or $openBrowser -eq "Y") {
    Start-Process "http://localhost:8080/swagger-ui.html"
}

Write-Host "Deployment script completed!" -ForegroundColor Green
Read-Host "Press Enter to exit"
