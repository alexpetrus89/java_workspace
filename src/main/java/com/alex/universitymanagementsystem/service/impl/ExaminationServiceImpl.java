package com.alex.universitymanagementsystem.service.impl;

import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.UpdateExaminationDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.service.ExaminationService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Service
public class ExaminationServiceImpl implements ExaminationService {

	// constants
    private static final String REGISTER_ERROR = "Register cannot be null or empty";
    private static final String UNIQUE_CODE_ERROR = "Unique code cannot be null or empty";
    private static final String COURSE_NAME_ERROR = "Course name cannot be null or empty";
    private static final String DEGREE_COURSE_NAME_ERROR = "Degree course name cannot be null or empty";

    // instance variables
    private final CourseRepository courseRepository;
    private final ExaminationRepository examinationRepository;
    private final ServiceHelpers helpers;
    private final ServiceValidators validators;


    // autowired - dependency injection - constructor
    public ExaminationServiceImpl(
        CourseRepository courseRepository,
        ExaminationRepository examinationRepository,
        ServiceHelpers helpers,
        ServiceValidators validators
    ) {
        this.courseRepository = courseRepository;
        this.examinationRepository = examinationRepository;
        this.helpers = helpers;
        this.validators = validators;
    }


    /**
     * Get all examinations
     * @return List<ExaminationDto> a list of all examination data transfer objects
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public List<ExaminationDto> getExaminations() throws DataAccessServiceException {
        try {
            return helpers.mapExaminations(examinationRepository.findAll());
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
        // sanity checks
        validators.validateNotNullOrNotBlank(courseName, COURSE_NAME_ERROR);
        validators.validateNotNullOrNotBlank(degreeCourseName, DEGREE_COURSE_NAME_ERROR);

        Course course = helpers.fetchCourse(courseName, degreeCourseName);

        try {
            return helpers.mapExaminations(examinationRepository.findByCourse_Id_Id(course.getId().getId()));
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
        // sanity check
        validators.validateNotNullOrNotBlank(register.toString(), REGISTER_ERROR);
        validators.validateStudentExists(register);

        try {
            return helpers.mapExaminations(examinationRepository.findByRegister(register.toString()));
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
        // sanity check
        validators.validateNotNullOrNotBlank(uniqueCode.toString(), UNIQUE_CODE_ERROR);
        validators.validateProfessorExists(uniqueCode);

        try {
            return courseRepository
                .findByProfessor(uniqueCode)
                .stream()
                .flatMap(course -> examinationRepository
                    .findByCourse_Id_Id(course.getId().getId())
                    .stream()
                    .map(ExaminationMapper::toDto))
                .toList();
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
        // sanity check
        validators.validateGradeWithHonors(request.getGrade(), request.isWithHonors());

        try {
            // --- Fetch domain entities ---

            Student student = helpers.fetchStudent(request.getRegister());
            DegreeCourse degreeCourse = helpers.fetchDegreeCourse(request.getDegreeCourseName());
            Course course = helpers.fetchCourse(request.getCourseName(), request.getDegreeCourseName());

            // --- Domain validations ---
            if (!degreeCourse.getStudents().contains(student))
                throw new IllegalStateException("Student must be enrolled in the specified degree course.");

            if (student.getStudyPlan() == null || !student.getStudyPlan().getCourses().contains(course))
                throw new ObjectNotFoundException("Course is not part of the student's study plan.");

            if (examinationRepository
                    .findByRegister(request.getRegister())
                    .stream()
                    .anyMatch(e -> e.getCourse().equals(course)))
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
        int parsedGrade = validators.parseAndValidateGrade(request.getGrade(), request.isWithHonors(), request.getDate());

        try {
            // --- Load updated entities ---
            String oldRegister = request.getOldRegister();
            String newRegister = request.getNewRegister();

            Student newStudent = helpers.fetchStudent(newRegister);
            DegreeCourse newDegreeCourse = helpers.fetchDegreeCourse(request.getNewDegreeCourseName());
            Course newCourse = helpers.fetchCourse(request.getNewCourseName(), request.getNewDegreeCourseName());
            Course oldCourse = helpers.fetchCourse(request.getOldCourseName(), request.getOldDegreeCourseName());

            // --- Retrieve old examination ---
            Examination examination = helpers.findExistingExamination(oldCourse, oldRegister, request.getOldDegreeCourseName());

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
        // sanity checks
        validators.validateNotNullOrNotBlank(register, REGISTER_ERROR);
        validators.validateNotNullOrNotBlank(courseName, COURSE_NAME_ERROR);
        validators.validateNotNullOrNotBlank(degreeCourseName, DEGREE_COURSE_NAME_ERROR);

        Course course = helpers.fetchCourse(courseName, degreeCourseName);

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

