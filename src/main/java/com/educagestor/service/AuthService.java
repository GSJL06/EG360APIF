package com.educagestor.service;

import com.educagestor.dto.auth.JwtResponse;
import com.educagestor.dto.auth.LoginRequest;
import com.educagestor.dto.auth.RefreshTokenRequest;
import com.educagestor.dto.auth.RegisterRequest;
import com.educagestor.entity.Role;
import com.educagestor.entity.User;
import com.educagestor.exception.BadRequestException;
import com.educagestor.repository.UserRepository;
import com.educagestor.security.JwtUtils;
import com.educagestor.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for authentication operations
 * 
 * This service handles user authentication, registration, and JWT token management.
 * It provides secure login/logout functionality and user account creation.
 */
@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Authenticates user and returns JWT tokens
     * 
     * @param loginRequest login credentials
     * @return JWT response with tokens and user info
     * @throws BadRequestException if credentials are invalid
     */
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(userPrincipal.getUsername());

            logger.info("User authenticated successfully: {}", userPrincipal.getUsername());

            return new JwtResponse(
                jwt,
                refreshToken,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                userPrincipal.getFirstName(),
                userPrincipal.getLastName(),
                userRepository.findByUsername(userPrincipal.getUsername())
                    .orElseThrow(() -> new BadRequestException("User not found"))
                    .getRoles()
            );

        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            throw new BadRequestException("Invalid username or password");
        }
    }

    /**
     * Registers a new user account
     * 
     * @param registerRequest user registration data
     * @return JWT response with tokens and user info
     * @throws BadRequestException if registration data is invalid
     */
    public JwtResponse registerUser(RegisterRequest registerRequest) {
        logger.info("Attempting to register new user: {}", registerRequest.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            logger.warn("Registration failed - username already exists: {}", registerRequest.getUsername());
            throw new BadRequestException("Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.warn("Registration failed - email already exists: {}", registerRequest.getEmail());
            throw new BadRequestException("Email is already in use!");
        }

        // Validate roles
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            logger.warn("Registration failed - no roles specified for user: {}", registerRequest.getUsername());
            throw new BadRequestException("At least one role must be specified!");
        }

        // Create new user
        User user = new User(
            registerRequest.getUsername(),
            registerRequest.getEmail(),
            passwordEncoder.encode(registerRequest.getPassword()),
            registerRequest.getFirstName(),
            registerRequest.getLastName()
        );

        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRoles(registerRequest.getRoles());

        try {
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getUsername());

            // Authenticate the newly registered user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    registerRequest.getUsername(),
                    registerRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(savedUser.getUsername());

            return new JwtResponse(
                jwt,
                refreshToken,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRoles()
            );

        } catch (Exception e) {
            logger.error("Registration failed for user: {}", registerRequest.getUsername(), e);
            throw new BadRequestException("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Refreshes JWT token using refresh token
     * 
     * @param refreshTokenRequest refresh token request
     * @return new JWT response with refreshed tokens
     * @throws BadRequestException if refresh token is invalid
     */
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        
        logger.info("Attempting to refresh token");

        try {
            if (jwtUtils.validateJwtToken(refreshToken)) {
                String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
                User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BadRequestException("User not found"));

                String newJwt = jwtUtils.generateTokenFromUsername(username);
                String newRefreshToken = jwtUtils.generateRefreshToken(username);

                logger.info("Token refreshed successfully for user: {}", username);

                return new JwtResponse(
                    newJwt,
                    newRefreshToken,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRoles()
                );
            } else {
                logger.warn("Invalid refresh token provided");
                throw new BadRequestException("Invalid refresh token");
            }
        } catch (Exception e) {
            logger.error("Token refresh failed", e);
            throw new BadRequestException("Token refresh failed: " + e.getMessage());
        }
    }

    /**
     * Validates if a role can be assigned during registration
     * 
     * @param role the role to validate
     * @return true if role can be assigned
     */
    private boolean isValidRoleForRegistration(Role role) {
        // In a real application, you might want to restrict certain roles
        // For now, we allow all roles but you could add business logic here
        return role != null;
    }
}
