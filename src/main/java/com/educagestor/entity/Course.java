package com.educagestor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Course entity representing academic courses in the educational system
 * 
 * This entity contains course information including code, name, description,
 * credits, schedule, and relationships with teachers and students.
 */
@Entity
@Table(name = "courses", indexes = {
    @Index(name = "idx_course_code", columnList = "course_code"),
    @Index(name = "idx_course_teacher", columnList = "teacher_id"),
    @Index(name = "idx_course_status", columnList = "course_status")
})
@EntityListeners(AuditingEntityListener.class)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code must not exceed 20 characters")
    @Column(name = "course_code", unique = true, nullable = false, length = 20)
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must not exceed 100 characters")
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 10, message = "Credits must not exceed 10")
    @Column(nullable = false)
    private Integer credits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Size(max = 100, message = "Schedule must not exceed 100 characters")
    @Column(length = 100)
    private String schedule;

    @Size(max = 50, message = "Classroom must not exceed 50 characters")
    @Column(length = 50)
    private String classroom;

    @NotNull(message = "Maximum students is required")
    @Min(value = 1, message = "Maximum students must be at least 1")
    @Max(value = 200, message = "Maximum students must not exceed 200")
    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_status", nullable = false)
    private CourseStatus courseStatus = CourseStatus.ACTIVE;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Course() {}

    public Course(String courseCode, String courseName, Integer credits, LocalDate startDate, LocalDate endDate) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }

    public CourseStatus getCourseStatus() { return courseStatus; }
    public void setCourseStatus(CourseStatus courseStatus) { this.courseStatus = courseStatus; }

    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }

    public List<Grade> getGrades() { return grades; }
    public void setGrades(List<Grade> grades) { this.grades = grades; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Utility methods
    public int getCurrentEnrollmentCount() {
        return (int) enrollments.stream()
                .filter(enrollment -> enrollment.getEnrollmentStatus() == Enrollment.EnrollmentStatus.ENROLLED)
                .count();
    }

    public boolean hasAvailableSpots() {
        return getCurrentEnrollmentCount() < maxStudents;
    }

    public boolean isActive() {
        return courseStatus == CourseStatus.ACTIVE;
    }

    /**
     * Course status enumeration
     */
    public enum CourseStatus {
        ACTIVE("Active"),
        INACTIVE("Inactive"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private final String displayName;

        CourseStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
