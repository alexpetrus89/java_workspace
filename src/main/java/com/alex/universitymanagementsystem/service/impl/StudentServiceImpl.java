package com.alex.universitymanagementsystem.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.entity.Course;
import com.alex.universitymanagementsystem.entity.DegreeCourse;
import com.alex.universitymanagementsystem.entity.Examination;
import com.alex.universitymanagementsystem.entity.ExaminationAppeal;
import com.alex.universitymanagementsystem.entity.Student;
import com.alex.universitymanagementsystem.entity.StudyPlan;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.service.StudentService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;




@Service
public class StudentServiceImpl implements StudentService {

	// logger
	private final Logger logger =
        LoggerFactory.getLogger(StudentServiceImpl.class);

	// constants
    private static final String REGISTER_ERROR = "Register cannot be null or empty";

	// inject repository - instance variable
	private final StudentRepository studentRepository;
	private final DegreeCourseRepository degreeCourseRepository;
	private final ExaminationRepository examinationRepository;
	private final ExaminationAppealRepository examinationAppealRepository;
	private final StudyPlanRepository studyPlanRepository;
	private final PasswordEncoder passwordEncoder;
	private final ServiceHelpers helpers;
    private final ServiceValidators validators;


	// autowired - dependency injection - constructor
	public StudentServiceImpl(
		StudentRepository studentRepository,
		DegreeCourseRepository degreeCourseRepository,
		ExaminationRepository examinationRepository,
		ExaminationAppealRepository examinationAppealRepository,
		StudyPlanRepository studyPlanRepository,
		PasswordEncoder passwordEncoder,
		ServiceHelpers helpers,
		ServiceValidators validators
	) {
		this.studentRepository = studentRepository;
		this.degreeCourseRepository = degreeCourseRepository;
		this.examinationRepository = examinationRepository;
		this.examinationAppealRepository = examinationAppealRepository;
		this.studyPlanRepository = studyPlanRepository;
		this.passwordEncoder = passwordEncoder;
		this.helpers = helpers;
		this.validators = validators;
	}


