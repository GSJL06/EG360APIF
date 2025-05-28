# EducaGestor360 API - PowerShell Development Script
# Optimizado para Windows PowerShell

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " EducaGestor360 API - Development Setup" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Function to check if command exists
function Test-CommandExists {
    param($Command)
    $null = Get-Command $Command -ErrorAction SilentlyContinue
    return $?
}

# Check Java version
Write-Host "Checking Java version..." -ForegroundColor Yellow
if (Test-CommandExists "java") {
    java -version
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Java is installed" -ForegroundColor Green
    }
    else {
        Write-Host "ERROR: Java is not working properly" -ForegroundColor Red
        Write-Host "Please install Java 17 or higher" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}
else {
    Write-Host "ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17 or higher from:" -ForegroundColor Red
    Write-Host "https://adoptium.net/" -ForegroundColor Cyan
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""

# Check Maven
Write-Host "Checking Maven..." -ForegroundColor Yellow
if (Test-CommandExists "mvn") {
    mvn -version
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Maven is installed" -ForegroundColor Green
    }
    else {
        Write-Host "ERROR: Maven is not working properly" -ForegroundColor Red
        Write-Host "Please install Maven 3.6 or higher" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}
else {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven from:" -ForegroundColor Red
    Write-Host "https://maven.apache.org/download.cgi" -ForegroundColor Cyan
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""

# Check PostgreSQL connection
Write-Host "Checking PostgreSQL connection..." -ForegroundColor Yellow
if (Test-CommandExists "psql") {
    try {
        $result = psql -h localhost -U postgres -d postgres -c "SELECT version();" 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ PostgreSQL connection successful" -ForegroundColor Green
        }
        else {
            Write-Host "WARNING: Cannot connect to PostgreSQL" -ForegroundColor Yellow
            Write-Host "Please ensure PostgreSQL is running on localhost:5432" -ForegroundColor Yellow
            Write-Host "You can continue if you have configured a different database" -ForegroundColor Yellow
        }
    }
    catch {
        Write-Host "WARNING: Cannot connect to PostgreSQL" -ForegroundColor Yellow
        Write-Host "Please ensure PostgreSQL is running on localhost:5432" -ForegroundColor Yellow
    }
}
else {
    Write-Host "WARNING: psql command not found" -ForegroundColor Yellow
    Write-Host "Please ensure PostgreSQL is installed and running" -ForegroundColor Yellow
    Write-Host "Download from: https://www.postgresql.org/download/windows/" -ForegroundColor Cyan
}

Write-Host ""

# Build the application
Write-Host "Building the application..." -ForegroundColor Yellow
try {
    mvn clean compile
    if ($LASTEXITCODE -ne 0) {
        throw "Build failed"
    }
    Write-Host "✓ Application built successfully" -ForegroundColor Green
}
catch {
    Write-Host "ERROR: Build failed" -ForegroundColor Red
    Write-Host "Please check the error messages above" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Starting EducaGestor360 API..." -ForegroundColor Green
Write-Host "The application will be available at:" -ForegroundColor White
Write-Host "- API Base URL: http://localhost:8080/api" -ForegroundColor Cyan
Write-Host "- Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "- API Docs: http://localhost:8080/api-docs" -ForegroundColor Cyan
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host ""

# Start the application
try {
    mvn spring-boot:run
}
catch {
    Write-Host ""
    Write-Host "Application stopped" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Thank you for using EducaGestor360 API!" -ForegroundColor Green
Read-Host "Press Enter to exit"
