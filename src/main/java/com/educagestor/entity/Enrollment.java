package com.educagestor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Enrollment entity representing student enrollments in courses
 * 
 * This entity manages the many-to-many relationship between students and courses,
 * including enrollment status, dates, and academic progress tracking.
 */
@Entity
@Table(name = "enrollments", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}),
       indexes = {
           @Index(name = "idx_enrollment_student", columnList = "student_id"),
           @Index(name = "idx_enrollment_course", columnList = "course_id"),
           @Index(name = "idx_enrollment_status", columnList = "enrollment_status")
       })
@EntityListeners(AuditingEntityListener.class)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "Enrollment date is required")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false)
    private EnrollmentStatus enrollmentStatus = EnrollmentStatus.ENROLLED;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "final_grade")
    private Double finalGrade;

    @Column(name = "grade_letter", length = 2)
    private String gradeLetter;

    @Column(name = "credits_earned")
    private Integer creditsEarned;

    @Column(name = "notes", length = 500)
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Enrollment() {
        this.enrollmentDate = LocalDate.now();
    }

    public Enrollment(Student student, Course course) {
        this();
        this.student = student;
        this.course = course;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public EnrollmentStatus getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

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
    public boolean isActive() {
        return enrollmentStatus == EnrollmentStatus.ENROLLED;
    }

    public boolean isCompleted() {
        return enrollmentStatus == EnrollmentStatus.COMPLETED;
    }

    public void complete(Double finalGrade) {
        this.enrollmentStatus = EnrollmentStatus.COMPLETED;
        this.finalGrade = finalGrade;
        this.completionDate = LocalDate.now();
        this.creditsEarned = course.getCredits();
        this.gradeLetter = calculateGradeLetter(finalGrade);
    }

    public void withdraw() {
        this.enrollmentStatus = EnrollmentStatus.WITHDRAWN;
        this.completionDate = LocalDate.now();
    }

    private String calculateGradeLetter(Double grade) {
        if (grade == null) return null;
        if (grade >= 90) return "A";
        if (grade >= 80) return "B";
        if (grade >= 70) return "C";
        if (grade >= 60) return "D";
        return "F";
    }

    /**
     * Enrollment status enumeration
     */
    public enum EnrollmentStatus {
        ENROLLED("Enrolled"),
        COMPLETED("Completed"),
        WITHDRAWN("Withdrawn"),
        DROPPED("Dropped"),
        FAILED("Failed");

        private final String displayName;

        EnrollmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
