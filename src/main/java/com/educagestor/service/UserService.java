package com.educagestor.service;

import com.educagestor.dto.user.UpdateUserProfileRequest;
import com.educagestor.dto.user.UserProfileDto;
import com.educagestor.entity.Role;
import com.educagestor.entity.User;
import com.educagestor.exception.ResourceNotFoundException;
import com.educagestor.repository.UserRepository;
import com.educagestor.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for user management operations
 * 
 * This service handles user profile management, user queries,
 * and user-related business logic operations.
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Gets the current authenticated user's profile
     * 
     * @return user profile DTO
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        logger.info("Fetching profile for current user: {}", userPrincipal.getUsername());
        
        User user = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        
        return convertToUserProfileDto(user);
    }

    /**
     * Updates the current authenticated user's profile
     * 
     * @param updateRequest profile update data
     * @return updated user profile DTO
     * @throws ResourceNotFoundException if user not found
     */
    public UserProfileDto updateCurrentUserProfile(UpdateUserProfileRequest updateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        logger.info("Updating profile for user: {}", userPrincipal.getUsername());
        
        User user = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        
        // Update user fields if provided
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().trim().isEmpty()) {
            user.setEmail(updateRequest.getEmail().trim());
        }
        
        if (updateRequest.getFirstName() != null && !updateRequest.getFirstName().trim().isEmpty()) {
            user.setFirstName(updateRequest.getFirstName().trim());
        }
        
        if (updateRequest.getLastName() != null && !updateRequest.getLastName().trim().isEmpty()) {
            user.setLastName(updateRequest.getLastName().trim());
        }
        
        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber().trim().isEmpty() ? null : updateRequest.getPhoneNumber().trim());
        }
        
        User updatedUser = userRepository.save(user);
        logger.info("Profile updated successfully for user: {}", updatedUser.getUsername());
        
        return convertToUserProfileDto(updatedUser);
    }

    /**
     * Gets user profile by ID (admin only)
     * 
     * @param userId user ID
     * @return user profile DTO
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserProfileDto getUserById(Long userId) {
        logger.info("Fetching user profile by ID: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return convertToUserProfileDto(user);
    }

    /**
     * Gets user profile by username (admin only)
     * 
     * @param username username
     * @return user profile DTO
     * @throws ResourceNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserProfileDto getUserByUsername(String username) {
        logger.info("Fetching user profile by username: {}", username);
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        return convertToUserProfileDto(user);
    }

    /**
     * Gets all users with pagination (admin only)
     * 
     * @param pageable pagination information
     * @return page of user profile DTOs
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDto> getAllUsers(Pageable pageable) {
        logger.info("Fetching all users with pagination");
        
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToUserProfileDto);
    }

    /**
     * Gets users by role with pagination (admin only)
     * 
     * @param role user role
     * @param pageable pagination information
     * @return page of user profile DTOs
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDto> getUsersByRole(Role role, Pageable pageable) {
        logger.info("Fetching users by role: {}", role);
        
        Page<User> users = userRepository.findByRole(role, pageable);
        return users.map(this::convertToUserProfileDto);
    }

    /**
     * Searches users by search term with pagination (admin only)
     * 
     * @param searchTerm search term
     * @param pageable pagination information
     * @return page of user profile DTOs
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDto> searchUsers(String searchTerm, Pageable pageable) {
        logger.info("Searching users with term: {}", searchTerm);
        
        Page<User> users = userRepository.findBySearchTerm(searchTerm, pageable);
        return users.map(this::convertToUserProfileDto);
    }

    /**
     * Gets users by active status with pagination (admin only)
     * 
     * @param active active status
     * @param pageable pagination information
     * @return page of user profile DTOs
     */
    @Transactional(readOnly = true)
    public Page<UserProfileDto> getUsersByActiveStatus(Boolean active, Pageable pageable) {
        logger.info("Fetching users by active status: {}", active);
        
        Page<User> users = userRepository.findByActive(active, pageable);
        return users.map(this::convertToUserProfileDto);
    }

    /**
     * Converts User entity to UserProfileDto
     * 
     * @param user User entity
     * @return UserProfileDto
     */
    private UserProfileDto convertToUserProfileDto(User user) {
        return new UserProfileDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getActive(),
            user.getRoles(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
