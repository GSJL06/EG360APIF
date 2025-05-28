package com.educagestor.dto.enrollment;

import com.educagestor.entity.Enrollment;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for enrollment information
 * 
 * This DTO represents enrollment data including student and course information.
 */
public class EnrollmentDto {

    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;
    private String studentName;
    private String studentCode;

    @NotNull(message = "Course ID is required")
    private Long courseId;
    private String courseName;
    private String courseCode;

    private LocalDate enrollmentDate;
    private Enrollment.EnrollmentStatus enrollmentStatus;
    private LocalDate completionDate;
    private Double finalGrade;
    private String gradeLetter;
    private Integer creditsEarned;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public EnrollmentDto() {}

    public EnrollmentDto(Long id, Long studentId, String studentName, String studentCode,
                        Long courseId, String courseName, String courseCode,
                        LocalDate enrollmentDate, Enrollment.EnrollmentStatus enrollmentStatus,
                        LocalDate completionDate, Double finalGrade, String gradeLetter,
                        Integer creditsEarned, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
        this.enrollmentStatus = enrollmentStatus;
        this.completionDate = completionDate;
        this.finalGrade = finalGrade;
        this.gradeLetter = gradeLetter;
        this.creditsEarned = creditsEarned;
        this.notes = notes;
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

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public Enrollment.EnrollmentStatus getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(Enrollment.EnrollmentStatus enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }

    public Double getFinalGrade() { return finalGrade; }
    public void setFinalGrade(Double finalGrade) { this.finalGrade = finalGrade; }

    public String getGradeLetter() { return gradeLetter; }
    public void setGradeLetter(String gradeLetter) { this.gradeLetter = gradeLetter; }

    public Integer getCreditsEarned() { return creditsEarned; }
    public void setCreditsEarned(Integer creditsEarned) { this.creditsEarned = creditsEarned; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Utility methods
    public boolean isActive() { return enrollmentStatus == Enrollment.EnrollmentStatus.ENROLLED; }
    public boolean isCompleted() { return enrollmentStatus == Enrollment.EnrollmentStatus.COMPLETED; }
    public boolean hasGrade() { return finalGrade != null; }
    public boolean isPassing() { return finalGrade != null && finalGrade >= 60.0; }
}
