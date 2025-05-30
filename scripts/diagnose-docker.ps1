# EducaGestor360 API - Docker Diagnostic Script
# Script para diagnosticar problemas con Docker

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " EducaGestor360 API - Docker Diagnostics" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Function to test URL
function Test-Url {
    param($Url, $Description)
    
    Write-Host "Testing: $Description" -ForegroundColor Yellow
    Write-Host "  URL: $Url" -ForegroundColor Gray
    
    try {
        $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 10
        Write-Host "  ✓ Status: $($response.StatusCode)" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "  ✗ Error: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# 1. Check Docker containers
Write-Host "=== Docker Container Status ===" -ForegroundColor Cyan
try {
    $containers = docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    Write-Host $containers -ForegroundColor White
}
catch {
    Write-Host "Error getting container status" -ForegroundColor Red
}

Write-Host ""

# 2. Check specific containers
Write-Host "=== EducaGestor360 Containers ===" -ForegroundColor Cyan
try {
    $educaContainers = docker ps --filter "name=educagestor" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    if ($educaContainers) {
        Write-Host $educaContainers -ForegroundColor White
    }
    else {
        Write-Host "No EducaGestor360 containers found" -ForegroundColor Red
    }
}
catch {
    Write-Host "Error checking EducaGestor360 containers" -ForegroundColor Red
}

Write-Host ""

# 3. Check application logs
Write-Host "=== Application Logs (Last 20 lines) ===" -ForegroundColor Cyan
try {
    $logs = docker-compose logs --tail=20 educagestor-api 2>$null
    if ($logs) {
        Write-Host $logs -ForegroundColor White
    }
    else {
        Write-Host "No logs found or container not running" -ForegroundColor Red
    }
}
catch {
    Write-Host "Error getting application logs" -ForegroundColor Red
}

Write-Host ""

# 4. Check database logs
Write-Host "=== Database Logs (Last 10 lines) ===" -ForegroundColor Cyan
try {
    $dbLogs = docker-compose logs --tail=10 postgres 2>$null
    if ($dbLogs) {
        Write-Host $dbLogs -ForegroundColor White
    }
    else {
        Write-Host "No database logs found" -ForegroundColor Red
    }
}
catch {
    Write-Host "Error getting database logs" -ForegroundColor Red
}

Write-Host ""

# 5. Test endpoints
Write-Host "=== Endpoint Testing ===" -ForegroundColor Cyan

$endpoints = @(
    @{ Url = "http://localhost:8080"; Description = "Base Application" },
    @{ Url = "http://localhost:8080/api"; Description = "API Base" },
    @{ Url = "http://localhost:8080/api/actuator/health"; Description = "Health Check" },
    @{ Url = "http://localhost:8080/swagger-ui.html"; Description = "Swagger UI" },
    @{ Url = "http://localhost:8080/api-docs"; Description = "API Documentation" }
)

$workingEndpoints = 0
foreach ($endpoint in $endpoints) {
    if (Test-Url -Url $endpoint.Url -Description $endpoint.Description) {
        $workingEndpoints++
    }
    Start-Sleep -Milliseconds 500
}

Write-Host ""

# 6. Port check
Write-Host "=== Port Usage Check ===" -ForegroundColor Cyan
try {
    $portCheck = netstat -ano | Select-String ":8080"
    if ($portCheck) {
        Write-Host "Port 8080 usage:" -ForegroundColor White
        Write-Host $portCheck -ForegroundColor Gray
    }
    else {
        Write-Host "Port 8080 is not in use" -ForegroundColor Red
    }
}
catch {
    Write-Host "Error checking port usage" -ForegroundColor Red
}

Write-Host ""

# 7. Docker Compose status
Write-Host "=== Docker Compose Status ===" -ForegroundColor Cyan
try {
    $composeStatus = docker-compose ps 2>$null
    if ($composeStatus) {
        Write-Host $composeStatus -ForegroundColor White
    }
    else {
        Write-Host "No Docker Compose services found" -ForegroundColor Red
    }
}
catch {
    Write-Host "Error getting Docker Compose status" -ForegroundColor Red
}

Write-Host ""

# 8. Summary and recommendations
Write-Host "=== Diagnostic Summary ===" -ForegroundColor Cyan
Write-Host "Working endpoints: $workingEndpoints/5" -ForegroundColor White

if ($workingEndpoints -eq 0) {
    Write-Host ""
    Write-Host "🚨 CRITICAL: No endpoints are responding" -ForegroundColor Red
    Write-Host ""
    Write-Host "Recommended actions:" -ForegroundColor White
    Write-Host "1. Check if containers are running: docker ps" -ForegroundColor Yellow
    Write-Host "2. Restart services: docker-compose restart" -ForegroundColor Yellow
    Write-Host "3. Check logs: docker-compose logs educagestor-api" -ForegroundColor Yellow
    Write-Host "4. Rebuild if necessary: docker-compose down && docker-compose up -d" -ForegroundColor Yellow
}
elseif ($workingEndpoints -lt 3) {
    Write-Host ""
    Write-Host "⚠️  WARNING: Some endpoints are not responding" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Recommended actions:" -ForegroundColor White
    Write-Host "1. Wait a few more minutes for full startup" -ForegroundColor Yellow
    Write-Host "2. Check application logs for errors" -ForegroundColor Yellow
    Write-Host "3. Verify database connection" -ForegroundColor Yellow
}
else {
    Write-Host ""
    Write-Host "✅ GOOD: Most endpoints are responding" -ForegroundColor Green
    Write-Host ""
    Write-Host "If Swagger UI still doesn't work, try:" -ForegroundColor White
    Write-Host "1. Clear browser cache" -ForegroundColor Yellow
    Write-Host "2. Try incognito/private mode" -ForegroundColor Yellow
    Write-Host "3. Access directly: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== Quick Fix Commands ===" -ForegroundColor Cyan
Write-Host "Restart services:" -ForegroundColor White
Write-Host "  docker-compose restart" -ForegroundColor Green
Write-Host ""
Write-Host "Full rebuild:" -ForegroundColor White
Write-Host "  docker-compose down" -ForegroundColor Green
Write-Host "  docker-compose up -d" -ForegroundColor Green
Write-Host ""
Write-Host "View live logs:" -ForegroundColor White
Write-Host "  docker-compose logs -f educagestor-api" -ForegroundColor Green
Write-Host ""

Read-Host "Press Enter to exit"
