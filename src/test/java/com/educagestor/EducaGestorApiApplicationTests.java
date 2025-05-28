package com.educagestor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test for the EducaGestor360 API application
 * 
 * This test verifies that the Spring Boot application context loads successfully
 * and all components are properly configured.
 */
@SpringBootTest
@ActiveProfiles("test")
class EducaGestorApiApplicationTests {

    /**
     * Test that the application context loads successfully
     */
    @Test
    void contextLoads() {
        // This test will pass if the application context loads without errors
        // It validates that all beans are properly configured and dependencies are satisfied
    }
}
