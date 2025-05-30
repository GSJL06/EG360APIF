Write-Host "🚀 EducaGestor360 API Comprehensive Testing" -ForegroundColor Green
Write-Host "===========================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"

# Test 1: Check if API is responding
Write-Host "`n1. 🌐 Testing API Connectivity..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/" -Method Get -ErrorAction Stop
    Write-Host "✅ API is responding - Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ API connectivity failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

# Test 2: Test Admin Login with detailed error handling
Write-Host "`n2. 🔐 Testing Admin Login..." -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json

    Write-Host "Request Body: $loginBody" -ForegroundColor Gray

    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json" -ErrorAction Stop
    Write-Host "✅ Admin login successful!" -ForegroundColor Green
    Write-Host "Username: $($response.username)" -ForegroundColor Gray
    Write-Host "Roles: $($response.roles -join ', ')" -ForegroundColor Gray
    Write-Host "Token: $($response.accessToken.Substring(0,30))..." -ForegroundColor Gray

    $adminToken = $response.accessToken

} catch {
    Write-Host "❌ Admin login failed" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "Error Message: $($_.Exception.Message)" -ForegroundColor Red

    # Try to get more details from the response
    try {
        $errorDetails = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorDetails)
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
    } catch {
        Write-Host "Could not read error response body" -ForegroundColor Red
    }
}

# Test 3: Test Swagger UI endpoint
Write-Host "`n3. 📚 Testing Swagger UI..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/swagger-ui.html" -Method Get -ErrorAction Stop
    Write-Host "✅ Swagger UI accessible - Status: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Swagger UI failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Test API Docs endpoint
Write-Host "`n4. 📖 Testing API Docs..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/v3/api-docs" -Method Get -ErrorAction Stop
    Write-Host "✅ API Docs accessible - Title: $($response.info.title)" -ForegroundColor Green
} catch {
    Write-Host "❌ API Docs failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTesting Summary Complete!" -ForegroundColor Green
