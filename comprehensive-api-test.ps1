Write-Host "=========================================" -ForegroundColor Green
Write-Host "  EducaGestor360 API Comprehensive Test" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"
$token = ""

# Test 1: User Registration
Write-Host "`n1. Testing User Registration..." -ForegroundColor Yellow
try {
    $registerBody = @{
        username = "testuser"
        email = "testuser@test.com"
        password = "test123"
        firstName = "Test"
        lastName = "User"
        roles = @("STUDENT")
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/register" -Method Post -Body $registerBody -ContentType "application/json"
    Write-Host "✅ Registration successful" -ForegroundColor Green
    $token = $response.token
} catch {
    Write-Host "❌ Registration failed" -ForegroundColor Red
}

# Test 2: User Login
Write-Host "`n2. Testing User Login..." -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "testadmin"
        password = "admin123"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "✅ Login successful" -ForegroundColor Green
    $token = $response.token
    Write-Host "   User: $($response.username) | Roles: $($response.roles -join ', ')" -ForegroundColor Gray
} catch {
    Write-Host "❌ Login failed" -ForegroundColor Red
}

if ($token) {
    $headers = @{ Authorization = "Bearer $token" }
    
    # Test 3: Get Users
    Write-Host "`n3. Testing Get Users..." -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/users" -Method Get -Headers $headers
        Write-Host "✅ Get users successful - Found $($response.totalElements) users" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get users failed" -ForegroundColor Red
    }
    
    # Test 4: Get Students
    Write-Host "`n4. Testing Get Students..." -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/students" -Method Get -Headers $headers
        Write-Host "✅ Get students successful - Found $($response.totalElements) students" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get students failed" -ForegroundColor Red
    }
    
    # Test 5: Get Courses
    Write-Host "`n5. Testing Get Courses..." -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/courses" -Method Get -Headers $headers
        Write-Host "✅ Get courses successful - Found $($response.totalElements) courses" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get courses failed" -ForegroundColor Red
    }
    
    # Test 6: Get Teachers
    Write-Host "`n6. Testing Get Teachers..." -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/teachers" -Method Get -Headers $headers
        Write-Host "✅ Get teachers successful" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get teachers failed (Known issue)" -ForegroundColor Yellow
    }
    
    # Test 7: Get Enrollments
    Write-Host "`n7. Testing Get Enrollments..." -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/enrollments" -Method Get -Headers $headers
        Write-Host "✅ Get enrollments successful" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get enrollments failed (Known issue)" -ForegroundColor Yellow
    }
    
    # Test 8: Get Grades
    Write-Host "`n8. Testing Get Grades..." -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/grades" -Method Get -Headers $headers
        Write-Host "✅ Get grades successful" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get grades failed (Known issue)" -ForegroundColor Yellow
    }
}

# Test 9: Swagger UI
Write-Host "`n9. Testing Swagger UI..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/swagger-ui.html" -Method Get
    Write-Host "✅ Swagger UI accessible - Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Swagger UI failed" -ForegroundColor Red
}

Write-Host "`n=========================================" -ForegroundColor Green
Write-Host "  API Testing Complete!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green
