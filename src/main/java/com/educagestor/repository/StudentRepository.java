package com.educagestor.repository;

import com.educagestor.entity.Student;
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
 * Repository interface for Student entity operations
 * 
 * This repository provides data access methods for Student entities,
 * including custom queries for student management and academic tracking.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Finds student by student ID
     * 
     * @param studentId the student ID to search for
     * @return Optional containing the student if found
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * Finds student by user ID
     * 
     * @param userId the user ID to search for
     * @return Optional containing the student if found
     */
    Optional<Student> findByUserId(Long userId);

    /**
     * Checks if student ID exists
     * 
     * @param studentId the student ID to check
     * @return true if student ID exists, false otherwise
     */
    Boolean existsByStudentId(String studentId);

    /**
     * Finds students by academic status
     * 
     * @param academicStatus the academic status to search for
     * @param pageable pagination information
     * @return Page of students with the specified academic status
     */
    Page<Student> findByAcademicStatus(Student.AcademicStatus academicStatus, Pageable pageable);

    /**
     * Finds students enrolled between dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return Page of students enrolled within the date range
     */
    Page<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Searches students by name or student ID
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return Page of students matching the search criteria
     */
    @Query("SELECT s FROM Student s JOIN s.user u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.studentId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Student> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Finds students by course enrollment
     * 
     * @param courseId the course ID
     * @param pageable pagination information
     * @return Page of students enrolled in the specified course
     */
    @Query("SELECT s FROM Student s JOIN s.enrollments e WHERE e.course.id = :courseId AND e.enrollmentStatus = 'ENROLLED'")
    Page<Student> findByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    /**
     * Counts active students
     * 
     * @return number of active students
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.academicStatus = 'ACTIVE'")
    Long countActiveStudents();

    /**
     * Finds students with birthdays in a specific month
     * 
     * @param month the month (1-12)
     * @return List of students with birthdays in the specified month
     */
    @Query("SELECT s FROM Student s WHERE MONTH(s.dateOfBirth) = :month")
    List<Student> findByBirthMonth(@Param("month") int month);

    /**
     * Finds students by age range
     * 
     * @param minAge minimum age
     * @param maxAge maximum age
     * @param pageable pagination information
     * @return Page of students within the age range
     */
    @Query("SELECT s FROM Student s WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(s.dateOfBirth) BETWEEN :minAge AND :maxAge")
    Page<Student> findByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge, Pageable pageable);
}
