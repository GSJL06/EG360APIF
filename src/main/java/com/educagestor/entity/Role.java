package com.educagestor.entity;

/**
 * Enumeration representing user roles in the EducaGestor360 system
 * 
 * This enum defines the different types of users and their access levels:
 * - ADMIN: Full system access, can manage all entities
 * - TEACHER: Can manage courses, students, and grades for assigned courses
 * - STUDENT: Limited access, can view own information and grades
 */
public enum Role {
    /**
     * Administrator role with full system privileges
     * Can perform all CRUD operations on all entities
     */
    ADMIN("ROLE_ADMIN", "Administrator"),
    
    /**
     * Teacher role with course management privileges
     * Can manage students and grades for assigned courses
     */
    TEACHER("ROLE_TEACHER", "Teacher"),
    
    /**
     * Student role with limited read access
     * Can view own profile, courses, and grades
     */
    STUDENT("ROLE_STUDENT", "Student");

    private final String authority;
    private final String displayName;

    /**
     * Constructor for Role enum
     * 
     * @param authority the Spring Security authority string
     * @param displayName the human-readable display name
     */
    Role(String authority, String displayName) {
        this.authority = authority;
        this.displayName = displayName;
    }

    /**
     * Gets the Spring Security authority string
     * 
     * @return the authority string used by Spring Security
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * Gets the human-readable display name
     * 
     * @return the display name for UI purposes
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts string to Role enum
     * 
     * @param role the role string to convert
     * @return the corresponding Role enum
     * @throws IllegalArgumentException if role string is invalid
     */
    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role) || r.authority.equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
