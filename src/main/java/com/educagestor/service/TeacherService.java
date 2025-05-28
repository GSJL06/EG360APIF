package com.educagestor.service;

import com.educagestor.dto.teacher.TeacherDto;
import com.educagestor.entity.Role;
import com.educagestor.entity.Teacher;
import com.educagestor.entity.User;
import com.educagestor.exception.BadRequestException;
import com.educagestor.exception.ResourceNotFoundException;
import com.educagestor.repository.TeacherRepository;
import com.educagestor.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

/**
 * Service class for teacher management operations
 * 
 * This service handles teacher registration, profile management,
 * and teacher-related business logic operations.
 */
@Service
@Transactional
public class TeacherService {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new teacher
     * 
     * @param teacherDto teacher registration data
     * @return created teacher DTO
     * @throws BadRequestException if registration data is invalid
     */
    public TeacherDto registerTeacher(TeacherDto teacherDto) {
        logger.info("Registering new teacher with employee ID: {}", teacherDto.getEmployeeId());

        // Validate employee ID uniqueness
        if (teacherRepository.existsByEmployeeId(teacherDto.getEmployeeId())) {
            throw new BadRequestException("Employee ID already exists: " + teacherDto.getEmployeeId());
        }

        // Validate username and email uniqueness
        if (userRepository.existsByUsername(teacherDto.getUser().getUsername())) {
            throw new BadRequestException("Username already exists: " + teacherDto.getUser().getUsername());
        }

        if (userRepository.existsByEmail(teacherDto.getUser().getEmail())) {
            throw new BadRequestException("Email already exists: " + teacherDto.getUser().getEmail());
        }

        // Create user account
        User user = new User();
        user.setUsername(teacherDto.getUser().getUsername());
        user.setEmail(teacherDto.getUser().getEmail());
        user.setPassword(passwordEncoder.encode("teacher123")); // Default password
        user.setFirstName(teacherDto.getUser().getFirstName());
        user.setLastName(teacherDto.getUser().getLastName());
        user.setPhoneNumber(teacherDto.getUser().getPhoneNumber());
        user.setRoles(Set.of(Role.TEACHER));

        User savedUser = userRepository.save(user);

        // Create teacher profile
        Teacher teacher = new Teacher();
        teacher.setEmployeeId(teacherDto.getEmployeeId());
        teacher.setUser(savedUser);
        teacher.setDepartment(teacherDto.getDepartment());
        teacher.setSpecialization(teacherDto.getSpecialization());
        teacher.setQualifications(teacherDto.getQualifications());
        teacher.setHireDate(teacherDto.getHireDate() != null ? 
            teacherDto.getHireDate() : LocalDate.now());
        teacher.setOfficeLocation(teacherDto.getOfficeLocation());
        teacher.setOfficeHours(teacherDto.getOfficeHours());
        teacher.setEmploymentStatus(teacherDto.getEmploymentStatus() != null ? 
            teacherDto.getEmploymentStatus() : Teacher.EmploymentStatus.ACTIVE);

        Teacher savedTeacher = teacherRepository.save(teacher);
        logger.info("Teacher registered successfully: {}", savedTeacher.getEmployeeId());

        return convertToTeacherDto(savedTeacher);
    }

    /**
     * Gets teacher by ID
     * 
     * @param teacherId teacher ID
     * @return teacher DTO
     * @throws ResourceNotFoundException if teacher not found
     */
    @Transactional(readOnly = true)
    public TeacherDto getTeacherById(Long teacherId) {
        logger.info("Fetching teacher by ID: {}", teacherId);

        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        return convertToTeacherDto(teacher);
    }

    /**
     * Gets teacher by employee ID
     * 
     * @param employeeId employee ID
     * @return teacher DTO
     * @throws ResourceNotFoundException if teacher not found
     */
    @Transactional(readOnly = true)
    public TeacherDto getTeacherByEmployeeId(String employeeId) {
        logger.info("Fetching teacher by employee ID: {}", employeeId);

        Teacher teacher = teacherRepository.findByEmployeeId(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "employeeId", employeeId));

