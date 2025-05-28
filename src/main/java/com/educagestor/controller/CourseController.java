package com.educagestor.controller;

import com.educagestor.dto.course.CourseDto;
import com.educagestor.entity.Course;
import com.educagestor.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for course management operations
 * 
 * This controller handles course creation, management, teacher assignments,
 * and course queries. It provides endpoints for CRUD operations on courses.
 */
@RestController
@RequestMapping("/courses")
@Tag(name = "Course Management", description = "Course creation and management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    /**
     * Creates a new course
     * 
     * @param courseDto course creation data
     * @return created course information
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Create Course",
        description = "Creates a new course (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Course created successfully",
            content = @Content(schema = @Schema(implementation = CourseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or duplicate course code",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin or Teacher role required",
            content = @Content
        )
    })
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto) {
        logger.info("Creating new course: {}", courseDto.getCourseCode());
        
        CourseDto createdCourse = courseService.createCourse(courseDto);
        
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    /**
     * Gets all courses with pagination
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of courses
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(
        summary = "Get All Courses",
        description = "Retrieves all courses with pagination"
    )
    public ResponseEntity<Page<CourseDto>> getAllCourses(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "courseCode") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Getting all courses - page: {}, size: {}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CourseDto> courses = courseService.getAllCourses(pageable);
        
        return ResponseEntity.ok(courses);
    }

    /**
     * Gets course by ID
     * 
     * @param courseId course ID
     * @return course information
     */
    @GetMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(
        summary = "Get Course by ID",
        description = "Retrieves course information by ID"
    )
    public ResponseEntity<CourseDto> getCourseById(
            @Parameter(description = "Course ID") @PathVariable Long courseId) {
        logger.info("Getting course by ID: {}", courseId);
        
        CourseDto course = courseService.getCourseById(courseId);
        
        return ResponseEntity.ok(course);
    }

    /**
     * Updates course information
     * 
     * @param courseId course ID
     * @param courseDto updated course data
     * @return updated course information
     */
    @PutMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Update Course",
        description = "Updates course information (Admin/Teacher only)"
    )
    public ResponseEntity<CourseDto> updateCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Valid @RequestBody CourseDto courseDto) {
        logger.info("Updating course with ID: {}", courseId);
        
        CourseDto updatedCourse = courseService.updateCourse(courseId, courseDto);
        
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Assigns teacher to course
     * 
     * @param courseId course ID
     * @param teacherId teacher ID
     * @return updated course information
     */
    @PostMapping("/{courseId}/assign-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Assign Teacher to Course",
        description = "Assigns a teacher to a course (Admin only)"
    )
    public ResponseEntity<CourseDto> assignTeacherToCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Teacher ID") @RequestParam Long teacherId) {
        logger.info("Assigning teacher {} to course {}", teacherId, courseId);
        
        CourseDto updatedCourse = courseService.assignTeacherToCourse(courseId, teacherId);
        
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Gets courses by teacher ID
     * 
     * @param teacherId teacher ID
     * @param page page number
     * @param size page size
     * @return page of courses assigned to teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Courses by Teacher",
        description = "Retrieves courses assigned to a specific teacher"
    )
    public ResponseEntity<Page<CourseDto>> getCoursesByTeacher(
            @Parameter(description = "Teacher ID") @PathVariable Long teacherId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Getting courses for teacher: {}", teacherId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseCode"));
        Page<CourseDto> courses = courseService.getCoursesByTeacherId(teacherId, pageable);
        
        return ResponseEntity.ok(courses);
    }

    /**
     * Gets courses by status
     * 
     * @param status course status
     * @param page page number
     * @param size page size
     * @return page of courses with specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Courses by Status",
        description = "Retrieves courses by status"
    )
    public ResponseEntity<Page<CourseDto>> getCoursesByStatus(
            @Parameter(description = "Course status") @PathVariable Course.CourseStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Getting courses by status: {}", status);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseCode"));
        Page<CourseDto> courses = courseService.getCoursesByStatus(status, pageable);
        
        return ResponseEntity.ok(courses);
    }

    /**
     * Searches courses by search term
     * 
     * @param searchTerm search term
     * @param page page number
     * @param size page size
     * @return page of matching courses
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(
        summary = "Search Courses",
        description = "Searches courses by name or code"
    )
    public ResponseEntity<Page<CourseDto>> searchCourses(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Searching courses with term: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseCode"));
        Page<CourseDto> courses = courseService.searchCourses(searchTerm, pageable);
        
        return ResponseEntity.ok(courses);
    }

    /**
     * Gets courses with available spots
     * 
     * @param page page number
     * @param size page size
     * @return page of courses with available enrollment spots
     */
    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(
        summary = "Get Available Courses",
        description = "Retrieves courses with available enrollment spots"
    )
    public ResponseEntity<Page<CourseDto>> getAvailableCourses(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Getting courses with available spots");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("courseCode"));
        Page<CourseDto> courses = courseService.getCoursesWithAvailableSpots(pageable);
        
        return ResponseEntity.ok(courses);
    }
}
