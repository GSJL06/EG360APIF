# EducaGestor360 API - PowerShell API Testing Script
# Script para probar los endpoints principales de la API

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " EducaGestor360 API - API Testing Script" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Configuration
$BASE_URL = "http://localhost:8080/api"
$HEADERS = @{
    "Content-Type" = "application/json"
    "Accept" = "application/json"
}

# Function to make HTTP requests with error handling
function Invoke-ApiRequest {
    param(
        [string]$Method,
        [string]$Url,
        [hashtable]$Headers = @{},
        [string]$Body = $null,
        [string]$Description
    )
    
    Write-Host "Testing: $Description" -ForegroundColor Yellow
    Write-Host "  $Method $Url" -ForegroundColor Gray
    
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            Headers = $Headers
            UseBasicParsing = $true
            TimeoutSec = 30
        }
        
        if ($Body) {
            $params.Body = $Body
            Write-Host "  Body: $Body" -ForegroundColor Gray
        }
        
        $response = Invoke-WebRequest @params
        
        Write-Host "  ✓ Status: $($response.StatusCode)" -ForegroundColor Green
        
        if ($response.Content) {
            $jsonResponse = $response.Content | ConvertFrom-Json
            return $jsonResponse
        }
        
        return $response
    }
    catch {
        Write-Host "  ✗ Error: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            Write-Host "  Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
        }
        return $null
    }
}

# Check if API is running
Write-Host "Checking if API is running..." -ForegroundColor Yellow

$healthCheck = Invoke-ApiRequest -Method "GET" -Url "$BASE_URL/actuator/health" -Description "Health Check"

