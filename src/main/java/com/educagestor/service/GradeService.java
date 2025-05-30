package com.educagestor.service;

import com.educagestor.dto.grade.GradeDto;
import com.educagestor.entity.Course;
import com.educagestor.entity.Grade;
import com.educagestor.entity.Student;
import com.educagestor.exception.BadRequestException;
import com.educagestor.exception.ResourceNotFoundException;
import com.educagestor.repository.CourseRepository;
import com.educagestor.repository.GradeRepository;
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
 * Service class for grade management operations
 *
 * This service handles grade recording, grade management,
 * and grade-related business logic operations.
 */
@Service
@Transactional
public class GradeService {

    private static final Logger logger = LoggerFactory.getLogger(GradeService.class);

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Records a new grade
     *
     * @param gradeDto grade data
     * @return created grade DTO
     * @throws BadRequestException if grade data is invalid
     * @throws ResourceNotFoundException if student or course not found
     */
    public GradeDto recordGrade(GradeDto gradeDto) {
        logger.info("Recording grade for student {} in course {}", gradeDto.getStudentId(), gradeDto.getCourseId());

        // Validate student exists
        Student student = studentRepository.findById(gradeDto.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", gradeDto.getStudentId()));

        // Validate course exists
        Course course = courseRepository.findById(gradeDto.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", gradeDto.getCourseId()));

        // Validate grade values
        if (gradeDto.getGradeValue() < 0 || gradeDto.getGradeValue() > gradeDto.getMaxPoints()) {
            throw new BadRequestException("Grade value must be between 0 and maximum points");
        }

        // Create grade
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setAssignmentName(gradeDto.getAssignmentName());
        grade.setGradeType(gradeDto.getGradeType());
        grade.setGradeValue(gradeDto.getGradeValue());
        grade.setMaxPoints(gradeDto.getMaxPoints());
        grade.setWeight(gradeDto.getWeight());
        grade.setGradeDate(gradeDto.getGradeDate() != null ? gradeDto.getGradeDate() : LocalDate.now());
        grade.setComments(gradeDto.getComments());
        grade.setIsExtraCredit(gradeDto.getIsExtraCredit());
        grade.setIsDropped(false); // New grades are not dropped by default

        Grade savedGrade = gradeRepository.save(grade);
        logger.info("Grade recorded successfully for student {} in course {}", gradeDto.getStudentId(), gradeDto.getCourseId());

        return convertToGradeDto(savedGrade);
    }

    /**
     * Gets all grades with pagination
     *
     * @param pageable pagination information
     * @return page of grade DTOs
     */
    @Transactional(readOnly = true)
    public Page<GradeDto> getAllGrades(Pageable pageable) {
        logger.info("Fetching all grades with pagination");

        Page<Grade> grades = gradeRepository.findAll(pageable);
        return grades.map(this::convertToGradeDto);
    }

    /**
     * Gets grade by ID
     *
     * @param gradeId grade ID
     * @return grade DTO
     * @throws ResourceNotFoundException if grade not found
     */
    @Transactional(readOnly = true)
    public GradeDto getGradeById(Long gradeId) {
        logger.info("Fetching grade by ID: {}", gradeId);

        Grade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", gradeId));

        return convertToGradeDto(grade);
    }

    /**
     * Gets grades by student ID
     *
     * @param studentId student ID
     * @param pageable pagination information
     * @return page of grade DTOs
     */
    @Transactional(readOnly = true)
    public Page<GradeDto> getGradesByStudentId(Long studentId, Pageable pageable) {
        logger.info("Fetching grades for student: {}", studentId);

        Page<Grade> grades = gradeRepository.findByStudentId(studentId, pageable);
        return grades.map(this::convertToGradeDto);
    }

    /**
     * Gets grades by course ID
     *
     * @param courseId course ID
     * @param pageable pagination information
     * @return page of grade DTOs
     */
    @Transactional(readOnly = true)
    public Page<GradeDto> getGradesByCourseId(Long courseId, Pageable pageable) {
        logger.info("Fetching grades for course: {}", courseId);

        Page<Grade> grades = gradeRepository.findByCourseId(courseId, pageable);
        return grades.map(this::convertToGradeDto);
    }

    /**
     * Gets grades by student and course
     *
     * @param studentId student ID
     * @param courseId course ID
     * @param pageable pagination information
     * @return page of grade DTOs
     */
    @Transactional(readOnly = true)
    public Page<GradeDto> getGradesByStudentAndCourse(Long studentId, Long courseId, Pageable pageable) {
        logger.info("Fetching grades for student {} in course {}", studentId, courseId);

        Page<Grade> grades = gradeRepository.findByStudentIdAndCourseId(studentId, courseId, pageable);
        return grades.map(this::convertToGradeDto);
    }

    /**
     * Updates a grade
     *
     * @param gradeId grade ID
     * @param gradeDto updated grade data
     * @return updated grade DTO
     * @throws ResourceNotFoundException if grade not found
     * @throws BadRequestException if grade data is invalid
     */
    public GradeDto updateGrade(Long gradeId, GradeDto gradeDto) {
        logger.info("Updating grade: {}", gradeId);

        Grade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", gradeId));

        // Update grade fields
        if (gradeDto.getAssignmentName() != null) {
            grade.setAssignmentName(gradeDto.getAssignmentName());
        }
        if (gradeDto.getGradeType() != null) {
            grade.setGradeType(gradeDto.getGradeType());
        }
        if (gradeDto.getGradeValue() != null) {
            // Validate grade value
            if (gradeDto.getGradeValue() < 0 || gradeDto.getGradeValue() > grade.getMaxPoints()) {
                throw new BadRequestException("Grade value must be between 0 and maximum points");
            }
            grade.setGradeValue(gradeDto.getGradeValue());
        }
        if (gradeDto.getMaxPoints() != null) {
            grade.setMaxPoints(gradeDto.getMaxPoints());
        }
        if (gradeDto.getWeight() != null) {
            grade.setWeight(gradeDto.getWeight());
        }
        if (gradeDto.getGradeDate() != null) {
            grade.setGradeDate(gradeDto.getGradeDate());
        }
        if (gradeDto.getComments() != null) {
            grade.setComments(gradeDto.getComments());
        }
        if (gradeDto.getIsExtraCredit() != null) {
            grade.setIsExtraCredit(gradeDto.getIsExtraCredit());
        }
        if (gradeDto.getIsDropped() != null) {
            grade.setIsDropped(gradeDto.getIsDropped());
        }

        Grade updatedGrade = gradeRepository.save(grade);
        logger.info("Grade updated successfully: {}", gradeId);

        return convertToGradeDto(updatedGrade);
    }

    /**
     * Deletes a grade
     *
     * @param gradeId grade ID
     * @throws ResourceNotFoundException if grade not found
     */
    public void deleteGrade(Long gradeId) {
        logger.info("Deleting grade: {}", gradeId);

        Grade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", gradeId));

        gradeRepository.delete(grade);
        logger.info("Grade deleted successfully: {}", gradeId);
    }

    /**
     * Calculates average grade for a student in a course
     *
     * @param studentId student ID
     * @param courseId course ID
     * @return average grade
     */
    @Transactional(readOnly = true)
    public Double calculateAverageGrade(Long studentId, Long courseId) {
        logger.info("Calculating average grade for student {} in course {}", studentId, courseId);

        Double average = gradeRepository.calculateAverageGrade(studentId, courseId);
        return average != null ? average : 0.0;
    }

    /**
     * Calculates weighted average grade for a student in a course
     *
     * @param studentId student ID
     * @param courseId course ID
     * @return weighted average grade
     */
    @Transactional(readOnly = true)
    public Double calculateWeightedAverageGrade(Long studentId, Long courseId) {
        logger.info("Calculating weighted average grade for student {} in course {}", studentId, courseId);

        Double weightedAverage = gradeRepository.calculateWeightedAverageGrade(studentId, courseId);
        return weightedAverage != null ? weightedAverage : 0.0;
    }

    /**
     * Converts Grade entity to GradeDto
     *
     * @param grade Grade entity
     * @return GradeDto
     */
    private GradeDto convertToGradeDto(Grade grade) {
        GradeDto dto = new GradeDto();
        dto.setId(grade.getId());
        dto.setStudentId(grade.getStudent().getId());
        dto.setStudentName(grade.getStudent().getUser().getFullName());
        dto.setStudentCode(grade.getStudent().getStudentId());
        dto.setCourseId(grade.getCourse().getId());
        dto.setCourseName(grade.getCourse().getCourseName());
        dto.setCourseCode(grade.getCourse().getCourseCode());
        dto.setAssignmentName(grade.getAssignmentName());
        dto.setGradeType(grade.getGradeType());
        dto.setGradeValue(grade.getGradeValue());
        dto.setMaxPoints(grade.getMaxPoints());
        dto.setWeight(grade.getWeight());
        dto.setGradeDate(grade.getGradeDate());
        dto.setComments(grade.getComments());
        dto.setIsExtraCredit(grade.getIsExtraCredit());
        dto.setIsDropped(grade.getIsDropped());
        dto.setCreatedAt(grade.getCreatedAt());
        dto.setUpdatedAt(grade.getUpdatedAt());

        return dto;
    }
}
