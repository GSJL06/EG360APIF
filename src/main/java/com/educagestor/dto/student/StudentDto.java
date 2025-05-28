package com.educagestor.dto.student;

import com.educagestor.dto.user.UserProfileDto;
import com.educagestor.entity.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for student information
 * 
 * This DTO represents complete student data including user information
 * and student-specific details.
 */
public class StudentDto {

    private Long id;
    private String studentId;
    private UserProfileDto user;
    private LocalDate dateOfBirth;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
    private LocalDate enrollmentDate;
    private Student.AcademicStatus academicStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public StudentDto() {}

    public StudentDto(Long id, String studentId, UserProfileDto user, LocalDate dateOfBirth,
                     String address, String emergencyContact, String emergencyPhone,
                     LocalDate enrollmentDate, Student.AcademicStatus academicStatus,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.enrollmentDate = enrollmentDate;
        this.academicStatus = academicStatus;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public UserProfileDto getUser() {
        return user;
    }

    public void setUser(UserProfileDto user) {
        this.user = user;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Student.AcademicStatus getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(Student.AcademicStatus academicStatus) {
        this.academicStatus = academicStatus;
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

    public int getAge() {
        return dateOfBirth != null ? LocalDate.now().getYear() - dateOfBirth.getYear() : 0;
    }
}