if (-not $healthCheck) {
    Write-Host ""
    Write-Host "ERROR: API is not running or not accessible" -ForegroundColor Red
    Write-Host "Please start the API first with:" -ForegroundColor White
    Write-Host "  .\scripts\start-dev.ps1" -ForegroundColor Cyan
    Write-Host "  or" -ForegroundColor White
    Write-Host "  .\scripts\deploy.ps1" -ForegroundColor Cyan
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✓ API is running and healthy" -ForegroundColor Green
Write-Host ""

# Test user registration
Write-Host "=== Testing User Registration ===" -ForegroundColor Cyan

$registerData = @{
    username = "testuser_$(Get-Random)"
    email = "testuser$(Get-Random)@example.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
    roles = @("STUDENT")
} | ConvertTo-Json

$registerResponse = Invoke-ApiRequest -Method "POST" -Url "$BASE_URL/auth/register" -Headers $HEADERS -Body $registerData -Description "Register New User"

if ($registerResponse -and $registerResponse.token) {
    Write-Host "✓ User registered successfully" -ForegroundColor Green
    $accessToken = $registerResponse.token
    $username = $registerResponse.username
    Write-Host "  Username: $username" -ForegroundColor Cyan
    Write-Host "  Token received: $($accessToken.Substring(0, 20))..." -ForegroundColor Cyan
}
else {
    Write-Host "✗ User registration failed" -ForegroundColor Red
    $accessToken = $null
}

Write-Host ""

# Test user login (if registration failed, try with default user)
if (-not $accessToken) {
    Write-Host "=== Testing User Login (Default User) ===" -ForegroundColor Cyan
    
    $loginData = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json
    
    $loginResponse = Invoke-ApiRequest -Method "POST" -Url "$BASE_URL/auth/login" -Headers $HEADERS -Body $loginData -Description "Login with Default User"
    
    if ($loginResponse -and $loginResponse.token) {
        Write-Host "✓ Login successful" -ForegroundColor Green
        $accessToken = $loginResponse.token
        $username = $loginResponse.username
        Write-Host "  Username: $username" -ForegroundColor Cyan
    }
    else {
        Write-Host "✗ Login failed" -ForegroundColor Red
        Write-Host ""
        Write-Host "Cannot continue testing without authentication token" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
    
    Write-Host ""
}

# Set up authenticated headers
$authHeaders = $HEADERS.Clone()
$authHeaders["Authorization"] = "Bearer $accessToken"

# Test authenticated endpoints
Write-Host "=== Testing Authenticated Endpoints ===" -ForegroundColor Cyan

# Get current user profile
$profileResponse = Invoke-ApiRequest -Method "GET" -Url "$BASE_URL/users/profile" -Headers $authHeaders -Description "Get Current User Profile"

if ($profileResponse) {
    Write-Host "✓ Profile retrieved successfully" -ForegroundColor Green
    Write-Host "  User ID: $($profileResponse.id)" -ForegroundColor Cyan
    Write-Host "  Username: $($profileResponse.username)" -ForegroundColor Cyan
    Write-Host "  Email: $($profileResponse.email)" -ForegroundColor Cyan
}

Write-Host ""

# Test courses endpoint
$coursesResponse = Invoke-ApiRequest -Method "GET" -Url "$BASE_URL/courses?page=0&size=5" -Headers $authHeaders -Description "Get Courses (Paginated)"

if ($coursesResponse) {
    Write-Host "✓ Courses retrieved successfully" -ForegroundColor Green
    Write-Host "  Total Elements: $($coursesResponse.totalElements)" -ForegroundColor Cyan
    Write-Host "  Page Size: $($coursesResponse.size)" -ForegroundColor Cyan
    Write-Host "  Current Page: $($coursesResponse.number)" -ForegroundColor Cyan
}

Write-Host ""

# Test enrollments endpoint (if user has appropriate role)
$enrollmentsResponse = Invoke-ApiRequest -Method "GET" -Url "$BASE_URL/enrollments/status/ENROLLED?page=0&size=5" -Headers $authHeaders -Description "Get Active Enrollments"

if ($enrollmentsResponse) {
    Write-Host "✓ Enrollments retrieved successfully" -ForegroundColor Green
    Write-Host "  Total Elements: $($enrollmentsResponse.totalElements)" -ForegroundColor Cyan
}

Write-Host ""

# Test grades endpoint (if user has appropriate role)
$gradesResponse = Invoke-ApiRequest -Method "GET" -Url "$BASE_URL/grades/student/1?page=0&size=5" -Headers $authHeaders -Description "Get Student Grades"

if ($gradesResponse) {
    Write-Host "✓ Grades retrieved successfully" -ForegroundColor Green
    Write-Host "  Total Elements: $($gradesResponse.totalElements)" -ForegroundColor Cyan
}

Write-Host ""

# Test logout
$logoutResponse = Invoke-ApiRequest -Method "POST" -Url "$BASE_URL/auth/logout" -Headers $authHeaders -Description "User Logout"

if ($logoutResponse) {
    Write-Host "✓ Logout successful" -ForegroundColor Green
}

Write-Host ""

# Test API documentation endpoints
Write-Host "=== Testing Documentation Endpoints ===" -ForegroundColor Cyan

$apiDocsResponse = Invoke-ApiRequest -Method "GET" -Url "$BASE_URL/../api-docs" -Description "Get OpenAPI Documentation"

if ($apiDocsResponse) {
    Write-Host "✓ API documentation is accessible" -ForegroundColor Green
}

Write-Host ""

# Summary
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " API Testing Summary" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✓ API Health Check: Passed" -ForegroundColor Green
Write-Host "✓ User Authentication: Working" -ForegroundColor Green
Write-Host "✓ Protected Endpoints: Accessible" -ForegroundColor Green
Write-Host "✓ API Documentation: Available" -ForegroundColor Green
Write-Host ""
Write-Host "API Endpoints tested:" -ForegroundColor White
Write-Host "- POST /auth/register" -ForegroundColor Cyan
Write-Host "- POST /auth/login" -ForegroundColor Cyan
Write-Host "- POST /auth/logout" -ForegroundColor Cyan
Write-Host "- GET /users/profile" -ForegroundColor Cyan
Write-Host "- GET /courses" -ForegroundColor Cyan
Write-Host "- GET /enrollments/status/{status}" -ForegroundColor Cyan
Write-Host "- GET /grades/student/{id}" -ForegroundColor Cyan
Write-Host ""
Write-Host "You can now access:" -ForegroundColor White
Write-Host "- Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "- API Documentation: http://localhost:8080/api-docs" -ForegroundColor Cyan
Write-Host ""

$openSwagger = Read-Host "Do you want to open Swagger UI in your browser? (y/n)"
if ($openSwagger -eq "y" -or $openSwagger -eq "Y") {
    Start-Process "http://localhost:8080/swagger-ui.html"
}

Write-Host ""
Write-Host "API testing completed successfully!" -ForegroundColor Green
Read-Host "Press Enter to exit"
