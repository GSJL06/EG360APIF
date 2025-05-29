# EducaGestor360 API - PowerShell Database Setup Guide (Manual Steps for MySQL/SQL Server)
# This script was originally for PostgreSQL. Manual setup is now required for MySQL or SQL Server.

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " EducaGestor360 API - Database Setup" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "This script previously automated PostgreSQL setup. For MySQL or SQL Server, please follow manual setup steps." -ForegroundColor Yellow

# Function to check if command exists
function Test-CommandExists {
    param(
        [string]$command
    )
    $null = Get-Command $command -ErrorAction SilentlyContinue
    return $?
}

# Example Database configuration (adjust as needed)
$DB_NAME = "educagestor_db"
$DB_USER = "educagestor_user"
$DB_PASSWORD = "educagestor_pass"

Write-Host "Database Configuration (Example):" -ForegroundColor White
Write-Host "- Database: $DB_NAME" -ForegroundColor Cyan
Write-Host "- User: $DB_USER" -ForegroundColor Cyan
Write-Host ""

Write-Host "Instructions for MySQL:" -ForegroundColor Yellow
Write-Host "1. Ensure MySQL Server is installed and running."
Write-Host "2. Connect to MySQL using MySQL Workbench or command line."
Write-Host "3. Execute the following SQL commands (adjust names/passwords as needed):" -ForegroundColor White
Write-Host "   CREATE DATABASE IF NOT EXISTS $DB_NAME;" -ForegroundColor Cyan
Write-Host "   CREATE USER IF NOT EXISTS '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASSWORD';" -ForegroundColor Cyan
Write-Host "   GRANT ALL PRIVILEGES ON $DB_NAME.* TO '$DB_USER'@'localhost';" -ForegroundColor Cyan
Write-Host "   FLUSH PRIVILEGES;" -ForegroundColor Cyan
Write-Host ""

Write-Host "Instructions for SQL Server:" -ForegroundColor Yellow
Write-Host "1. Ensure SQL Server is installed and running."
Write-Host "2. Connect to SQL Server using SQL Server Management Studio (SSMS) or Azure Data Studio."
Write-Host "3. Execute the following T-SQL commands (adjust names/passwords as needed):" -ForegroundColor White
Write-Host "   IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = N'$DB_NAME')" -ForegroundColor Cyan
Write-Host "   BEGIN" -ForegroundColor Cyan
Write-Host "       CREATE DATABASE [$DB_NAME];" -ForegroundColor Cyan
Write-Host "   END" -ForegroundColor Cyan
Write-Host "   GO" -ForegroundColor Cyan
Write-Host "   USE master;" -ForegroundColor Cyan
Write-Host "   IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = N'$DB_USER')" -ForegroundColor Cyan
Write-Host "   BEGIN" -ForegroundColor Cyan
Write-Host "       CREATE LOGIN [$DB_USER] WITH PASSWORD=N'$DB_PASSWORD', DEFAULT_DATABASE=[master], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF;" -ForegroundColor Cyan
Write-Host "   END" -ForegroundColor Cyan
Write-Host "   GO" -ForegroundColor Cyan
Write-Host "   USE [$DB_NAME];" -ForegroundColor Cyan
Write-Host "   IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'$DB_USER')" -ForegroundColor Cyan
Write-Host "   BEGIN" -ForegroundColor Cyan
Write-Host "       CREATE USER [$DB_USER] FOR LOGIN [$DB_USER];" -ForegroundColor Cyan
Write-Host "       ALTER ROLE [db_owner] ADD MEMBER [$DB_USER];" -ForegroundColor Cyan
Write-Host "   END" -ForegroundColor Cyan
Write-Host "   GO" -ForegroundColor Cyan
Write-Host ""

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " Database setup completed!" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Database connection details:" -ForegroundColor White
Write-Host "- Host: localhost (or your DB server address)" -ForegroundColor Cyan
Write-Host "- Port: (MySQL: 3306, SQL Server: 1433)" -ForegroundColor Cyan
Write-Host "- Database: $DB_NAME" -ForegroundColor Cyan
Write-Host "- Username: $DB_USER" -ForegroundColor Cyan
Write-Host "- Password: $DB_PASSWORD" -ForegroundColor Cyan
