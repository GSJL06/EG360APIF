Write-Host "Testing EducaGestor360 API" -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"

# Test API connectivity
Write-Host "1. Testing API connectivity..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/" -Method Get
    Write-Host "API is responding - Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "API connectivity failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Admin Login
Write-Host "2. Testing Admin Login..." -ForegroundColor Yellow
try {
    $body = '{"username":"admin","password":"admin123"}'
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $body -ContentType "application/json"
    Write-Host "Admin login successful!" -ForegroundColor Green
    Write-Host "Username: $($response.username)" -ForegroundColor Gray
    Write-Host "Roles: $($response.roles)" -ForegroundColor Gray
    $adminToken = $response.accessToken
} catch {
    Write-Host "Admin login failed" -ForegroundColor Red
    Write-Host "Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Swagger UI
Write-Host "3. Testing Swagger UI..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/swagger-ui.html" -Method Get
    Write-Host "Swagger UI accessible - Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "Swagger UI failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Testing complete!" -ForegroundColor Green
