package com.educagestor.repository;

import com.educagestor.entity.Role;
import com.educagestor.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations
 * 
 * This repository provides data access methods for User entities,
 * including custom queries for authentication and user management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds user by username
     * 
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds user by email address
     * 
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if username exists
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if email exists
     * 
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    Boolean existsByEmail(String email);

    /**
     * Finds users by role
     * 
     * @param role the role to search for
     * @param pageable pagination information
     * @return Page of users with the specified role
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    Page<User> findByRole(@Param("role") Role role, Pageable pageable);

    /**
     * Finds active users
     * 
     * @param active the active status
     * @param pageable pagination information
     * @return Page of users with the specified active status
     */
    Page<User> findByActive(Boolean active, Pageable pageable);

    /**
     * Finds users by first name or last name containing the search term
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return Page of users matching the search criteria
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Finds users by role and active status
     * 
     * @param role the role to search for
     * @param active the active status
     * @param pageable pagination information
     * @return Page of users matching the criteria
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role AND u.active = :active")
    Page<User> findByRoleAndActive(@Param("role") Role role, @Param("active") Boolean active, Pageable pageable);

    /**
     * Counts users by role
     * 
     * @param role the role to count
     * @return number of users with the specified role
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r = :role")
    Long countByRole(@Param("role") Role role);

    /**
     * Finds all users with multiple roles
     * 
     * @param roles the roles to search for
     * @return List of users having any of the specified roles
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r IN :roles")
    List<User> findByRolesIn(@Param("roles") List<Role> roles);
}
