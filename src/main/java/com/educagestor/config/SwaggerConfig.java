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
                    new Server().url("http://localhost:8080/api").description("Local Development Server")
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
                .description("Comprehensive Educational Management Platform API")
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
