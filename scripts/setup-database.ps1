# EducaGestor360 API - PowerShell Database Setup Script
# Optimizado para Windows PostgreSQL

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " EducaGestor360 API - Database Setup" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Function to check if command exists
function Test-CommandExists {
    param($Command)
    $null = Get-Command $Command -ErrorAction SilentlyContinue
    return $?
}

# Database configuration
$DB_NAME = "educagestor_db"
$DB_USER = "educagestor_user"
$DB_PASSWORD = "educagestor_pass"
$DB_HOST = "localhost"
$DB_PORT = "5432"

Write-Host "Database Configuration:" -ForegroundColor White
Write-Host "- Database: $DB_NAME" -ForegroundColor Cyan
Write-Host "- User: $DB_USER" -ForegroundColor Cyan
Write-Host "- Host: $DB_HOST" -ForegroundColor Cyan
Write-Host "- Port: $DB_PORT" -ForegroundColor Cyan
Write-Host ""

# Check if PostgreSQL is installed
Write-Host "Checking PostgreSQL installation..." -ForegroundColor Yellow

if (-not (Test-CommandExists "psql")) {
    Write-Host "ERROR: PostgreSQL is not installed or not in PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install PostgreSQL from:" -ForegroundColor White
    Write-Host "https://www.postgresql.org/download/windows/" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "After installation, make sure to:" -ForegroundColor White
    Write-Host "1. Add PostgreSQL bin directory to your PATH" -ForegroundColor Yellow
    Write-Host "2. Remember the postgres user password you set during installation" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "✓ PostgreSQL is installed" -ForegroundColor Green

# Test connection to PostgreSQL
Write-Host ""
Write-Host "Testing connection to PostgreSQL..." -ForegroundColor Yellow
Write-Host "You will be prompted for the postgres user password" -ForegroundColor White

try {
    $env:PGPASSWORD = Read-Host "Enter postgres user password" -AsSecureString
    $env:PGPASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($env:PGPASSWORD))
    
    $result = psql -h $DB_HOST -p $DB_PORT -U postgres -d postgres -c "SELECT version();" 2>$null
    if ($LASTEXITCODE -ne 0) {
        throw "Connection failed"
    }
    Write-Host "✓ Successfully connected to PostgreSQL" -ForegroundColor Green
}
catch {
    Write-Host "ERROR: Cannot connect to PostgreSQL" -ForegroundColor Red
    Write-Host "Please check:" -ForegroundColor White
    Write-Host "1. PostgreSQL service is running" -ForegroundColor Yellow
    Write-Host "2. Correct password for postgres user" -ForegroundColor Yellow
    Write-Host "3. PostgreSQL is listening on port $DB_PORT" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "To start PostgreSQL service:" -ForegroundColor White
    Write-Host "- Open Services (services.msc)" -ForegroundColor Cyan
    Write-Host "- Find 'postgresql-x64-xx' service" -ForegroundColor Cyan
    Write-Host "- Start the service if it's stopped" -ForegroundColor Cyan
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Create database and user
Write-Host ""
Write-Host "Creating database and user..." -ForegroundColor Yellow

$sqlCommands = @"
-- Create database
CREATE DATABASE $DB_NAME
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Create user
CREATE USER $DB_USER WITH
    LOGIN
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    INHERIT
    NOREPLICATION
    CONNECTION LIMIT -1
    PASSWORD '$DB_PASSWORD';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
"@

try {
    $sqlCommands | psql -h $DB_HOST -p $DB_PORT -U postgres -d postgres
    if ($LASTEXITCODE -ne 0) {
        Write-Host "WARNING: Some commands may have failed (database might already exist)" -ForegroundColor Yellow
    }
    else {
        Write-Host "✓ Database and user created successfully" -ForegroundColor Green
    }
}
catch {
    Write-Host "WARNING: Database creation had some issues (might already exist)" -ForegroundColor Yellow
}

# Grant additional privileges
Write-Host ""
Write-Host "Setting up additional privileges..." -ForegroundColor Yellow

$additionalPrivileges = @"
-- Connect to the database
\c $DB_NAME;

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO $DB_USER;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO $DB_USER;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO $DB_USER;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO $DB_USER;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO $DB_USER;

-- Display success message
SELECT 'Database setup completed successfully!' as status;
"@

try {
    $additionalPrivileges | psql -h $DB_HOST -p $DB_PORT -U postgres
    Write-Host "✓ Additional privileges set successfully" -ForegroundColor Green
}
catch {
    Write-Host "WARNING: Some privilege settings may have failed" -ForegroundColor Yellow
}

# Test connection with new user
Write-Host ""
Write-Host "Testing connection with new user..." -ForegroundColor Yellow

try {
    $env:PGPASSWORD = $DB_PASSWORD
    $result = psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT current_database(), current_user;" 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Successfully connected with new user" -ForegroundColor Green
    }
    else {
        Write-Host "WARNING: Could not connect with new user" -ForegroundColor Yellow
    }
}
catch {
    Write-Host "WARNING: Could not test new user connection" -ForegroundColor Yellow
}

# Clean up environment variable
$env:PGPASSWORD = $null

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host " Database setup completed!" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Database connection details:" -ForegroundColor White
Write-Host "- Host: $DB_HOST" -ForegroundColor Cyan
Write-Host "- Port: $DB_PORT" -ForegroundColor Cyan
Write-Host "- Database: $DB_NAME" -ForegroundColor Cyan
Write-Host "- Username: $DB_USER" -ForegroundColor Cyan
Write-Host "- Password: $DB_PASSWORD" -ForegroundColor Cyan
Write-Host ""
Write-Host "You can now run the application with:" -ForegroundColor White
Write-Host ".\scripts\start-dev.ps1" -ForegroundColor Green
Write-Host ""
Write-Host "Or deploy with Docker:" -ForegroundColor White
Write-Host ".\scripts\deploy.ps1" -ForegroundColor Green
Write-Host ""

Read-Host "Press Enter to exit"
