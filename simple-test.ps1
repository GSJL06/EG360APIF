Write-Host "Testing EducaGestor360 API" -ForegroundColor Green

# Test Admin Login
Write-Host "Testing Admin Login..." -ForegroundColor Yellow
try {
    $body = '{"username":"admin","password":"admin123"}'
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -Body $body -ContentType "application/json"
    Write-Host "✅ Admin login successful" -ForegroundColor Green
    Write-Host "User: $($response.username)" -ForegroundColor Gray
    Write-Host "Roles: $($response.roles -join ', ')" -ForegroundColor Gray
} catch {
    Write-Host "❌ Admin login failed" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
