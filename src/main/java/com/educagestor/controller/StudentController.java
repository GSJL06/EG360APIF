package com.educagestor.controller;

import com.educagestor.dto.student.StudentDto;
import com.educagestor.entity.Student;
import com.educagestor.service.StudentService;
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
 * REST Controller for student management operations
 * 
 * This controller handles student registration, profile management,
 * and student queries. It provides endpoints for CRUD operations on students.
 */
@RestController
@RequestMapping("/students")
@Tag(name = "Student Management", description = "Student registration and management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    /**
     * Registers a new student
     * 
     * @param studentDto student registration data
     * @return created student information
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Register Student",
        description = "Creates a new student account with user profile (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Student registered successfully",
            content = @Content(schema = @Schema(implementation = StudentDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or duplicate student ID",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin or Teacher role required",
            content = @Content
        )
    })
    public ResponseEntity<StudentDto> registerStudent(@Valid @RequestBody StudentDto studentDto) {
        logger.info("Registering new student: {}", studentDto.getStudentId());
        
        StudentDto createdStudent = studentService.registerStudent(studentDto);
        
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    /**
     * Gets all students with pagination
     * 
     * @param page page number (0-based)
     * @param size page size
     * @param sortBy sort field
     * @param sortDir sort direction
     * @return page of students
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get All Students",
        description = "Retrieves all students with pagination (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Students retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin or Teacher role required",
            content = @Content
        )
    })
    public ResponseEntity<Page<StudentDto>> getAllStudents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "studentId") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("Getting all students - page: {}, size: {}", page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<StudentDto> students = studentService.getAllStudents(pageable);
        
        return ResponseEntity.ok(students);
    }

    /**
     * Gets student by ID
     * 
     * @param studentId student ID
     * @return student information
     */
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Student by ID",
        description = "Retrieves student information by ID (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Student retrieved successfully",
            content = @Content(schema = @Schema(implementation = StudentDto.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin or Teacher role required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
        )
    })
    public ResponseEntity<StudentDto> getStudentById(
            @Parameter(description = "Student ID") @PathVariable Long studentId) {
        logger.info("Getting student by ID: {}", studentId);
        
        StudentDto student = studentService.getStudentById(studentId);
        
        return ResponseEntity.ok(student);
    }

    /**
     * Gets student by student ID
     * 
     * @param studentId student ID
     * @return student information
     */
    @GetMapping("/student-id/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Student by Student ID",
        description = "Retrieves student information by student ID (Admin/Teacher only)"
    )
    public ResponseEntity<StudentDto> getStudentByStudentId(
            @Parameter(description = "Student ID") @PathVariable String studentId) {
        logger.info("Getting student by student ID: {}", studentId);
        
        StudentDto student = studentService.getStudentByStudentId(studentId);
        
        return ResponseEntity.ok(student);
    }

    /**
     * Updates student information
     * 
     * @param studentId student ID
     * @param studentDto updated student data
     * @return updated student information
     */
    @PutMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Update Student",
        description = "Updates student information (Admin/Teacher only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Student updated successfully",
            content = @Content(schema = @Schema(implementation = StudentDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin or Teacher role required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
        )
    })
    public ResponseEntity<StudentDto> updateStudent(
            @Parameter(description = "Student ID") @PathVariable Long studentId,
            @Valid @RequestBody StudentDto studentDto) {
        logger.info("Updating student with ID: {}", studentId);
        
        StudentDto updatedStudent = studentService.updateStudent(studentId, studentDto);
        
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Deletes a student (deactivates)
     * 
     * @param studentId student ID
     * @return success message
     */
    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete Student",
        description = "Deactivates a student account (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Student deactivated successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - Admin role required",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Student not found",
            content = @Content
        )
    })
    public ResponseEntity<String> deleteStudent(
            @Parameter(description = "Student ID") @PathVariable Long studentId) {
        logger.info("Deleting student with ID: {}", studentId);
        
        studentService.deleteStudent(studentId);
        
        return ResponseEntity.ok("Student deactivated successfully");
    }

    /**
     * Searches students by search term
     * 
     * @param searchTerm search term
     * @param page page number
     * @param size page size
     * @return page of matching students
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Search Students",
        description = "Searches students by name or student ID (Admin/Teacher only)"
    )
    public ResponseEntity<Page<StudentDto>> searchStudents(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Searching students with term: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("studentId"));
        Page<StudentDto> students = studentService.searchStudents(searchTerm, pageable);
        
        return ResponseEntity.ok(students);
    }

    /**
     * Gets students by academic status
     * 
     * @param academicStatus academic status
     * @param page page number
     * @param size page size
     * @return page of students with specified status
     */
    @GetMapping("/status/{academicStatus}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
        summary = "Get Students by Academic Status",
        description = "Retrieves students by academic status (Admin/Teacher only)"
    )
    public ResponseEntity<Page<StudentDto>> getStudentsByAcademicStatus(
            @Parameter(description = "Academic status") @PathVariable Student.AcademicStatus academicStatus,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Getting students by academic status: {}", academicStatus);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("studentId"));
        Page<StudentDto> students = studentService.getStudentsByAcademicStatus(academicStatus, pageable);
        
        return ResponseEntity.ok(students);
    }
}
