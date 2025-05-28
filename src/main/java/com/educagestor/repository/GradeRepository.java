package com.educagestor.repository;

import com.educagestor.entity.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Grade entity operations
 * 
 * This repository provides data access methods for Grade entities,
 * including custom queries for grade management and academic analytics.
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * Finds grades by student ID
     * 
     * @param studentId the student ID
     * @param pageable pagination information
     * @return Page of grades for the specified student
     */
    Page<Grade> findByStudentId(Long studentId, Pageable pageable);

    /**
     * Finds grades by course ID
     * 
     * @param courseId the course ID
     * @param pageable pagination information
     * @return Page of grades for the specified course
     */
    Page<Grade> findByCourseId(Long courseId, Pageable pageable);

    /**
     * Finds grades by student and course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @param pageable pagination information
     * @return Page of grades for the specified student and course
     */
    Page<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId, Pageable pageable);

    /**
     * Finds grades by grade type
     * 
     * @param gradeType the grade type
     * @param pageable pagination information
     * @return Page of grades with the specified type
     */
    Page<Grade> findByGradeType(Grade.GradeType gradeType, Pageable pageable);

    /**
     * Finds grades by student and grade type
     * 
     * @param studentId the student ID
     * @param gradeType the grade type
     * @param pageable pagination information
     * @return Page of grades matching the criteria
     */
    Page<Grade> findByStudentIdAndGradeType(Long studentId, Grade.GradeType gradeType, Pageable pageable);

    /**
     * Finds grades by course and grade type
     * 
     * @param courseId the course ID
     * @param gradeType the grade type
     * @param pageable pagination information
     * @return Page of grades matching the criteria
     */
    Page<Grade> findByCourseIdAndGradeType(Long courseId, Grade.GradeType gradeType, Pageable pageable);

    /**
     * Finds grades between dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return Page of grades within the date range
     */
    Page<Grade> findByGradeDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Finds grades by value range
     * 
     * @param minGrade minimum grade value
     * @param maxGrade maximum grade value
     * @param pageable pagination information
     * @return Page of grades within the value range
     */
    Page<Grade> findByGradeValueBetween(Double minGrade, Double maxGrade, Pageable pageable);

    /**
     * Calculates average grade for a student in a course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @return average grade
     */
    @Query("SELECT AVG(g.gradeValue) FROM Grade g WHERE g.student.id = :studentId AND g.course.id = :courseId AND g.isDropped = false")
    Double calculateAverageGrade(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * Calculates weighted average grade for a student in a course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @return weighted average grade
     */
    @Query("SELECT SUM(g.gradeValue * COALESCE(g.weight, 1.0)) / SUM(COALESCE(g.weight, 1.0)) " +
           "FROM Grade g WHERE g.student.id = :studentId AND g.course.id = :courseId AND g.isDropped = false")
    Double calculateWeightedAverageGrade(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * Finds highest grade for a student in a course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @return highest grade value
     */
    @Query("SELECT MAX(g.gradeValue) FROM Grade g WHERE g.student.id = :studentId AND g.course.id = :courseId AND g.isDropped = false")
    Double findHighestGrade(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * Finds lowest grade for a student in a course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @return lowest grade value
     */
    @Query("SELECT MIN(g.gradeValue) FROM Grade g WHERE g.student.id = :studentId AND g.course.id = :courseId AND g.isDropped = false")
    Double findLowestGrade(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * Counts grades by student and course
     * 
     * @param studentId the student ID
     * @param courseId the course ID
     * @return number of grades
     */
    Long countByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Finds recent grades for a student
     * 
     * @param studentId the student ID
     * @param days number of days back
     * @return List of recent grades
     */
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.gradeDate >= :sinceDate ORDER BY g.gradeDate DESC")
    List<Grade> findRecentGrades(@Param("studentId") Long studentId, @Param("sinceDate") LocalDate sinceDate);

    /**
     * Gets grade distribution for a course
     * 
     * @param courseId the course ID
     * @return List of grade distribution data
     */
    @Query("SELECT " +
           "CASE " +
           "WHEN g.gradeValue >= 90 THEN 'A' " +
           "WHEN g.gradeValue >= 80 THEN 'B' " +
           "WHEN g.gradeValue >= 70 THEN 'C' " +
           "WHEN g.gradeValue >= 60 THEN 'D' " +
           "ELSE 'F' " +
           "END as letterGrade, " +
           "COUNT(g) as count " +
           "FROM Grade g WHERE g.course.id = :courseId AND g.isDropped = false " +
           "GROUP BY " +
           "CASE " +
           "WHEN g.gradeValue >= 90 THEN 'A' " +
           "WHEN g.gradeValue >= 80 THEN 'B' " +
           "WHEN g.gradeValue >= 70 THEN 'C' " +
           "WHEN g.gradeValue >= 60 THEN 'D' " +
           "ELSE 'F' " +
           "END")
    List<Object[]> getGradeDistribution(@Param("courseId") Long courseId);
}
