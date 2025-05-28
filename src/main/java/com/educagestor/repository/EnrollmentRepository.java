package com.educagestor.repository;

import com.educagestor.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Enrollment entity operations
 * 
 * This repository provides data access methods for Enrollment entities,
 * including custom queries for enrollment management and academic tracking.
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Finds enrollment by student and course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @return Optional containing the enrollment if found
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    Optional<Enrollment> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * Finds enrollments by student ID
     * 
     * @param studentId the student ID
     * @param pageable pagination information
     * @return Page of enrollments for the specified student
     */
    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);

    /**
     * Finds enrollments by course ID
     * 
     * @param courseId the course ID
     * @param pageable pagination information
     * @return Page of enrollments for the specified course
     */
    Page<Enrollment> findByCourseId(Long courseId, Pageable pageable);

    /**
     * Finds enrollments by status
     * 
     * @param enrollmentStatus the enrollment status
     * @param pageable pagination information
     * @return Page of enrollments with the specified status
     */
    Page<Enrollment> findByEnrollmentStatus(Enrollment.EnrollmentStatus enrollmentStatus, Pageable pageable);

    /**
     * Finds enrollments by student and status
     * 
     * @param studentId the student ID
     * @param enrollmentStatus the enrollment status
     * @param pageable pagination information
     * @return Page of enrollments matching the criteria
     */
    Page<Enrollment> findByStudentIdAndEnrollmentStatus(Long studentId, Enrollment.EnrollmentStatus enrollmentStatus, Pageable pageable);

    /**
     * Finds enrollments by course and status
     * 
     * @param courseId the course ID
     * @param enrollmentStatus the enrollment status
     * @param pageable pagination information
     * @return Page of enrollments matching the criteria
     */
    Page<Enrollment> findByCourseIdAndEnrollmentStatus(Long courseId, Enrollment.EnrollmentStatus enrollmentStatus, Pageable pageable);

    /**
     * Finds enrollments between dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return Page of enrollments within the date range
     */
    Page<Enrollment> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Checks if student is enrolled in course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @return true if student is enrolled in the course
     */
    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId AND e.enrollmentStatus = 'ENROLLED'")
    Boolean existsByStudentIdAndCourseIdAndEnrolled(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * Counts enrollments by course and status
     * 
     * @param courseId the course ID
     * @param enrollmentStatus the enrollment status
     * @return number of enrollments matching the criteria
     */
    Long countByCourseIdAndEnrollmentStatus(Long courseId, Enrollment.EnrollmentStatus enrollmentStatus);

    /**
     * Counts enrollments by student and status
     * 
     * @param studentId the student ID
     * @param enrollmentStatus the enrollment status
     * @return number of enrollments matching the criteria
     */
    Long countByStudentIdAndEnrollmentStatus(Long studentId, Enrollment.EnrollmentStatus enrollmentStatus);

    /**
     * Finds active enrollments for a student
     * 
     * @param studentId the student ID
     * @return List of active enrollments
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.enrollmentStatus = 'ENROLLED'")
    List<Enrollment> findActiveEnrollmentsByStudentId(@Param("studentId") Long studentId);

    /**
     * Finds completed enrollments with grades
     * 
     * @param studentId the student ID
     * @param pageable pagination information
     * @return Page of completed enrollments with final grades
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.enrollmentStatus = 'COMPLETED' AND e.finalGrade IS NOT NULL")
    Page<Enrollment> findCompletedEnrollmentsWithGrades(@Param("studentId") Long studentId, Pageable pageable);

    /**
     * Calculates student's GPA
     * 
     * @param studentId the student ID
     * @return the student's GPA
     */
    @Query("SELECT AVG(e.finalGrade) FROM Enrollment e WHERE e.student.id = :studentId AND e.enrollmentStatus = 'COMPLETED' AND e.finalGrade IS NOT NULL")
    Double calculateStudentGPA(@Param("studentId") Long studentId);

    /**
     * Gets total credits earned by student
     * 
     * @param studentId the student ID
     * @return total credits earned
     */
    @Query("SELECT COALESCE(SUM(e.creditsEarned), 0) FROM Enrollment e WHERE e.student.id = :studentId AND e.enrollmentStatus = 'COMPLETED'")
    Integer getTotalCreditsEarned(@Param("studentId") Long studentId);
}