	/**
	 * Retrieves all students.
	 * @return List of StudentDto objects representing all students.
	 * @throws DataAccessServiceException if there is an error accessing the database.
	 */
	@Override
    public List<StudentDto> getStudents() throws DataAccessServiceException {
		try {
			return studentRepository
				.findAll()
				.stream()
				.map(StudentMapper::toDto)
				.toList();
		} catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching students: " + e.getMessage(), e);
        }
	}


	/**
	 * Retrieves a student by register.
	 * @param register the register of the student.
	 * @return StudentDto object containing the student's data.
	 * @throws IllegalArgumentException if the register is null or blank.
	 * @throws ObjectNotFoundException if no student found
	 * @throws DataAccessServiceException if there is an error accessing the database.
	 */
	@Override
	public StudentDto getStudentByRegister(String register)
		throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
	{
		validators.validateNotNullOrNotBlank(register, REGISTER_ERROR);

		try {
			return StudentMapper.toDto(helpers.fetchStudent(register));
		} catch (PersistenceException e) {
			throw new DataAccessServiceException("Error accessing database for fetching student by register: " + e.getMessage(), e);
		}
	}


	/**
	 * Retrieves a student by fullname.
	 * @param fullname the name of the student.
	 * @return List<StudentDto> List of StudentDto object containing the
	 * 		   student's data.
	 * @throws IllegalArgumentException if the fullname is null or blank.
	 * @throws DataAccessServiceException if there is an error accessing the database.
	 */
	@Override
	public List<StudentDto> getStudentsByFullname(String fullname)
		throws IllegalArgumentException, DataAccessServiceException
	{
		validators.validateNotNullOrNotBlank(fullname, "Fullname cannot be null or empty");

		String[] nameParts = fullname.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

		try {
			return studentRepository
				.findByFullname(firstName, lastName)
				.stream()
				.map(StudentMapper::toDto)
				.toList();
		} catch (PersistenceException e) {
			throw new DataAccessServiceException("Error accessing database for fetching students by fullname: " + e.getMessage(), e);
		}
	}


	/**
	 * Adds a new student to the repository.
	 * @param form with data of the student to be added
	 * @param degreeCourse degree course of the new student
	 * @param ordering the ordering of the courses in the study plan
	 * @return Optional<StudentDto> object containing the added student's data.
	 * @throws IllegalArgumentException if the form is invalid.
	 * @throws ObjectAlreadyExistsException if a student with the same register
	 * 		   already exists in the repository.
	 * @throws ObjectNotFoundException if the degree course does not exists
	 * 		   in the repository.
	 * @throws DataAccessServiceException if there is an error accessing the database
	 */
	@Override
	@Transactional(rollbackOn = {IllegalArgumentException.class, ObjectAlreadyExistsException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<StudentDto> addNewStudent(RegistrationForm form, DegreeCourse degreeCourse, String ordering)
		throws IllegalArgumentException, ObjectAlreadyExistsException, ObjectNotFoundException, DataAccessServiceException
	{
		Student student = form.toStudent(passwordEncoder);

		// check if student already exists
		validators.validateStudentAlreadyExists(student.getRegister());
		// check if degree course exists
		validators.validateDegreeCourseExists(degreeCourse.getName());

		try {

			// check if ordering is valid
			ordering = Optional.ofNullable(ordering)
				.filter(s -> !s.isBlank())
				.orElse("ORD270");

			// set the degree course
            student.setDegreeCourse(degreeCourse);

            // set the study plan
			student.setStudyPlan(new StudyPlan(student, ordering, new HashSet<>(degreeCourse.getCourses())));

			// save the student
			studentRepository.saveAndFlush(student);
			// save the study plan
			studyPlanRepository.saveAndFlush(student.getStudyPlan());
			// return the student as DTO
			return Optional.of(StudentMapper.toDto(student));
		} catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }


	/**
	 * Deletes the relationship between a student and their associated entities.
	 * @param student the student whose relationships are to be deleted
	 * @return
	 */
	@Override
	@Transactional
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public void deleteStudentRelationship(Student student) {
        // 1. Remove the student's StudyPlan
        Optional.ofNullable(student.getStudyPlan()).ifPresent(studyPlanRepository::delete);
        // 2. Remove the student's Register from all ExaminationAppeals
        List<ExaminationAppeal> appealsToUpdate = examinationAppealRepository
			.findAll()
			.stream()
			.filter(appeal -> appeal.getRegisters().contains(student.getRegister()))
			.map(appeal -> {
				appeal.getRegisters().remove(student.getRegister());
				return appeal;
			})
			.toList();

		examinationAppealRepository.saveAll(appealsToUpdate);
    }


	/**
 	 * Moves a student to another degree course, removing all examinations
 	 * that do not belong to the new degree course.
     *
 	 * @param register unique student register
 	 * @param newDegreeCourseName name of the new degree course
 	 * @throws ObjectNotFoundException if the student or degree course are not found
 	 * @throws DataAccessServiceException if database access fails
 	 */
	@Override
	@Transactional(rollbackOn = { ObjectNotFoundException.class, DataAccessServiceException.class })
	@Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public boolean changeDegreeCourse(String register, String degreeCourse)
        throws ObjectNotFoundException, DataAccessServiceException {

		validators.validateNotNullOrNotBlank(register, REGISTER_ERROR);
		validators.validateDegreeCourseExists(degreeCourse);

		try {

			Student student = helpers.fetchStudent(register);
			DegreeCourse newDegreeCourse = helpers.fetchDegreeCourse(degreeCourse);
			DegreeCourse oldDegreeCourse = helpers.fetchDegreeCourse(student.getDegreeCourse().getName());

			if(!newDegreeCourse.getGraduationClass().equals(oldDegreeCourse.getGraduationClass()))
				return false;

			Set<Course> allowedCourses = new HashSet<>(newDegreeCourse.getCourses());
			Collection<Examination> allExaminations = helpers.fetchExaminations(register);
			List<Examination> invalidExaminations = allExaminations
				.stream()
				.filter(exam -> !allowedCourses.contains(exam.getCourse()))
				.toList();

			if (!invalidExaminations.isEmpty()) {
				examinationRepository.deleteAll(invalidExaminations);
				examinationRepository.flush();
			}

			student.setDegreeCourse(newDegreeCourse);
			oldDegreeCourse.removeStudent(student);
			newDegreeCourse.addStudent(student);

			studentRepository.saveAndFlush(student);
			degreeCourseRepository.saveAndFlush(oldDegreeCourse);
			degreeCourseRepository.saveAndFlush(newDegreeCourse);

			logger.info("Student {} moved to degree course '{}'. {} examinations removed.",
                register, degreeCourse, invalidExaminations.size());

			return true;

		} catch (PersistenceException | ObjectNotFoundException e) {
			logger.error("Error changing degree course for student {}: {}", register, e.getMessage(), e);
			return false;
		}
	}


}
