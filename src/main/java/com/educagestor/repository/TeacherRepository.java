package com.educagestor.repository;

import com.educagestor.entity.Teacher;
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
 * Repository interface for Teacher entity operations
 * 
 * This repository provides data access methods for Teacher entities,
 * including custom queries for teacher management and course assignments.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * Finds teacher by employee ID
     * 
     * @param employeeId the employee ID to search for
     * @return Optional containing the teacher if found
     */
    Optional<Teacher> findByEmployeeId(String employeeId);

    /**
     * Finds teacher by user ID
     * 
     * @param userId the user ID to search for
     * @return Optional containing the teacher if found
     */
    Optional<Teacher> findByUserId(Long userId);

    /**
     * Checks if employee ID exists
     * 
     * @param employeeId the employee ID to check
     * @return true if employee ID exists, false otherwise
     */
    Boolean existsByEmployeeId(String employeeId);

    /**
     * Finds teachers by department
     * 
     * @param department the department to search for
     * @param pageable pagination information
     * @return Page of teachers in the specified department
     */
    Page<Teacher> findByDepartment(String department, Pageable pageable);

    /**
     * Finds teachers by employment status
     * 
     * @param employmentStatus the employment status to search for
     * @param pageable pagination information
     * @return Page of teachers with the specified employment status
     */
    Page<Teacher> findByEmploymentStatus(Teacher.EmploymentStatus employmentStatus, Pageable pageable);

    /**
     * Finds teachers by specialization
     * 
     * @param specialization the specialization to search for
     * @param pageable pagination information
     * @return Page of teachers with the specified specialization
     */
    Page<Teacher> findBySpecializationContainingIgnoreCase(String specialization, Pageable pageable);

    /**
     * Searches teachers by name, employee ID, or department
     * 
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return Page of teachers matching the search criteria
     */
    @Query("SELECT t FROM Teacher t JOIN t.user u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.department) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Teacher> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Finds teachers hired between dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return Page of teachers hired within the date range
     */
    Page<Teacher> findByHireDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Finds all departments
     * 
     * @return List of distinct departments
     */
    @Query("SELECT DISTINCT t.department FROM Teacher t WHERE t.department IS NOT NULL ORDER BY t.department")
    List<String> findAllDepartments();

    /**
     * Counts teachers by department
     * 
     * @param department the department to count
     * @return number of teachers in the specified department
     */
    Long countByDepartment(String department);

    /**
     * Finds teachers with active courses
     * 
     * @param pageable pagination information
     * @return Page of teachers who have active courses
     */
    @Query("SELECT DISTINCT t FROM Teacher t JOIN t.courses c WHERE c.courseStatus = 'ACTIVE'")
    Page<Teacher> findTeachersWithActiveCourses(Pageable pageable);

    /**
     * Finds teachers without any course assignments
     * 
     * @param pageable pagination information
     * @return Page of teachers without course assignments
     */
    @Query("SELECT t FROM Teacher t WHERE t.courses IS EMPTY")
    Page<Teacher> findTeachersWithoutCourses(Pageable pageable);

    /**
     * Counts active teachers
     * 
     * @return number of active teachers
     */
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.employmentStatus = 'ACTIVE'")
    Long countActiveTeachers();
}
