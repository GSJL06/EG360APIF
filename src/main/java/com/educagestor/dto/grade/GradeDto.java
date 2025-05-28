package com.educagestor.dto.grade;

import com.educagestor.entity.Grade;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for grade information
 * 
 * This DTO represents grade data including student and course information.
 */
public class GradeDto {

    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;
    private String studentName;
    private String studentCode;

    @NotNull(message = "Course ID is required")
    private Long courseId;
    private String courseName;
    private String courseCode;

    @NotBlank(message = "Assignment name is required")
    @Size(max = 100, message = "Assignment name must not exceed 100 characters")
    private String assignmentName;

    @NotNull(message = "Grade type is required")
    private Grade.GradeType gradeType;

    @NotNull(message = "Grade value is required")
    @DecimalMin(value = "0.0", message = "Grade must be at least 0")
    @DecimalMax(value = "100.0", message = "Grade must not exceed 100")
    private Double gradeValue;

    @NotNull(message = "Maximum points is required")
    @DecimalMin(value = "0.1", message = "Maximum points must be greater than 0")
    private Double maxPoints = 100.0;

    @DecimalMin(value = "0.0", message = "Weight must be at least 0")
    @DecimalMax(value = "1.0", message = "Weight must not exceed 1")
    private Double weight;

    private LocalDate gradeDate;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    private String comments;

    private Boolean isExtraCredit = false;
    private Boolean isDropped = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public GradeDto() {}

    public GradeDto(Long id, Long studentId, String studentName, String studentCode,
                   Long courseId, String courseName, String courseCode, String assignmentName,
                   Grade.GradeType gradeType, Double gradeValue, Double maxPoints, Double weight,
                   LocalDate gradeDate, String comments, Boolean isExtraCredit, Boolean isDropped,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.assignmentName = assignmentName;
        this.gradeType = gradeType;
        this.gradeValue = gradeValue;
        this.maxPoints = maxPoints;
        this.weight = weight;
        this.gradeDate = gradeDate;
        this.comments = comments;
        this.isExtraCredit = isExtraCredit;
        this.isDropped = isDropped;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getAssignmentName() { return assignmentName; }
    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }

    public Grade.GradeType getGradeType() { return gradeType; }
    public void setGradeType(Grade.GradeType gradeType) { this.gradeType = gradeType; }

    public Double getGradeValue() { return gradeValue; }
    public void setGradeValue(Double gradeValue) { this.gradeValue = gradeValue; }

    public Double getMaxPoints() { return maxPoints; }
    public void setMaxPoints(Double maxPoints) { this.maxPoints = maxPoints; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public LocalDate getGradeDate() { return gradeDate; }
    public void setGradeDate(LocalDate gradeDate) { this.gradeDate = gradeDate; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public Boolean getIsExtraCredit() { return isExtraCredit; }
    public void setIsExtraCredit(Boolean isExtraCredit) { this.isExtraCredit = isExtraCredit; }

    public Boolean getIsDropped() { return isDropped; }
    public void setIsDropped(Boolean isDropped) { this.isDropped = isDropped; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Utility methods
    public Double getPercentage() {
        if (maxPoints == null || maxPoints == 0) return 0.0;
        return (gradeValue / maxPoints) * 100;
    }

    public String getLetterGrade() {
        Double percentage = getPercentage();
        if (percentage >= 90) return "A";
        if (percentage >= 80) return "B";
        if (percentage >= 70) return "C";
        if (percentage >= 60) return "D";
        return "F";
    }

    public boolean isPassing() { return getPercentage() >= 60; }
    public boolean isExtraCredit() { return Boolean.TRUE.equals(isExtraCredit); }
    public boolean isDropped() { return Boolean.TRUE.equals(isDropped); }
}
