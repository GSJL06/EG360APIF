package com.educagestor.dto.teacher;

import com.educagestor.dto.user.UserProfileDto;
import com.educagestor.entity.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for teacher information
 * 
 * This DTO represents complete teacher data including user information
 * and teacher-specific details.
 */
public class TeacherDto {

    private Long id;

    @NotBlank(message = "Employee ID is required")
    @Size(max = 20, message = "Employee ID must not exceed 20 characters")
    private String employeeId;

    @NotNull(message = "User information is required")
    private UserProfileDto user;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    @Size(max = 500, message = "Qualifications must not exceed 500 characters")
    private String qualifications;

    private LocalDate hireDate;

    @Size(max = 100, message = "Office location must not exceed 100 characters")
    private String officeLocation;

    @Size(max = 200, message = "Office hours must not exceed 200 characters")
    private String officeHours;

    private Teacher.EmploymentStatus employmentStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public TeacherDto() {}

    public TeacherDto(Long id, String employeeId, UserProfileDto user, String department,
                     String specialization, String qualifications, LocalDate hireDate,
                     String officeLocation, String officeHours, Teacher.EmploymentStatus employmentStatus,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.user = user;
        this.department = department;
        this.specialization = specialization;
        this.qualifications = qualifications;
        this.hireDate = hireDate;
        this.officeLocation = officeLocation;
        this.officeHours = officeHours;
        this.employmentStatus = employmentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public UserProfileDto getUser() {
        return user;
    }

    public void setUser(UserProfileDto user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public Teacher.EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(Teacher.EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility methods
    public String getFullName() {
        return user != null ? user.getFullName() : "";
    }

    public int getYearsOfService() {
        return hireDate != null ? LocalDate.now().getYear() - hireDate.getYear() : 0;
    }

    public boolean isActive() {
        return employmentStatus == Teacher.EmploymentStatus.ACTIVE;
    }
}
