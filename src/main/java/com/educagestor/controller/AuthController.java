package com.educagestor.controller;

import com.educagestor.dto.auth.JwtResponse;
import com.educagestor.dto.auth.LoginRequest;
import com.educagestor.dto.auth.RefreshTokenRequest;
import com.educagestor.dto.auth.RegisterRequest;
import com.educagestor.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication operations
 * 
 * This controller handles user authentication, registration, and token management.
 * It provides endpoints for login, registration, and JWT token refresh.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Authenticates user and returns JWT tokens
     * 
     * @param loginRequest user credentials
     * @return JWT response with tokens and user information
     */
    @PostMapping("/login")
    @Operation(
        summary = "User Login",
        description = "Authenticates user credentials and returns JWT access token and refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid credentials",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed",
            content = @Content
        )
    })
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());
        
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        
        logger.info("Login successful for user: {}", loginRequest.getUsername());
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Registers a new user account
     * 
     * @param registerRequest user registration data
     * @return JWT response with tokens and user information
     */
    @PostMapping("/register")
    @Operation(
        summary = "User Registration",
        description = "Creates a new user account and returns JWT tokens for immediate authentication"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Registration successful",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Registration failed - validation errors or duplicate data",
            content = @Content
        )
    })
    public ResponseEntity<JwtResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Registration attempt for user: {}", registerRequest.getUsername());
        
        JwtResponse jwtResponse = authService.registerUser(registerRequest);
        
        logger.info("Registration successful for user: {}", registerRequest.getUsername());
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Refreshes JWT access token using refresh token
     * 
     * @param refreshTokenRequest refresh token
     * @return new JWT response with refreshed tokens
     */
    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh JWT Token",
        description = "Generates new access token and refresh token using a valid refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token refresh successful",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid or expired refresh token",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Refresh token validation failed",
            content = @Content
        )
    })
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        logger.info("Token refresh attempt");
        
        JwtResponse jwtResponse = authService.refreshToken(refreshTokenRequest);
        
        logger.info("Token refresh successful");
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Logs out user (client-side token invalidation)
     * 
     * @return success message
     */
    @PostMapping("/logout")
    @Operation(
        summary = "User Logout",
        description = "Logs out the current user. Note: JWT tokens are stateless, so logout is handled client-side by discarding tokens."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Logout successful",
            content = @Content
        )
    })
    public ResponseEntity<String> logoutUser() {
        logger.info("User logout");
        
        // Since JWT is stateless, logout is handled on the client side
        // by removing the token from storage
        return ResponseEntity.ok("User logged out successfully!");
    }
}
