package com.educagestor.service;

import com.educagestor.dto.course.CourseDto;
import com.educagestor.entity.Course;
import com.educagestor.entity.Teacher;
import com.educagestor.exception.BadRequestException;
import com.educagestor.exception.ResourceNotFoundException;
import com.educagestor.repository.CourseRepository;
import com.educagestor.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for course management operations
 * 
 * This service handles course creation, management, teacher assignments,
 * and course-related business logic operations.
 */
@Service
@Transactional
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * Creates a new course
     * 
     * @param courseDto course creation data
     * @return created course DTO
     * @throws BadRequestException if course data is invalid
     */
    public CourseDto createCourse(CourseDto courseDto) {
        logger.info("Creating new course with code: {}", courseDto.getCourseCode());

        // Validate course code uniqueness
        if (courseRepository.existsByCourseCode(courseDto.getCourseCode())) {
            throw new BadRequestException("Course code already exists: " + courseDto.getCourseCode());
        }

        // Validate date range
        if (courseDto.getStartDate() != null && courseDto.getEndDate() != null) {
            if (courseDto.getStartDate().isAfter(courseDto.getEndDate())) {
                throw new BadRequestException("Start date cannot be after end date");
            }
        }

        // Create course
        Course course = new Course();
        course.setCourseCode(courseDto.getCourseCode());
        course.setCourseName(courseDto.getCourseName());
        course.setDescription(courseDto.getDescription());
        course.setCredits(courseDto.getCredits());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setSchedule(courseDto.getSchedule());
        course.setClassroom(courseDto.getClassroom());
        course.setMaxStudents(courseDto.getMaxStudents());
        course.setCourseStatus(courseDto.getCourseStatus() != null ? 
            courseDto.getCourseStatus() : Course.CourseStatus.ACTIVE);

        // Assign teacher if provided
        if (courseDto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(courseDto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", courseDto.getTeacherId()));
            course.setTeacher(teacher);
        }

        Course savedCourse = courseRepository.save(course);
        logger.info("Course created successfully: {}", savedCourse.getCourseCode());

        return convertToCourseDto(savedCourse);
    }

    /**
     * Gets course by ID
     * 
     * @param courseId course ID
     * @return course DTO
     * @throws ResourceNotFoundException if course not found
     */
    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long courseId) {
        logger.info("Fetching course by ID: {}", courseId);

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        return convertToCourseDto(course);
    }

    /**
     * Gets course by course code
     * 
     * @param courseCode course code
     * @return course DTO
     * @throws ResourceNotFoundException if course not found
     */
    @Transactional(readOnly = true)
    public CourseDto getCourseByCourseCode(String courseCode) {
        logger.info("Fetching course by course code: {}", courseCode);

        Course course = courseRepository.findByCourseCode(courseCode)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "courseCode", courseCode));

        return convertToCourseDto(course);
    }

    /**
     * Gets all courses with pagination
     * 
     * @param pageable pagination information
     * @return page of course DTOs
     */
    @Transactional(readOnly = true)
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        logger.info("Fetching all courses with pagination");

        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(this::convertToCourseDto);
    }

    /**
     * Updates course information
     * 
     * @param courseId course ID
     * @param courseDto updated course data
     * @return updated course DTO
     * @throws ResourceNotFoundException if course not found
     */
    public CourseDto updateCourse(Long courseId, CourseDto courseDto) {
        logger.info("Updating course with ID: {}", courseId);

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        // Update course fields
        if (courseDto.getCourseName() != null) {
            course.setCourseName(courseDto.getCourseName());
        }
        if (courseDto.getDescription() != null) {
            course.setDescription(courseDto.getDescription());
        }
        if (courseDto.getCredits() != null) {
            course.setCredits(courseDto.getCredits());
        }
        if (courseDto.getStartDate() != null) {
            course.setStartDate(courseDto.getStartDate());
        }
        if (courseDto.getEndDate() != null) {
            course.setEndDate(courseDto.getEndDate());
        }
        if (courseDto.getSchedule() != null) {
            course.setSchedule(courseDto.getSchedule());
        }
        if (courseDto.getClassroom() != null) {
            course.setClassroom(courseDto.getClassroom());
        }
        if (courseDto.getMaxStudents() != null) {
            course.setMaxStudents(courseDto.getMaxStudents());
        }
        if (courseDto.getCourseStatus() != null) {
            course.setCourseStatus(courseDto.getCourseStatus());
        }

        // Validate date range if both dates are provided
        if (course.getStartDate() != null && course.getEndDate() != null) {
            if (course.getStartDate().isAfter(course.getEndDate())) {
                throw new BadRequestException("Start date cannot be after end date");
            }
        }

        Course updatedCourse = courseRepository.save(course);
        logger.info("Course updated successfully: {}", updatedCourse.getCourseCode());

        return convertToCourseDto(updatedCourse);
    }

    /**
     * Assigns teacher to course
     * 
     * @param courseId course ID
     * @param teacherId teacher ID
     * @return updated course DTO
     * @throws ResourceNotFoundException if course or teacher not found
     */
    public CourseDto assignTeacherToCourse(Long courseId, Long teacherId) {
        logger.info("Assigning teacher {} to course {}", teacherId, courseId);

        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        course.setTeacher(teacher);
        Course updatedCourse = courseRepository.save(course);

        logger.info("Teacher assigned successfully to course: {}", updatedCourse.getCourseCode());
        return convertToCourseDto(updatedCourse);
    }

    /**
     * Gets courses by teacher ID
     * 
     * @param teacherId teacher ID
     * @param pageable pagination information
     * @return page of course DTOs
     */
    @Transactional(readOnly = true)
    public Page<CourseDto> getCoursesByTeacherId(Long teacherId, Pageable pageable) {
        logger.info("Fetching courses by teacher ID: {}", teacherId);

        Page<Course> courses = courseRepository.findByTeacherId(teacherId, pageable);
        return courses.map(this::convertToCourseDto);
    }

    /**
     * Gets courses by status
     * 
     * @param courseStatus course status
     * @param pageable pagination information
     * @return page of course DTOs
     */
    @Transactional(readOnly = true)
    public Page<CourseDto> getCoursesByStatus(Course.CourseStatus courseStatus, Pageable pageable) {
        logger.info("Fetching courses by status: {}", courseStatus);

        Page<Course> courses = courseRepository.findByCourseStatus(courseStatus, pageable);
        return courses.map(this::convertToCourseDto);
    }

    /**
     * Searches courses by search term
     * 
     * @param searchTerm search term
     * @param pageable pagination information
     * @return page of course DTOs
     */
    @Transactional(readOnly = true)
    public Page<CourseDto> searchCourses(String searchTerm, Pageable pageable) {
        logger.info("Searching courses with term: {}", searchTerm);

        Page<Course> courses = courseRepository.findBySearchTerm(searchTerm, pageable);
        return courses.map(this::convertToCourseDto);
    }

    /**
     * Gets courses with available spots
     * 
     * @param pageable pagination information
     * @return page of course DTOs
     */
    @Transactional(readOnly = true)
    public Page<CourseDto> getCoursesWithAvailableSpots(Pageable pageable) {
        logger.info("Fetching courses with available spots");

        Page<Course> courses = courseRepository.findCoursesWithAvailableSpots(pageable);
        return courses.map(this::convertToCourseDto);
    }

    /**
     * Converts Course entity to CourseDto
     * 
     * @param course Course entity
     * @return CourseDto
     */
    private CourseDto convertToCourseDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setDescription(course.getDescription());
        dto.setCredits(course.getCredits());
        dto.setTeacherId(course.getTeacher() != null ? course.getTeacher().getId() : null);
        dto.setTeacherName(course.getTeacher() != null ? course.getTeacher().getUser().getFullName() : null);
        dto.setStartDate(course.getStartDate());
        dto.setEndDate(course.getEndDate());
        dto.setSchedule(course.getSchedule());
        dto.setClassroom(course.getClassroom());
        dto.setMaxStudents(course.getMaxStudents());
        dto.setCurrentEnrollmentCount(course.getCurrentEnrollmentCount());
        dto.setCourseStatus(course.getCourseStatus());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());

        return dto;
    }
}
