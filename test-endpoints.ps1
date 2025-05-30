# EducaGestor360 API Endpoint Testing Script

$baseUrl = "http://localhost:8080/api"
$adminToken = ""

Write-Host "🚀 Testing EducaGestor360 API Endpoints" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# Test 1: Authentication - Admin Login
Write-Host "`n1. 🔐 Testing Admin Authentication..." -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $adminToken = $response.accessToken
    Write-Host "✅ Admin login successful" -ForegroundColor Green
    Write-Host "   Token: $($adminToken.Substring(0,20))..." -ForegroundColor Gray
} catch {
    Write-Host "❌ Admin login failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Authentication - Teacher Login
Write-Host "`n2. 🔐 Testing Teacher Authentication..." -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "teacher1"
        password = "admin123"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "✅ Teacher login successful" -ForegroundColor Green
} catch {
    Write-Host "❌ Teacher login failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Authentication - Student Login
Write-Host "`n3. 🔐 Testing Student Authentication..." -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "student1"
        password = "admin123"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "✅ Student login successful" -ForegroundColor Green
} catch {
    Write-Host "❌ Student login failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get Users (Admin only)
if ($adminToken) {
    Write-Host "`n4. 👥 Testing Get Users (Admin)..." -ForegroundColor Yellow
    try {
        $headers = @{ Authorization = "Bearer $adminToken" }
        $response = Invoke-RestMethod -Uri "$baseUrl/users" -Method Get -Headers $headers
        Write-Host "✅ Get users successful - Found $($response.content.Count) users" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get users failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 5: Get Students
if ($adminToken) {
    Write-Host "`n5. 🎓 Testing Get Students..." -ForegroundColor Yellow
    try {
        $headers = @{ Authorization = "Bearer $adminToken" }
        $response = Invoke-RestMethod -Uri "$baseUrl/students" -Method Get -Headers $headers
        Write-Host "✅ Get students successful - Found $($response.content.Count) students" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get students failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 6: Get Teachers
if ($adminToken) {
    Write-Host "`n6. 👨‍🏫 Testing Get Teachers..." -ForegroundColor Yellow
    try {
        $headers = @{ Authorization = "Bearer $adminToken" }
        $response = Invoke-RestMethod -Uri "$baseUrl/teachers" -Method Get -Headers $headers
        Write-Host "✅ Get teachers successful - Found $($response.content.Count) teachers" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get teachers failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 7: Get Courses
if ($adminToken) {
    Write-Host "`n7. 📚 Testing Get Courses..." -ForegroundColor Yellow
    try {
        $headers = @{ Authorization = "Bearer $adminToken" }
        $response = Invoke-RestMethod -Uri "$baseUrl/courses" -Method Get -Headers $headers
        Write-Host "✅ Get courses successful - Found $($response.content.Count) courses" -ForegroundColor Green
    } catch {
        Write-Host "❌ Get courses failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n🎉 API Testing Complete!" -ForegroundColor Green
