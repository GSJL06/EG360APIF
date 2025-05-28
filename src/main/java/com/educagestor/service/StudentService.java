package com.educagestor.service;

import com.educagestor.dto.student.StudentDto;
import com.educagestor.entity.Role;
import com.educagestor.entity.Student;
import com.educagestor.entity.User;
import com.educagestor.exception.BadRequestException;
import com.educagestor.exception.ResourceNotFoundException;
import com.educagestor.repository.StudentRepository;
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
 * Service class for student management operations
 * 
 * This service handles student registration, profile management,
 * and student-related business logic operations.
 */
@Service
@Transactional
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new student
     * 
     * @param studentDto student registration data
     * @return created student DTO
     * @throws BadRequestException if registration data is invalid
     */
    public StudentDto registerStudent(StudentDto studentDto) {
        logger.info("Registering new student with ID: {}", studentDto.getStudentId());

        // Validate student ID uniqueness
        if (studentRepository.existsByStudentId(studentDto.getStudentId())) {
            throw new BadRequestException("Student ID already exists: " + studentDto.getStudentId());
        }

        // Validate username and email uniqueness
        if (userRepository.existsByUsername(studentDto.getUser().getUsername())) {
            throw new BadRequestException("Username already exists: " + studentDto.getUser().getUsername());
        }

        if (userRepository.existsByEmail(studentDto.getUser().getEmail())) {
            throw new BadRequestException("Email already exists: " + studentDto.getUser().getEmail());
        }

        // Create user account
        User user = new User();
        user.setUsername(studentDto.getUser().getUsername());
        user.setEmail(studentDto.getUser().getEmail());
        user.setPassword(passwordEncoder.encode("student123")); // Default password
        user.setFirstName(studentDto.getUser().getFirstName());
        user.setLastName(studentDto.getUser().getLastName());
        user.setPhoneNumber(studentDto.getUser().getPhoneNumber());
        user.setRoles(Set.of(Role.STUDENT));

        User savedUser = userRepository.save(user);

        // Create student profile
        Student student = new Student();
        student.setStudentId(studentDto.getStudentId());
        student.setUser(savedUser);
        student.setDateOfBirth(studentDto.getDateOfBirth());
        student.setAddress(studentDto.getAddress());
        student.setEmergencyContact(studentDto.getEmergencyContact());
        student.setEmergencyPhone(studentDto.getEmergencyPhone());
        student.setEnrollmentDate(studentDto.getEnrollmentDate() != null ? 
            studentDto.getEnrollmentDate() : LocalDate.now());
        student.setAcademicStatus(studentDto.getAcademicStatus() != null ? 
            studentDto.getAcademicStatus() : Student.AcademicStatus.ACTIVE);

        Student savedStudent = studentRepository.save(student);
        logger.info("Student registered successfully: {}", savedStudent.getStudentId());

        return convertToStudentDto(savedStudent);
    }

    /**
     * Gets student by ID
     * 
     * @param studentId student ID
     * @return student DTO
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long studentId) {
        logger.info("Fetching student by ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        return convertToStudentDto(student);
    }

    /**
     * Gets student by student ID
     * 
     * @param studentId student ID
     * @return student DTO
     * @throws ResourceNotFoundException if student not found
     */
    @Transactional(readOnly = true)
    public StudentDto getStudentByStudentId(String studentId) {
        logger.info("Fetching student by student ID: {}", studentId);

        Student student = studentRepository.findByStudentId(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));

        return convertToStudentDto(student);
    }

    /**
     * Gets all students with pagination
     * 
     * @param pageable pagination information
     * @return page of student DTOs
     */
    @Transactional(readOnly = true)
    public Page<StudentDto> getAllStudents(Pageable pageable) {
        logger.info("Fetching all students with pagination");

        Page<Student> students = studentRepository.findAll(pageable);
        return students.map(this::convertToStudentDto);
    }

    /**
     * Updates student information
     * 
     * @param studentId student ID
     * @param studentDto updated student data
     * @return updated student DTO
     * @throws ResourceNotFoundException if student not found
     */
    public StudentDto updateStudent(Long studentId, StudentDto studentDto) {
        logger.info("Updating student with ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // Update student fields
        if (studentDto.getDateOfBirth() != null) {
            student.setDateOfBirth(studentDto.getDateOfBirth());
        }
        if (studentDto.getAddress() != null) {
            student.setAddress(studentDto.getAddress());
        }
        if (studentDto.getEmergencyContact() != null) {
            student.setEmergencyContact(studentDto.getEmergencyContact());
        }
        if (studentDto.getEmergencyPhone() != null) {
            student.setEmergencyPhone(studentDto.getEmergencyPhone());
        }
        if (studentDto.getAcademicStatus() != null) {
            student.setAcademicStatus(studentDto.getAcademicStatus());
        }

        // Update user fields if provided
        if (studentDto.getUser() != null) {
            User user = student.getUser();
            if (studentDto.getUser().getFirstName() != null) {
                user.setFirstName(studentDto.getUser().getFirstName());
            }
            if (studentDto.getUser().getLastName() != null) {
                user.setLastName(studentDto.getUser().getLastName());
            }
            if (studentDto.getUser().getEmail() != null) {
                user.setEmail(studentDto.getUser().getEmail());
            }
            if (studentDto.getUser().getPhoneNumber() != null) {
                user.setPhoneNumber(studentDto.getUser().getPhoneNumber());
            }
            userRepository.save(user);
        }

        Student updatedStudent = studentRepository.save(student);
        logger.info("Student updated successfully: {}", updatedStudent.getStudentId());

        return convertToStudentDto(updatedStudent);
    }

    /**
     * Deletes a student
     * 
     * @param studentId student ID
     * @throws ResourceNotFoundException if student not found
     */
    public void deleteStudent(Long studentId) {
        logger.info("Deleting student with ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // Set user as inactive instead of deleting
        User user = student.getUser();
        user.setActive(false);
        userRepository.save(user);

        // Set student as inactive
        student.setAcademicStatus(Student.AcademicStatus.INACTIVE);
        studentRepository.save(student);

        logger.info("Student deactivated successfully: {}", student.getStudentId());
    }

    /**
     * Searches students by search term
     * 
     * @param searchTerm search term
     * @param pageable pagination information
     * @return page of student DTOs
     */
    @Transactional(readOnly = true)
    public Page<StudentDto> searchStudents(String searchTerm, Pageable pageable) {
        logger.info("Searching students with term: {}", searchTerm);

        Page<Student> students = studentRepository.findBySearchTerm(searchTerm, pageable);
        return students.map(this::convertToStudentDto);
    }

    /**
     * Gets students by academic status
     * 
     * @param academicStatus academic status
     * @param pageable pagination information
     * @return page of student DTOs
     */
    @Transactional(readOnly = true)
    public Page<StudentDto> getStudentsByAcademicStatus(Student.AcademicStatus academicStatus, Pageable pageable) {
        logger.info("Fetching students by academic status: {}", academicStatus);

        Page<Student> students = studentRepository.findByAcademicStatus(academicStatus, pageable);
        return students.map(this::convertToStudentDto);
    }

    /**
     * Converts Student entity to StudentDto
     * 
     * @param student Student entity
     * @return StudentDto
     */
    private StudentDto convertToStudentDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setAddress(student.getAddress());
        dto.setEmergencyContact(student.getEmergencyContact());
        dto.setEmergencyPhone(student.getEmergencyPhone());
        dto.setEnrollmentDate(student.getEnrollmentDate());
        dto.setAcademicStatus(student.getAcademicStatus());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());

        // Convert user information
        if (student.getUser() != null) {
            User user = student.getUser();
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
