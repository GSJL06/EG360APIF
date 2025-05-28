package com.educagestor.repository;

import com.educagestor.entity.Course;
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
 * Repository interface for Course entity operations
 * 
 * This repository provides data access methods for Course entities,
 * including custom queries for course management and enrollment tracking.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Finds course by course code
     * 
     * @param courseCode the course code to search for
     * @return Optional containing the course if found
     */
    Optional<Course> findByCourseCode(String courseCode);

    /**
     * Checks if course code exists
     * 
     * @param courseCode the course code to check
     * @return true if course code exists, false otherwise
     */
    Boolean existsByCourseCode(String courseCode);

    /**
     * Finds courses by status
     * 
     * @param courseStatus the course status to search for
     * @param pageable pagination information
     * @return Page of courses with the specified status
     */
    Page<Course> findByCourseStatus(Course.CourseStatus courseStatus, Pageable pageable);

    /**
     * Finds courses by teacher ID
     * 
     * @param teacherId the teacher ID to search for
     * @param pageable pagination information
     * @return Page of courses assigned to the specified teacher
     */
    Page<Course> findByTeacherId(Long teacherId, Pageable pageable);

    /**
     * Finds courses by credits
     * 
     * @param credits the number of credits
     * @param pageable pagination information
     * @return Page of courses with the specified credits
     */
    Page<Course> findByCredits(Integer credits, Pageable pageable);

    /**
     * Searches courses by name or code
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return Page of courses matching the search criteria
     */
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Course> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Finds courses starting between dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return Page of courses starting within the date range
     */
    Page<Course> findByStartDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Finds courses with available spots
     * 
     * @param pageable pagination information
     * @return Page of courses that have available enrollment spots
     */
    @Query("SELECT c FROM Course c WHERE " +
           "(SELECT COUNT(e) FROM Enrollment e WHERE e.course = c AND e.enrollmentStatus = 'ENROLLED') < c.maxStudents " +
           "AND c.courseStatus = 'ACTIVE'")
    Page<Course> findCoursesWithAvailableSpots(Pageable pageable);

    /**
     * Finds courses without assigned teacher
     * 
     * @param pageable pagination information
     * @return Page of courses without teacher assignment
     */
    Page<Course> findByTeacherIsNull(Pageable pageable);

    /**
     * Finds courses by student enrollment
     * 
     * @param studentId the student ID
     * @param pageable pagination information
     * @return Page of courses the student is enrolled in
     */
    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.student.id = :studentId AND e.enrollmentStatus = 'ENROLLED'")
    Page<Course> findByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    /**
     * Counts courses by status
     * 
     * @param courseStatus the course status
     * @return number of courses with the specified status
     */
    Long countByCourseStatus(Course.CourseStatus courseStatus);

    /**
     * Finds courses ending soon (within specified days)
     * 
     * @param days number of days from now
     * @param pageable pagination information
     * @return Page of courses ending within the specified days
     */
    @Query("SELECT c FROM Course c WHERE c.endDate BETWEEN CURRENT_DATE AND :endDate AND c.courseStatus = 'ACTIVE'")
    Page<Course> findCoursesEndingSoon(@Param("endDate") LocalDate endDate, Pageable pageable);

    /**
     * Gets course enrollment statistics
     * 
     * @param courseId the course ID
     * @return array containing [enrolled_count, max_students, available_spots]
     */
    @Query("SELECT " +
           "(SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.enrollmentStatus = 'ENROLLED'), " +
           "c.maxStudents, " +
           "(c.maxStudents - (SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.enrollmentStatus = 'ENROLLED')) " +
           "FROM Course c WHERE c.id = :courseId")
    Object[] getCourseEnrollmentStats(@Param("courseId") Long courseId);
}
