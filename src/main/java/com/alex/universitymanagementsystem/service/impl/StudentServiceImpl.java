package com.alex.universitymanagementsystem.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.service.StudentService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;




@Service
public class StudentServiceImpl implements StudentService {

	// constants
    private static final String REGISTER_ERROR = "Register cannot be null or empty";

	// inject repository - instance variable
	private final StudentRepository studentRepository;
	private final ExaminationAppealRepository examinationAppealRepository;
	private final StudyPlanRepository studyPlanRepository;
	private final PasswordEncoder passwordEncoder;
	private final ServiceHelpers helpers;
    private final ServiceValidators validators;


	// autowired - dependency injection - constructor
	public StudentServiceImpl(
		StudentRepository studentRepository,
		ExaminationAppealRepository examinationAppealRepository,
		StudyPlanRepository studyPlanRepository,
		PasswordEncoder passwordEncoder,
		ServiceHelpers helpers,
		ServiceValidators validators
	) {
		this.studentRepository = studentRepository;
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
	public StudentDto getStudentByRegister(Register register)
		throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
	{
		validators.validateNotNullOrNotBlank(register.toString(), REGISTER_ERROR);

		try {
			return StudentMapper.toDto(helpers.fetchStudent(register.toString()));
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


}
