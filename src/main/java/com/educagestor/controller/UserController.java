package com.educagestor.controller;

import com.educagestor.dto.user.UpdateUserProfileRequest;
import com.educagestor.dto.user.UserProfileDto;
import com.educagestor.entity.Role;
import com.educagestor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for user management operations
 * 
 * This controller handles user profile management and user queries.
 * It provides endpoints for viewing and updating user information.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "User profile and management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Gets the current authenticated user's profile
     * 
     * @return user profile information
     */
    @GetMapping("/profile")
    @Operation(
        summary = "Get Current User Profile",
        description = "Retrieves the profile information of the currently authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<UserProfileDto> getCurrentUserProfile() {
        logger.info("Getting current user profile");
        
        UserProfileDto userProfile = userService.getCurrentUserProfile();
        
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Updates the current authenticated user's profile
     * 
     * @param updateRequest profile update data
     * @return updated user profile information
     */
    @PutMapping("/profile")
    @Operation(
        summary = "Update Current User Profile",
        description = "Updates the profile information of the currently authenticated user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile updated successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content
        )
    })
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            @Valid @RequestBody UpdateUserProfileRequest updateRequest) {
        logger.info("Updating current user profile");
        
        UserProfileDto updatedProfile = userService.updateCurrentUserProfile(updateRequest);
        
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Gets user by ID (admin only)
     * 
     * @param userId user ID
     * @return user profile information
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get User by ID",
        description = "Retrieves user profile by ID (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin role required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<UserProfileDto> getUserById(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        logger.info("Getting user by ID: {}", userId);
        
        UserProfileDto userProfile = userService.getUserById(userId);
        
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Gets user by username (admin only)
     * 
     * @param username username
     * @return user profile information
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get User by Username",
        description = "Retrieves user profile by username (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin role required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
        )
    })
    public ResponseEntity<UserProfileDto> getUserByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        logger.info("Getting user by username: {}", username);
        
        UserProfileDto userProfile = userService.getUserByUsername(username);
        
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Gets all users with pagination (admin only)
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get All Users",
        description = "Retrieves all users with pagination (Admin only)"
    )
    public ResponseEntity<Page<UserProfileDto>> getAllUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "username") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Getting all users - page: {}, size: {}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserProfileDto> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(users);
    }

    /**
     * Gets users by role with pagination (admin only)
     * 
     * @param role user role
     * @param page page number
     * @param size page size
     * @return page of users with specified role
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get Users by Role",
        description = "Retrieves users by role with pagination (Admin only)"
    )
    public ResponseEntity<Page<UserProfileDto>> getUsersByRole(
            @Parameter(description = "User role") @PathVariable Role role,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Getting users by role: {}", role);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("username"));
        Page<UserProfileDto> users = userService.getUsersByRole(role, pageable);
        
        return ResponseEntity.ok(users);
    }
}
