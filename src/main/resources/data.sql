-- Initial data for EducaGestor360 API
-- This script creates sample data for testing and development

-- Insert sample admin user
-- Password: admin123 (BCrypt encoded)
INSERT IGNORE INTO users (username, email, password, first_name, last_name, phone_number, active, created_at, updated_at)
VALUES
('admin', 'admin@educagestor360.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'System', 'Administrator', '+1234567890', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('teacher1', 'teacher1@educagestor360.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'John', 'Smith', '+1234567891', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('teacher2', 'teacher2@educagestor360.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Sarah', 'Johnson', '+1234567892', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('student1', 'student1@educagestor360.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Alice', 'Brown', '+1234567893', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('student2', 'student2@educagestor360.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Bob', 'Wilson', '+1234567894', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('student3', 'student3@educagestor360.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Carol', 'Davis', '+1234567895', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert user roles
INSERT IGNORE INTO user_roles (user_id, role)
SELECT u.id, 'ADMIN' FROM users u WHERE u.username = 'admin';

INSERT IGNORE INTO user_roles (user_id, role)
SELECT u.id, 'TEACHER' FROM users u WHERE u.username IN ('teacher1', 'teacher2');

INSERT IGNORE INTO user_roles (user_id, role)
SELECT u.id, 'STUDENT' FROM users u WHERE u.username IN ('student1', 'student2', 'student3');

-- Insert sample teachers
INSERT IGNORE INTO teachers (employee_id, user_id, department, specialization, qualifications, hire_date, office_location, office_hours, employment_status, created_at, updated_at)
SELECT
    'EMP001',
    u.id,
    'Computer Science',
    'Software Engineering',
    'PhD in Computer Science, 10+ years industry experience',
    '2020-01-15',
    'Building A, Room 101',
    'Monday-Friday 9:00-17:00',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'teacher1';

INSERT IGNORE INTO teachers (employee_id, user_id, department, specialization, qualifications, hire_date, office_location, office_hours, employment_status, created_at, updated_at)
SELECT
    'EMP002',
    u.id,
    'Mathematics',
    'Applied Mathematics',
    'PhD in Mathematics, Research in Applied Statistics',
    '2019-08-20',
    'Building B, Room 205',
    'Tuesday-Thursday 10:00-16:00',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'teacher2';

-- Insert sample students
INSERT IGNORE INTO students (student_id, user_id, date_of_birth, address, emergency_contact, emergency_phone, enrollment_date, academic_status, created_at, updated_at)
SELECT
    'STU001',
    u.id,
    '2000-05-15',
    '123 Main St, City, State 12345',
    'Mary Brown (Mother)',
    '+1234567896',
    '2023-09-01',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'student1';

INSERT IGNORE INTO students (student_id, user_id, date_of_birth, address, emergency_contact, emergency_phone, enrollment_date, academic_status, created_at, updated_at)
SELECT
    'STU002',
    u.id,
    '1999-12-03',
    '456 Oak Ave, City, State 12345',
    'Robert Wilson (Father)',
    '+1234567897',
    '2023-09-01',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'student2';

INSERT IGNORE INTO students (student_id, user_id, date_of_birth, address, emergency_contact, emergency_phone, enrollment_date, academic_status, created_at, updated_at)
SELECT
    'STU003',
    u.id,
    '2001-03-22',
    '789 Pine Rd, City, State 12345',
    'Linda Davis (Mother)',
    '+1234567898',
    '2023-09-01',
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM users u WHERE u.username = 'student3';

-- Insert sample courses
INSERT IGNORE INTO courses (course_code, course_name, description, credits, teacher_id, start_date, end_date, schedule, classroom, max_students, course_status, created_at, updated_at)
SELECT
    'CS101',
    'Introduction to Programming',
    'Basic programming concepts using Java. Covers variables, control structures, methods, and object-oriented programming fundamentals.',
    3,
    t.id,
    '2024-01-15',
    '2024-05-15',
    'Monday, Wednesday, Friday 10:00-11:00',
    'Lab A-101',
    30,
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM teachers t WHERE t.employee_id = 'EMP001';

INSERT IGNORE INTO courses (course_code, course_name, description, credits, teacher_id, start_date, end_date, schedule, classroom, max_students, course_status, created_at, updated_at)
SELECT
    'MATH201',
    'Calculus I',
    'Differential and integral calculus. Limits, derivatives, applications of derivatives, and introduction to integration.',
    4,
    t.id,
    '2024-01-15',
    '2024-05-15',
    'Tuesday, Thursday 14:00-16:00',
    'Room B-205',
    25,
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM teachers t WHERE t.employee_id = 'EMP002';

INSERT IGNORE INTO courses (course_code, course_name, description, credits, teacher_id, start_date, end_date, schedule, classroom, max_students, course_status, created_at, updated_at)
SELECT
    'CS201',
    'Data Structures',
    'Advanced programming concepts including arrays, linked lists, stacks, queues, trees, and graphs.',
    3,
    t.id,
    '2024-01-15',
    '2024-05-15',
    'Monday, Wednesday 14:00-15:30',
    'Lab A-102',
    25,
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM teachers t WHERE t.employee_id = 'EMP001';

-- Insert sample enrollments
INSERT IGNORE INTO enrollments (student_id, course_id, enrollment_date, enrollment_status, created_at, updated_at)
SELECT
    s.id,
    c.id,
    '2024-01-10',
    'ENROLLED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM students s, courses c
WHERE s.student_id = 'STU001' AND c.course_code = 'CS101';

INSERT IGNORE INTO enrollments (student_id, course_id, enrollment_date, enrollment_status, created_at, updated_at)
SELECT
    s.id,
    c.id,
    '2024-01-10',
    'ENROLLED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM students s, courses c
WHERE s.student_id = 'STU001' AND c.course_code = 'MATH201';

INSERT IGNORE INTO enrollments (student_id, course_id, enrollment_date, enrollment_status, created_at, updated_at)
SELECT
    s.id,
    c.id,
    '2024-01-10',
    'ENROLLED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM students s, courses c
WHERE s.student_id = 'STU002' AND c.course_code = 'CS101';

INSERT IGNORE INTO enrollments (student_id, course_id, enrollment_date, enrollment_status, created_at, updated_at)
SELECT
    s.id,
    c.id,
    '2024-01-10',
    'ENROLLED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM students s, courses c
WHERE s.student_id = 'STU003' AND c.course_code = 'MATH201';

-- Insert sample grades
INSERT INTO grades (student_id, course_id, assignment_name, grade_type, grade_value, max_points, weight, grade_date, comments, is_extra_credit, is_dropped, created_at, updated_at)
SELECT
    s.id,
    c.id,
    'Assignment 1: Hello World',
    'ASSIGNMENT',
    85.0,
    100.0,
    0.1,
    '2024-02-01',
    'Good work on basic syntax',
    false,
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM students s, courses c
WHERE s.student_id = 'STU001' AND c.course_code = 'CS101';

INSERT INTO grades (student_id, course_id, assignment_name, grade_type, grade_value, max_points, weight, grade_date, comments, is_extra_credit, is_dropped, created_at, updated_at)
SELECT
    s.id,
    c.id,
    'Quiz 1: Limits',
    'QUIZ',
    92.0,
    100.0,
    0.15,
    '2024-02-05',
    'Excellent understanding of limits',
    false,
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM students s, courses c
WHERE s.student_id = 'STU001' AND c.course_code = 'MATH201';
