package com.educagestor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Grade entity representing individual grades for students in courses
 * 
 * This entity tracks individual assignments, exams, and other graded
 * activities within a course, allowing for detailed academic progress monitoring.
 */
@Entity
@Table(name = "grades", indexes = {
    @Index(name = "idx_grade_student", columnList = "student_id"),
    @Index(name = "idx_grade_course", columnList = "course_id"),
    @Index(name = "idx_grade_type", columnList = "grade_type"),
    @Index(name = "idx_grade_date", columnList = "grade_date")
})
@EntityListeners(AuditingEntityListener.class)
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Assignment name is required")
    @Size(max = 100, message = "Assignment name must not exceed 100 characters")
    @Column(name = "assignment_name", nullable = false, length = 100)
    private String assignmentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type", nullable = false)
    private GradeType gradeType;

    @NotNull(message = "Grade value is required")
    @DecimalMin(value = "0.0", message = "Grade must be at least 0")
    @DecimalMax(value = "100.0", message = "Grade must not exceed 100")
    @Column(name = "grade_value", nullable = false)
    private Double gradeValue;

    @NotNull(message = "Maximum points is required")
    @DecimalMin(value = "0.1", message = "Maximum points must be greater than 0")
    @Column(name = "max_points", nullable = false)
    private Double maxPoints = 100.0;

    @DecimalMin(value = "0.0", message = "Weight must be at least 0")
    @DecimalMax(value = "1.0", message = "Weight must not exceed 1")
    @Column(name = "weight")
    private Double weight;

    @NotNull(message = "Grade date is required")
    @Column(name = "grade_date", nullable = false)
    private LocalDate gradeDate;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    @Column(length = 500)
    private String comments;

    @Column(name = "is_extra_credit")
    private Boolean isExtraCredit = false;

    @Column(name = "is_dropped")
    private Boolean isDropped = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Grade() {
        this.gradeDate = LocalDate.now();
    }

    public Grade(Student student, Course course, String assignmentName, GradeType gradeType, Double gradeValue) {
        this();
        this.student = student;
        this.course = course;
        this.assignmentName = assignmentName;
        this.gradeType = gradeType;
        this.gradeValue = gradeValue;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getAssignmentName() { return assignmentName; }
    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }

    public GradeType getGradeType() { return gradeType; }
    public void setGradeType(GradeType gradeType) { this.gradeType = gradeType; }

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

    public boolean isPassing() {
        return getPercentage() >= 60;
    }

    /**
     * Grade type enumeration for different types of assessments
     */
    public enum GradeType {
        ASSIGNMENT("Assignment"),
        QUIZ("Quiz"),
        EXAM("Exam"),
        PROJECT("Project"),
        PARTICIPATION("Participation"),
        HOMEWORK("Homework"),
        LAB("Lab"),
        FINAL_EXAM("Final Exam"),
        MIDTERM("Midterm"),
        OTHER("Other");

        private final String displayName;

        GradeType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
