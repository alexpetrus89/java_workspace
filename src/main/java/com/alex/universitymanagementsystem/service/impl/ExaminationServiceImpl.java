package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.UpdateExaminationDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.ExaminationService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Service
public class ExaminationServiceImpl implements ExaminationService {

	// constants
    private static final String REGISTER_BLANK_ERROR = "Register cannot be null or empty";
    private static final String COURSE_NAME_BLANK_ERROR = "Course name cannot be null or empty";
    private static final String DEGREE_COURSE_NAME_BLANK_ERROR = "Degree course name cannot be null or empty";

    // instance variables
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ExaminationRepository examinationRepository;


    // autowired - dependency injection - constructor
    public ExaminationServiceImpl(
        StudentRepository studentRepository,
        ProfessorRepository professorRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        ExaminationRepository examinationRepository
    ) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
        this.degreeCourseRepository = degreeCourseRepository;
        this.examinationRepository = examinationRepository;
    }


    /**
     * Get all examinations
     * @return List<ExaminationDto> a list of all examination data transfer objects
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public List<ExaminationDto> getExaminations() throws DataAccessServiceException {
        try {
            return examinationRepository
                .findAll()
                .stream()
                .map(ExaminationMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching examinations: " + e.getMessage(), e);
        }
    }


    /**
     * Get all examinations by course
     * @param courseName course name of the course
     * @param degreeCourseName degree course name of the course
     * @return List<ExaminationDto>
     * @throws IllegalArgumentException if the course name or degree course name is Blank
     * @throws ObjectNotFoundException if the course does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public List<ExaminationDto> getExaminationsByCourseNameAndDegreeCourseName(String courseName, String degreeCourseName)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        if(courseName.isBlank())
            throw new IllegalArgumentException(COURSE_NAME_BLANK_ERROR);

        if(degreeCourseName.isBlank())
            throw new IllegalArgumentException(DEGREE_COURSE_NAME_BLANK_ERROR);

        Course course = courseRepository
            .findByNameAndDegreeCourseName(courseName, degreeCourseName)
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

        try {
            return examinationRepository
                .findByCourse_Id_Id(course.getId().getId())
                .stream()
                .map(ExaminationMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching examinations: " + e.getMessage(), e);
        }
    }


    /**
     * Get all examinations by student register
     * @param register of the student
     * @return List<ExaminationDto>
     * @throws IllegalArgumentException if the register is blank
     * @throws ObjectNotFoundException if the student does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public List<ExaminationDto> getExaminationsByStudentRegister(Register register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        if(register.toString().isBlank())
            throw new IllegalArgumentException("Register cannot be empty");

        if (!studentRepository.existsByRegister(register))
            throw new ObjectNotFoundException(register);

        try {
            return examinationRepository
                .findByRegister(register.toString())
                .stream()
                .map(ExaminationMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching examinations: " + e.getMessage(), e);
        }
    }


    /**
     * Get all examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return List<ExaminationDto>
     * @throws IllegalArgumentException if the unique code is blank
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public List<ExaminationDto> getExaminationsByProfessorUniqueCode(UniqueCode uniqueCode)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        if(uniqueCode.toString().isBlank())
            throw new IllegalArgumentException("Unique Code cannot be null or empty");

        if (!professorRepository.existsByUniqueCode(uniqueCode))
            throw new ObjectNotFoundException(uniqueCode);

        try {
            Set<Course> courses = courseRepository.findByProfessor(uniqueCode);
            List<ExaminationDto> examinationsDto = new ArrayList<>();

            courses.forEach(course -> examinationsDto.addAll(
                examinationRepository
                    .findByCourse_Id_Id(course.getId().getId())
                    .stream()
                    .map(ExaminationMapper::toDto)
                    .toList()
            ));

            return examinationsDto;
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching examinations: " + e.getMessage(), e);
        }
    }


    /**
     * Add new examination
     *
     * @param request the examination data transfer object containing the details of the examination to be added
     * @return Examination data transfer object
     * @throws IllegalArgumentException for many kind of errors
     * @throws ObjectNotFoundException if any referenced entity is not found
     * @throws ObjectAlreadyExistsException if the examination already exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class, ObjectAlreadyExistsException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationDto addNewExamination(@Valid ExaminationDto request)
        throws IllegalArgumentException, ObjectNotFoundException, ObjectAlreadyExistsException, DataAccessServiceException
    {

        if (request.isWithHonors() && request.getGrade() != 30)
            throw new IllegalArgumentException("Honors can only be given if the grade is 30.");

        try {
            // --- Load domain entities ---
            String register = request.getRegister();

            Student student = studentRepository
                .findByRegister(new Register(register))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT));

            DegreeCourse degreeCourse = degreeCourseRepository
                .findByName(request.getDegreeCourseName().toUpperCase())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE));

            Course course = courseRepository
                .findByNameAndDegreeCourseName(request.getCourseName(), request.getDegreeCourseName().toUpperCase())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

            // --- Domain validations ---
            if (!degreeCourse.getStudents().contains(student))
                throw new IllegalStateException("Student must be enrolled in the specified degree course.");

            if (student.getStudyPlan() == null || !student.getStudyPlan().getCourses().contains(course))
                throw new ObjectNotFoundException("Course is not part of the student's study plan.");

            boolean alreadyExists = examinationRepository
                .findByRegister(register)
                .stream()
                .anyMatch(exam -> exam.getCourse().equals(course));

            if (alreadyExists)
                throw new ObjectAlreadyExistsException("Examination already exists for this student and course.");

            // --- Create & persist ---
            Examination examination = new Examination(course, student, request.getGrade(), request.isWithHonors(), request.getDate());
            return ExaminationMapper.toDto(examinationRepository.saveAndFlush(examination));

        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for adding new examination: " + e.getMessage(), e);
        }
    }



    /**
     * Update existing examination
     *
     * @param request the examination data transfer object containing the details of the examination to be updated
     * @return Examination data transfer object
     * @throws IllegalArgumentException for many kind of errors
     * @throws ObjectNotFoundException if any referenced entity is not found
     * @throws IllegalStateException if the student is not part of the degree course
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class, IllegalStateException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationDto updateExamination(@Valid UpdateExaminationDto request)
        throws IllegalArgumentException, ObjectNotFoundException, IllegalStateException, DataAccessServiceException
    {

        // --- Parse & validate grade ---
        int parsedGrade;
        try {
            parsedGrade = Integer.parseInt(request.getGrade());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Grade must be a valid integer.", e);
        }

        if (parsedGrade < 0 || parsedGrade > 30)
            throw new IllegalArgumentException("Grade must be between 0 and 30.");

        if (request.isWithHonors() && parsedGrade != 30)
            throw new IllegalArgumentException("Honors can only be assigned for grade 30.");

        if (request.getDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date cannot be in the future.");

        try {
            // --- Load updated entities ---
            String oldRegister = request.getOldRegister();
            String newRegister = request.getNewRegister();

            Student newStudent = studentRepository.findByRegister(new Register(newRegister))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT));

            DegreeCourse newDegreeCourse = degreeCourseRepository.findByName(request.getNewDegreeCourseName().toUpperCase())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE));

            Course newCourse = courseRepository.findByNameAndDegreeCourseName(
                    request.getNewCourseName(),
                    request.getNewDegreeCourseName().toUpperCase())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

            Course oldCourse = courseRepository.findByNameAndDegreeCourseName(
                    request.getOldCourseName(),
                    request.getOldDegreeCourseName().toUpperCase())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));


            // --- Retrieve old examination ---
            Examination examination = examinationRepository
                .findByCourse_Id_Id(oldCourse.getId().getId())
                .stream()
                .filter(e -> e.getCourse().getDegreeCourse().getName().equals(request.getOldDegreeCourseName()))
                .filter(e -> e.getRegister().equals(oldRegister))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.EXAMINATION));

            // --- Validate domain constraints ---
            if (!newDegreeCourse.getStudents().contains(newStudent))
                throw new IllegalStateException("Student must be part of the specified degree course.");

            if (newStudent.getStudyPlan() == null || !newStudent.getStudyPlan().getCourses().contains(newCourse))
                throw new ObjectNotFoundException("Course is not part of the student's study plan.");

            // --- Update and save ---
            examination.setStudentSnapshot(newStudent);
            examination.setCourse(newCourse);
            examination.setGrade(parsedGrade);
            examination.setWithHonors(request.isWithHonors());
            examination.setDate(request.getDate());

            return ExaminationMapper.toDto(examinationRepository.saveAndFlush(examination));

        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for updating examination: " + e.getMessage(), e);
        }
    }



    /**
     * Delete existing examination
     * @param register register of the student
     * @param courseName name of the course
     * @param degreeCourseName name of the degree course
     * @return Examination data transfer object
     * @throws IllegalArgumentException if the course name is null or empty
     *         or the register is null or empty
     * @throws ObjectNotFoundException if the examination does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
	@Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public ExaminationDto deleteExamination(String register, String courseName, String degreeCourseName)
		throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
	{
        if(register.isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if(courseName.isBlank())
            throw new IllegalArgumentException(COURSE_NAME_BLANK_ERROR);

        if (degreeCourseName.isBlank())
            throw new IllegalArgumentException(DEGREE_COURSE_NAME_BLANK_ERROR);

        Course course = courseRepository
            .findByNameAndDegreeCourseName(courseName, degreeCourseName.toUpperCase())
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

        try {
            Examination examination = examinationRepository
                .findByCourse_Id_Id(course.getId().getId())
                .stream()
                .filter(exam -> !exam.getRegister().equals(register))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Examination of course " + courseName + " and student register " + register));

            examinationRepository.delete(examination);
            return ExaminationMapper.toDto(examination);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("data access error while deleting examination of course " + courseName + " and student register " + register, e);
        }
    }

}

