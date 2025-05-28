package com.educagestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for EducaGestor360 API
 * 
 * This is a comprehensive educational management platform that provides
 * RESTful APIs for managing students, teachers, courses, enrollments, and grades.
 * 
 * Features:
 * - JWT-based authentication and authorization
 * - Role-based access control (ADMIN, TEACHER, STUDENT)
 * - Complete CRUD operations for all entities
 * - Swagger/OpenAPI documentation
 * - PostgreSQL database integration
 * 
 * @author EducaGestor360 Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class EducaGestorApiApplication {

    /**
     * Main method to start the Spring Boot application
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EducaGestorApiApplication.class, args);
    }
}
