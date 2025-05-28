-- EducaGestor360 Database Initialization Script
-- This script creates the database and user for the EducaGestor360 API

-- Create database
CREATE DATABASE educagestor_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Create user
CREATE USER educagestor_user WITH
    LOGIN
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    INHERIT
    NOREPLICATION
    CONNECTION LIMIT -1
    PASSWORD 'educagestor_pass';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE educagestor_db TO educagestor_user;

-- Connect to the database
\c educagestor_db;

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO educagestor_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO educagestor_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO educagestor_user;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO educagestor_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO educagestor_user;

-- Create indexes for better performance (these will be created by JPA, but listed here for reference)
-- Note: JPA will create these automatically based on entity annotations

-- Users table indexes
-- CREATE INDEX IF NOT EXISTS idx_user_email ON users(email);
-- CREATE INDEX IF NOT EXISTS idx_user_username ON users(username);

-- Students table indexes  
-- CREATE INDEX IF NOT EXISTS idx_student_student_id ON students(student_id);
-- CREATE INDEX IF NOT EXISTS idx_student_user_id ON students(user_id);

-- Teachers table indexes
-- CREATE INDEX IF NOT EXISTS idx_teacher_employee_id ON teachers(employee_id);
-- CREATE INDEX IF NOT EXISTS idx_teacher_user_id ON teachers(user_id);
-- CREATE INDEX IF NOT EXISTS idx_teacher_department ON teachers(department);

-- Courses table indexes
-- CREATE INDEX IF NOT EXISTS idx_course_code ON courses(course_code);
-- CREATE INDEX IF NOT EXISTS idx_course_teacher ON courses(teacher_id);
-- CREATE INDEX IF NOT EXISTS idx_course_status ON courses(course_status);

-- Enrollments table indexes
-- CREATE INDEX IF NOT EXISTS idx_enrollment_student ON enrollments(student_id);
-- CREATE INDEX IF NOT EXISTS idx_enrollment_course ON enrollments(course_id);
-- CREATE INDEX IF NOT EXISTS idx_enrollment_status ON enrollments(enrollment_status);

-- Grades table indexes
-- CREATE INDEX IF NOT EXISTS idx_grade_student ON grades(student_id);
-- CREATE INDEX IF NOT EXISTS idx_grade_course ON grades(course_id);
-- CREATE INDEX IF NOT EXISTS idx_grade_type ON grades(grade_type);
-- CREATE INDEX IF NOT EXISTS idx_grade_date ON grades(grade_date);

-- Display success message
SELECT 'Database initialization completed successfully!' as status;
