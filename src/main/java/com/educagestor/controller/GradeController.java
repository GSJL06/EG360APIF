package com.educagestor.controller;

import com.educagestor.dto.grade.GradeDto;
import com.educagestor.service.GradeService;
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
 * REST Controller for grade management operations
 *
 * This controller handles grade recording, grade management,
 * and grade queries. It provides endpoints for grade operations.
 */
@RestController
@RequestMapping("/grades")
@Tag(name = "Grade Management", description = "Grade recording and management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GradeController {

    private static final Logger logger = LoggerFactory.getLogger(GradeController.class);

    @Autowired
    private GradeService gradeService;

    /**
     * Records a new grade
     *
     * @param gradeDto grade data
     * @return created grade information
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Record Grade",
        description = "Records a new grade for a student (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Grade recorded successfully",
            content = @Content(schema = @Schema(implementation = GradeDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid grade data",
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
    public ResponseEntity<GradeDto> recordGrade(@Valid @RequestBody GradeDto gradeDto) {
        logger.info("Recording grade for student {} in course {}", gradeDto.getStudentId(), gradeDto.getCourseId());

        GradeDto createdGrade = gradeService.recordGrade(gradeDto);

        return new ResponseEntity<>(createdGrade, HttpStatus.CREATED);
    }

    /**
     * Gets all grades with pagination
     *
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of grades
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get All Grades",
        description = "Retrieves all grades with pagination (Admin/Teacher only)"
    )
    public ResponseEntity<Page<GradeDto>> getAllGrades(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "gradeDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("Getting all grades - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<GradeDto> grades = gradeService.getAllGrades(pageable);

        return ResponseEntity.ok(grades);
    }

    /**
     * Gets grade by ID
     *
     * @param gradeId grade ID
     * @return grade information
     */
    @GetMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Grade by ID",
        description = "Retrieves grade information by ID (Admin/Teacher only)"
    )
    public ResponseEntity<GradeDto> getGradeById(
            @Parameter(description = "Grade ID") @PathVariable Long gradeId) {
        logger.info("Getting grade by ID: {}", gradeId);

        GradeDto grade = gradeService.getGradeById(gradeId);

        return ResponseEntity.ok(grade);
    }

    /**
     * Gets grades by student ID
     *
     * @param studentId student ID
     * @param page page number
     * @param size page size
     * @return page of student grades
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @userService.getCurrentUserProfile().id == @studentRepository.findById(#studentId).orElse(new com.educagestor.entity.Student()).user.id)")
    @Operation(
        summary = "Get Student Grades",
        description = "Retrieves grades for a specific student"
    )
    public ResponseEntity<Page<GradeDto>> getGradesByStudent(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        logger.info("Getting grades for student: {}", studentId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("gradeDate").descending());
        Page<GradeDto> grades = gradeService.getGradesByStudentId(studentId, pageable);

        return ResponseEntity.ok(grades);
    }

    /**
     * Gets grades by course ID
     *
     * @param courseId course ID
     * @param page page number
     * @param size page size
     * @return page of course grades
     */
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Course Grades",
        description = "Retrieves grades for a specific course (Admin/Teacher only)"
    )
    public ResponseEntity<Page<GradeDto>> getGradesByCourse(
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        logger.info("Getting grades for course: {}", courseId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("gradeDate").descending());
        Page<GradeDto> grades = gradeService.getGradesByCourseId(courseId, pageable);

        return ResponseEntity.ok(grades);
    }

    /**
     * Gets grades by student and course
     *
     * @param studentId student ID
     * @param courseId course ID
     * @param page page number
     * @param size page size
     * @return page of grades for student in course
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @userService.getCurrentUserProfile().id == @studentRepository.findById(#studentId).orElse(new com.educagestor.entity.Student()).user.id)")
    @Operation(
        summary = "Get Student Grades in Course",
        description = "Retrieves grades for a specific student in a specific course"
    )
    public ResponseEntity<Page<GradeDto>> getGradesByStudentAndCourse(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Course ID") @PathVariable Long courseId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        logger.info("Getting grades for student {} in course {}", studentId, courseId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("gradeDate").descending());
        Page<GradeDto> grades = gradeService.getGradesByStudentAndCourse(studentId, courseId, pageable);

        return ResponseEntity.ok(grades);
    }

    /**
     * Updates a grade
     *
     * @param gradeId grade ID
     * @param gradeDto updated grade data
     * @return updated grade information
     */
    @PutMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Update Grade",
        description = "Updates grade information (Admin/Teacher only)"
    )
    public ResponseEntity<GradeDto> updateGrade(
            @Parameter(description = "Grade ID") @PathVariable Long gradeId,
            @Valid @RequestBody GradeDto gradeDto) {
        logger.info("Updating grade: {}", gradeId);

        GradeDto updatedGrade = gradeService.updateGrade(gradeId, gradeDto);

        return ResponseEntity.ok(updatedGrade);
    }

    /**
     * Deletes a grade
     *
     * @param gradeId grade ID
     * @return success message
     */
    @DeleteMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Delete Grade",
        description = "Deletes a grade (Admin/Teacher only)"
    )
    public ResponseEntity<String> deleteGrade(
            @Parameter(description = "Grade ID") @PathVariable Long gradeId) {
        logger.info("Deleting grade: {}", gradeId);

        gradeService.deleteGrade(gradeId);

        return ResponseEntity.ok("Grade deleted successfully");
    }

    /**
     * Calculates average grade for a student in a course
     *
     * @param studentId student ID
     * @param courseId course ID
     * @return average grade
     */
    @GetMapping("/student/{studentId}/course/{courseId}/average")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @userService.getCurrentUserProfile().id == @studentRepository.findById(#studentId).orElse(new com.educagestor.entity.Student()).user.id)")
    @Operation(
        summary = "Calculate Average Grade",
        description = "Calculates average grade for a student in a course"
    )
    public ResponseEntity<Double> calculateAverageGrade(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Calculating average grade for student {} in course {}", studentId, courseId);

        Double averageGrade = gradeService.calculateAverageGrade(studentId, courseId);

        return ResponseEntity.ok(averageGrade);
    }

    /**
     * Calculates weighted average grade for a student in a course
     *
     * @param studentId student ID
     * @param courseId course ID
     * @return weighted average grade
     */
    @GetMapping("/student/{studentId}/course/{courseId}/weighted-average")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @userService.getCurrentUserProfile().id == @studentRepository.findById(#studentId).orElse(new com.educagestor.entity.Student()).user.id)")
    @Operation(
        summary = "Calculate Weighted Average Grade",
        description = "Calculates weighted average grade for a student in a course"
    )
    public ResponseEntity<Double> calculateWeightedAverageGrade(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Parameter(description = "Course ID") @PathVariable Long courseId) {

        logger.info("Calculating weighted average grade for student {} in course {}", studentId, courseId);

        Double weightedAverageGrade = gradeService.calculateWeightedAverageGrade(studentId, courseId);

        return ResponseEntity.ok(weightedAverageGrade);
    }
}
