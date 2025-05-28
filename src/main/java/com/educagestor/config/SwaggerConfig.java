package com.educagestor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration for the EducaGestor360 API
 * 
 * This configuration sets up comprehensive API documentation with
 * JWT authentication support and detailed endpoint descriptions.
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * Configures OpenAPI documentation
     * 
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server().url("http://localhost:" + serverPort + "/api").description("Local Development Server"),
                    new Server().url("https://api.educagestor360.com/api").description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                    .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * Creates API information
     * 
     * @return Info object with API details
     */
    private Info apiInfo() {
        return new Info()
                .title("EducaGestor360 API")
                .description("Comprehensive Educational Management Platform API\n\n" +
                           "## Overview\n" +
                           "EducaGestor360 is a complete educational management system that provides RESTful APIs for:\n" +
                           "- User authentication and authorization\n" +
                           "- Student management and academic tracking\n" +
                           "- Teacher management and course assignments\n" +
                           "- Course creation and enrollment management\n" +
                           "- Grade recording and academic analytics\n\n" +
                           "## Authentication\n" +
                           "This API uses JWT (JSON Web Token) for authentication. To access protected endpoints:\n" +
                           "1. Login using `/auth/login` endpoint\n" +
                           "2. Include the JWT token in the Authorization header: `Bearer <token>`\n\n" +
                           "## User Roles\n" +
                           "- **ADMIN**: Full system access, can manage all entities\n" +
                           "- **TEACHER**: Can manage courses, students, and grades for assigned courses\n" +
                           "- **STUDENT**: Limited access, can view own information and grades\n\n" +
                           "## Error Handling\n" +
                           "The API returns consistent error responses with appropriate HTTP status codes:\n" +
                           "- 400: Bad Request (validation errors)\n" +
                           "- 401: Unauthorized (authentication required)\n" +
                           "- 403: Forbidden (insufficient permissions)\n" +
                           "- 404: Not Found (resource doesn't exist)\n" +
                           "- 500: Internal Server Error")
                .version("1.0.0")
                .contact(new Contact()
                    .name("EducaGestor360 Development Team")
                    .email("dev@educagestor360.com")
                    .url("https://educagestor360.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"));
    }

    /**
     * Creates JWT security scheme
     * 
     * @return SecurityScheme for JWT authentication
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT token obtained from login endpoint");
    }
}
