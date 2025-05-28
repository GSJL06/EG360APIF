package com.educagestor.service;

import com.educagestor.dto.enrollment.EnrollmentDto;
import com.educagestor.entity.Course;
import com.educagestor.entity.Enrollment;
import com.educagestor.entity.Student;
import com.educagestor.exception.BadRequestException;
import com.educagestor.exception.ResourceNotFoundException;
import com.educagestor.repository.CourseRepository;
import com.educagestor.repository.EnrollmentRepository;
import com.educagestor.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service class for enrollment management operations
 * 
 * This service handles student course enrollments, enrollment status management,
 * and enrollment-related business logic operations.
 */
@Service
@Transactional
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Enrolls a student in a course
     * 
     * @param studentId student ID
     * @param courseId course ID
     * @return enrollment DTO
     * @throws BadRequestException if enrollment is not allowed
     * @throws ResourceNotFoundException if student or course not found
     */
    public EnrollmentDto enrollStudent(Long studentId, Long courseId) {
        logger.info("Enrolling student {} in course {}", studentId, courseId);

        // Validate student exists
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // Validate course exists
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        // Check if student is already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndEnrolled(studentId, courseId)) {
            throw new BadRequestException("Student is already enrolled in this course");
        }

        // Check if course has available spots
        if (!course.hasAvailableSpots()) {
            throw new BadRequestException("Course is full - no available spots");
        }

        // Check if course is active
        if (!course.isActive()) {
            throw new BadRequestException("Cannot enroll in inactive course");
        }

        // Check if student is active
        if (student.getAcademicStatus() != Student.AcademicStatus.ACTIVE) {
            throw new BadRequestException("Cannot enroll inactive student");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment(student, course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.ENROLLED);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        logger.info("Student {} enrolled successfully in course {}", studentId, courseId);

        return convertToEnrollmentDto(savedEnrollment);
    }

    /**
     * Gets enrollment by ID
     * 
     * @param enrollmentId enrollment ID
     * @return enrollment DTO
     * @throws ResourceNotFoundException if enrollment not found
     */
    @Transactional(readOnly = true)
    public EnrollmentDto getEnrollmentById(Long enrollmentId) {
        logger.info("Fetching enrollment by ID: {}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        return convertToEnrollmentDto(enrollment);
    }

    /**
     * Gets enrollments by student ID
     * 
     * @param studentId student ID
     * @param pageable pagination information
     * @return page of enrollment DTOs
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentDto> getEnrollmentsByStudentId(Long studentId, Pageable pageable) {
        logger.info("Fetching enrollments for student: {}", studentId);

        Page<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId, pageable);
        return enrollments.map(this::convertToEnrollmentDto);
    }

    /**
     * Gets enrollments by course ID
     * 
     * @param courseId course ID
     * @param pageable pagination information
     * @return page of enrollment DTOs
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentDto> getEnrollmentsByCourseId(Long courseId, Pageable pageable) {
        logger.info("Fetching enrollments for course: {}", courseId);

        Page<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId, pageable);
        return enrollments.map(this::convertToEnrollmentDto);
    }

    /**
     * Cancels an enrollment
     * 
     * @param enrollmentId enrollment ID
     * @throws ResourceNotFoundException if enrollment not found
     * @throws BadRequestException if enrollment cannot be cancelled
     */
    public void cancelEnrollment(Long enrollmentId) {
        logger.info("Cancelling enrollment: {}", enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        // Check if enrollment can be cancelled
        if (enrollment.getEnrollmentStatus() != Enrollment.EnrollmentStatus.ENROLLED) {
            throw new BadRequestException("Can only cancel active enrollments");
        }

        // Update enrollment status
        enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.WITHDRAWN);
        enrollment.setCompletionDate(LocalDate.now());

        enrollmentRepository.save(enrollment);
        logger.info("Enrollment cancelled successfully: {}", enrollmentId);
    }

    /**
     * Completes an enrollment with final grade
     * 
     * @param enrollmentId enrollment ID
     * @param finalGrade final grade
     * @return updated enrollment DTO
     * @throws ResourceNotFoundException if enrollment not found
     * @throws BadRequestException if enrollment cannot be completed
     */
    public EnrollmentDto completeEnrollment(Long enrollmentId, Double finalGrade) {
        logger.info("Completing enrollment {} with grade {}", enrollmentId, finalGrade);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        // Check if enrollment can be completed
        if (enrollment.getEnrollmentStatus() != Enrollment.EnrollmentStatus.ENROLLED) {
            throw new BadRequestException("Can only complete active enrollments");
        }

        // Validate grade
        if (finalGrade < 0 || finalGrade > 100) {
            throw new BadRequestException("Final grade must be between 0 and 100");
        }

        // Complete enrollment
        enrollment.complete(finalGrade);

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        logger.info("Enrollment completed successfully: {}", enrollmentId);

        return convertToEnrollmentDto(updatedEnrollment);
    }

    /**
     * Gets enrollments by status
     * 
     * @param status enrollment status
     * @param pageable pagination information
     * @return page of enrollment DTOs
     */
    @Transactional(readOnly = true)
    public Page<EnrollmentDto> getEnrollmentsByStatus(Enrollment.EnrollmentStatus status, Pageable pageable) {
        logger.info("Fetching enrollments by status: {}", status);

        Page<Enrollment> enrollments = enrollmentRepository.findByEnrollmentStatus(status, pageable);
        return enrollments.map(this::convertToEnrollmentDto);
    }

    /**
     * Converts Enrollment entity to EnrollmentDto
     * 
     * @param enrollment Enrollment entity
     * @return EnrollmentDto
     */
    private EnrollmentDto convertToEnrollmentDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getUser().getFullName());
        dto.setStudentCode(enrollment.getStudent().getStudentId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseName(enrollment.getCourse().getCourseName());
        dto.setCourseCode(enrollment.getCourse().getCourseCode());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setEnrollmentStatus(enrollment.getEnrollmentStatus());
        dto.setCompletionDate(enrollment.getCompletionDate());
        dto.setFinalGrade(enrollment.getFinalGrade());
        dto.setGradeLetter(enrollment.getGradeLetter());
        dto.setCreditsEarned(enrollment.getCreditsEarned());
        dto.setNotes(enrollment.getNotes());
        dto.setCreatedAt(enrollment.getCreatedAt());
        dto.setUpdatedAt(enrollment.getUpdatedAt());

        return dto;
    }
}