        return convertToTeacherDto(teacher);
    }

    /**
     * Gets all teachers with pagination
     * 
     * @param pageable pagination information
     * @return page of teacher DTOs
     */
    @Transactional(readOnly = true)
    public Page<TeacherDto> getAllTeachers(Pageable pageable) {
        logger.info("Fetching all teachers with pagination");

        Page<Teacher> teachers = teacherRepository.findAll(pageable);
        return teachers.map(this::convertToTeacherDto);
    }

    /**
     * Updates teacher information
     * 
     * @param teacherId teacher ID
     * @param teacherDto updated teacher data
     * @return updated teacher DTO
     * @throws ResourceNotFoundException if teacher not found
     */
    public TeacherDto updateTeacher(Long teacherId, TeacherDto teacherDto) {
        logger.info("Updating teacher with ID: {}", teacherId);

        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        // Update teacher fields
        if (teacherDto.getDepartment() != null) {
            teacher.setDepartment(teacherDto.getDepartment());
        }
        if (teacherDto.getSpecialization() != null) {
            teacher.setSpecialization(teacherDto.getSpecialization());
        }
        if (teacherDto.getQualifications() != null) {
            teacher.setQualifications(teacherDto.getQualifications());
        }
        if (teacherDto.getOfficeLocation() != null) {
            teacher.setOfficeLocation(teacherDto.getOfficeLocation());
        }
        if (teacherDto.getOfficeHours() != null) {
            teacher.setOfficeHours(teacherDto.getOfficeHours());
        }
        if (teacherDto.getEmploymentStatus() != null) {
            teacher.setEmploymentStatus(teacherDto.getEmploymentStatus());
        }

        // Update user fields if provided
        if (teacherDto.getUser() != null) {
            User user = teacher.getUser();
            if (teacherDto.getUser().getFirstName() != null) {
                user.setFirstName(teacherDto.getUser().getFirstName());
            }
            if (teacherDto.getUser().getLastName() != null) {
                user.setLastName(teacherDto.getUser().getLastName());
            }
            if (teacherDto.getUser().getEmail() != null) {
                user.setEmail(teacherDto.getUser().getEmail());
            }
            if (teacherDto.getUser().getPhoneNumber() != null) {
                user.setPhoneNumber(teacherDto.getUser().getPhoneNumber());
            }
            userRepository.save(user);
        }

        Teacher updatedTeacher = teacherRepository.save(teacher);
        logger.info("Teacher updated successfully: {}", updatedTeacher.getEmployeeId());

        return convertToTeacherDto(updatedTeacher);
    }

    /**
     * Gets teachers by department
     * 
     * @param department department name
     * @param pageable pagination information
     * @return page of teacher DTOs
     */
    @Transactional(readOnly = true)
    public Page<TeacherDto> getTeachersByDepartment(String department, Pageable pageable) {
        logger.info("Fetching teachers by department: {}", department);

        Page<Teacher> teachers = teacherRepository.findByDepartment(department, pageable);
        return teachers.map(this::convertToTeacherDto);
    }

    /**
     * Searches teachers by search term
     * 
     * @param searchTerm search term
     * @param pageable pagination information
     * @return page of teacher DTOs
     */
    @Transactional(readOnly = true)
    public Page<TeacherDto> searchTeachers(String searchTerm, Pageable pageable) {
        logger.info("Searching teachers with term: {}", searchTerm);

        Page<Teacher> teachers = teacherRepository.findBySearchTerm(searchTerm, pageable);
        return teachers.map(this::convertToTeacherDto);
    }

    /**
     * Converts Teacher entity to TeacherDto
     * 
     * @param teacher Teacher entity
     * @return TeacherDto
     */
    private TeacherDto convertToTeacherDto(Teacher teacher) {
        TeacherDto dto = new TeacherDto();
        dto.setId(teacher.getId());
        dto.setEmployeeId(teacher.getEmployeeId());
        dto.setDepartment(teacher.getDepartment());
        dto.setSpecialization(teacher.getSpecialization());
        dto.setQualifications(teacher.getQualifications());
        dto.setHireDate(teacher.getHireDate());
        dto.setOfficeLocation(teacher.getOfficeLocation());
        dto.setOfficeHours(teacher.getOfficeHours());
        dto.setEmploymentStatus(teacher.getEmploymentStatus());
        dto.setCreatedAt(teacher.getCreatedAt());
        dto.setUpdatedAt(teacher.getUpdatedAt());

        // Convert user information
        if (teacher.getUser() != null) {
            User user = teacher.getUser();
            dto.setUser(new com.educagestor.dto.user.UserProfileDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getActive(),
                user.getRoles(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            ));
        }

        return dto;
    }
}
