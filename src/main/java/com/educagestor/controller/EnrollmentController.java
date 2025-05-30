package com.educagestor.controller;

import com.educagestor.dto.enrollment.EnrollmentDto;
import com.educagestor.entity.Enrollment;
import com.educagestor.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * REST Controller for enrollment management operations
 *
 * This controller handles student course enrollments, enrollment status management,
 * and enrollment queries. It provides endpoints for enrollment operations.
 */
@RestController
@RequestMapping("/enrollments")
@Tag(name = "Enrollment Management", description = "Student course enrollment endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * Enrolls a student in a course
     *
     * @param studentId student ID
     * @param courseId course ID
     * @return enrollment information
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Enroll Student in Course",
        description = "Enrolls a student in a course (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Student enrolled successfully",
            content = @Content(schema = @Schema(implementation = EnrollmentDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Enrollment not allowed - course full, student already enrolled, etc.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin or Teacher role required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Student or course not found",
            content = @Content
        )
    })
    public ResponseEntity<EnrollmentDto> enrollStudent(
            @Parameter(description = "Student ID") @RequestParam Long studentId,
            @Parameter(description = "Course ID") @RequestParam Long courseId) {
        logger.info("Enrolling student {} in course {}", studentId, courseId);

        EnrollmentDto enrollment = enrollmentService.enrollStudent(studentId, courseId);

        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
    }

    /**
     * Gets all enrollments with pagination
     *
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of enrollments
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get All Enrollments",
        description = "Retrieves all enrollments with pagination (Admin/Teacher only)"
    )
    public ResponseEntity<Page<EnrollmentDto>> getAllEnrollments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "enrollmentDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("Getting all enrollments - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EnrollmentDto> enrollments = enrollmentService.getAllEnrollments(pageable);

        return ResponseEntity.ok(enrollments);
    }

    /**
     * Gets enrollment by ID
     *
     * @param enrollmentId enrollment ID
     * @return enrollment information
     */
    @GetMapping("/{enrollmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Enrollment by ID",
        description = "Retrieves enrollment information by ID (Admin/Teacher only)"
    )
    public ResponseEntity<EnrollmentDto> getEnrollmentById(
            @Parameter(description = "Enrollment ID") @PathVariable Long enrollmentId) {
        logger.info("Getting enrollment by ID: {}", enrollmentId);

        EnrollmentDto enrollment = enrollmentService.getEnrollmentById(enrollmentId);

        return ResponseEntity.ok(enrollment);
    }

    /**
     * Gets enrollments by student ID
     *
     * @param studentId student ID
     * @param page page number
     * @param size page size
     * @return page of student enrollments
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @userService.getCurrentUserProfile().id == @studentRepository.findById(#studentId).orElse(new com.educagestor.entity.Student()).user.id)")
    @Operation(
        summary = "Get Student Enrollments",
        description = "Retrieves enrollments for a specific student"
    )
    public ResponseEntity<Page<EnrollmentDto>> getEnrollmentsByStudent(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        logger.info("Getting enrollments for student: {}", studentId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("enrollmentDate").descending());
        Page<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId, pageable);

        return ResponseEntity.ok(enrollments);
    }

    /**
     * Gets enrollments by course ID
     *
     * @param courseId course ID
     * @param page page number
     * @param size page size
     * @return page of course enrollments
     */
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Course Enrollments",
        description = "Retrieves enrollments for a specific course (Admin/Teacher only)"
    )
    public ResponseEntity<Page<EnrollmentDto>> getEnrollmentsByCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        logger.info("Getting enrollments for course: {}", courseId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("enrollmentDate").descending());
        Page<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId, pageable);

        return ResponseEntity.ok(enrollments);
    }

    /**
     * Cancels an enrollment
     *
     * @param enrollmentId enrollment ID
     * @return success message
     */
    @DeleteMapping("/{enrollmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Cancel Enrollment",
        description = "Cancels a student enrollment (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Enrollment cancelled successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Cannot cancel enrollment - invalid status",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin or Teacher role required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Enrollment not found",
            content = @Content
        )
    })
    public ResponseEntity<String> cancelEnrollment(
            @Parameter(description = "Enrollment ID") @PathVariable Long enrollmentId) {
        logger.info("Cancelling enrollment: {}", enrollmentId);

        enrollmentService.cancelEnrollment(enrollmentId);

        return ResponseEntity.ok("Enrollment cancelled successfully");
    }

    /**
     * Completes an enrollment with final grade
     *
     * @param enrollmentId enrollment ID
     * @param finalGrade final grade
     * @return updated enrollment information
     */
    @PostMapping("/{enrollmentId}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Complete Enrollment",
        description = "Completes an enrollment with final grade (Admin/Teacher only)"
    )
    public ResponseEntity<EnrollmentDto> completeEnrollment(
            @Parameter(description = "Enrollment ID") @PathVariable Long enrollmentId,
            @Parameter(description = "Final grade (0-100)") @RequestParam Double finalGrade) {
        logger.info("Completing enrollment {} with grade {}", enrollmentId, finalGrade);

        EnrollmentDto completedEnrollment = enrollmentService.completeEnrollment(enrollmentId, finalGrade);

        return ResponseEntity.ok(completedEnrollment);
    }

    /**
     * Gets enrollments by status
     *
     * @param status enrollment status
     * @param page page number
     * @param size page size
     * @return page of enrollments with specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Enrollments by Status",
        description = "Retrieves enrollments by status (Admin/Teacher only)"
    )
    public ResponseEntity<Page<EnrollmentDto>> getEnrollmentsByStatus(
            @Parameter(description = "Enrollment status") @PathVariable Enrollment.EnrollmentStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        logger.info("Getting enrollments by status: {}", status);

        Pageable pageable = PageRequest.of(page, size, Sort.by("enrollmentDate").descending());
        Page<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStatus(status, pageable);

        return ResponseEntity.ok(enrollments);
    }
}
