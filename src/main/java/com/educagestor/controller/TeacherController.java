package com.educagestor.controller;

import com.educagestor.dto.teacher.TeacherDto;
import com.educagestor.service.TeacherService;
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
 * REST Controller for teacher management operations
 * 
 * This controller handles teacher registration, profile management,
 * and teacher queries. It provides endpoints for CRUD operations on teachers.
 */
@RestController
@RequestMapping("/teachers")
@Tag(name = "Teacher Management", description = "Teacher registration and management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private TeacherService teacherService;

    /**
     * Registers a new teacher
     * 
     * @param teacherDto teacher registration data
     * @return created teacher information
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Register Teacher",
        description = "Creates a new teacher account with user profile (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Teacher registered successfully",
            content = @Content(schema = @Schema(implementation = TeacherDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or duplicate employee ID",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin role required",
            content = @Content
        )
    })
    public ResponseEntity<TeacherDto> registerTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        logger.info("Registering new teacher: {}", teacherDto.getEmployeeId());
        
        TeacherDto createdTeacher = teacherService.registerTeacher(teacherDto);
        
        return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
    }

    /**
     * Gets all teachers with pagination
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of teachers
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get All Teachers",
        description = "Retrieves all teachers with pagination (Admin/Teacher only)"
    )
    public ResponseEntity<Page<TeacherDto>> getAllTeachers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "employeeId") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Getting all teachers - page: {}, size: {}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<TeacherDto> teachers = teacherService.getAllTeachers(pageable);
        
        return ResponseEntity.ok(teachers);
    }

    /**
     * Gets teacher by ID
     * 
     * @param teacherId teacher ID
     * @return teacher information
     */
    @GetMapping("/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Teacher by ID",
        description = "Retrieves teacher information by ID (Admin/Teacher only)"
    )
    public ResponseEntity<TeacherDto> getTeacherById(
            @Parameter(description = "Teacher ID") @PathVariable Long teacherId) {
        logger.info("Getting teacher by ID: {}", teacherId);
        
        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        
        return ResponseEntity.ok(teacher);
    }

    /**
     * Gets teacher by employee ID
     * 
     * @param employeeId employee ID
     * @return teacher information
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Teacher by Employee ID",
        description = "Retrieves teacher information by employee ID (Admin/Teacher only)"
    )
    public ResponseEntity<TeacherDto> getTeacherByEmployeeId(
            @Parameter(description = "Employee ID") @PathVariable String employeeId) {
        logger.info("Getting teacher by employee ID: {}", employeeId);
        
        TeacherDto teacher = teacherService.getTeacherByEmployeeId(employeeId);
        
        return ResponseEntity.ok(teacher);
    }

    /**
     * Updates teacher information
     * 
     * @param teacherId teacher ID
     * @param teacherDto updated teacher data
     * @return updated teacher information
     */
    @PutMapping("/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update Teacher",
        description = "Updates teacher information (Admin only)"
    )
    public ResponseEntity<TeacherDto> updateTeacher(
            @Parameter(description = "Teacher ID") @PathVariable Long teacherId,
            @Valid @RequestBody TeacherDto teacherDto) {
        logger.info("Updating teacher with ID: {}", teacherId);
        
        TeacherDto updatedTeacher = teacherService.updateTeacher(teacherId, teacherDto);
        
        return ResponseEntity.ok(updatedTeacher);
    }

    /**
     * Gets teachers by department
     * 
     * @param department department name
     * @param page page number
     * @param size page size
     * @return page of teachers in department
     */
    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Teachers by Department",
        description = "Retrieves teachers by department (Admin/Teacher only)"
    )
    public ResponseEntity<Page<TeacherDto>> getTeachersByDepartment(
            @Parameter(description = "Department name") @PathVariable String department,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Getting teachers by department: {}", department);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("employeeId"));
        Page<TeacherDto> teachers = teacherService.getTeachersByDepartment(department, pageable);
        
        return ResponseEntity.ok(teachers);
    }

    /**
     * Searches teachers
     * 
     * @param searchTerm search term
     * @param page page number
     * @param size page size
     * @return page of matching teachers
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Search Teachers",
        description = "Searches teachers by name, employee ID, or department (Admin/Teacher only)"
    )
    public ResponseEntity<Page<TeacherDto>> searchTeachers(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Searching teachers with term: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("employeeId"));
        Page<TeacherDto> teachers = teacherService.searchTeachers(searchTerm, pageable);
        
        return ResponseEntity.ok(teachers);
    }
}
