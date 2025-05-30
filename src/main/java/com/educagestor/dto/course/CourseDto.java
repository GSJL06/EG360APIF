package com.educagestor.dto.course;

import com.educagestor.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for course information
 *
 * This DTO represents course data including details like code, name,
 * credits, teacher information, and status.
 */
public class CourseDto {

    private Long id;

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code must not exceed 20 characters")
    private String courseCode;

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name must not exceed 100 characters")
    private String courseName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Credits are required")
    private Integer credits;

    private Long teacherId;
    private String teacherName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
            private LocalDate endDate;

    @Size(max = 100, message = "Schedule must not exceed 100 characters")
    private String schedule;

    @Size(max = 50, message = "Classroom must not exceed 50 characters")
    private String classroom;

    @NotNull(message = "Maximum students is required")
    private Integer maxStudents;

    private Integer currentEnrollmentCount;
    private Course.CourseStatus courseStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CourseDto() {}

    public CourseDto(Long id, String courseCode, String courseName, String description, Integer credits,
                     Long teacherId, String teacherName, LocalDate startDate, LocalDate endDate,
                     String schedule, String classroom, Integer maxStudents, Integer currentEnrollmentCount,
                     Course.CourseStatus courseStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.schedule = schedule;
        this.classroom = classroom;
        this.maxStudents = maxStudents;
        this.currentEnrollmentCount = currentEnrollmentCount;
        this.courseStatus = courseStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

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

    public Integer getCurrentEnrollmentCount() { return currentEnrollmentCount; }
    public void setCurrentEnrollmentCount(Integer currentEnrollmentCount) { this.currentEnrollmentCount = currentEnrollmentCount; }

    public Course.CourseStatus getCourseStatus() { return courseStatus; }
    public void setCourseStatus(Course.CourseStatus courseStatus) { this.courseStatus = courseStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Utility methods
    public boolean hasAvailableSpots() {
        return currentEnrollmentCount != null && maxStudents != null && currentEnrollmentCount < maxStudents;
    }

    public boolean isActive() {
        return courseStatus == Course.CourseStatus.ACTIVE;
    }
}