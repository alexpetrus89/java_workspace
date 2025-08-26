package com.alex.universitymanagementsystem.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Address;
import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.service.StudentService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;




@Service
public class StudentServiceImpl implements StudentService {

	// inject repository - instance variable
	private final StudentRepository studentRepository;
	private final DegreeCourseRepository degreeCourseRepository;
	private final StudyPlanRepository studyPlanRepository;
	private final PasswordEncoder passwordEncoder;


	// autowired - dependency injection - constructor
	public StudentServiceImpl(
		StudentRepository studentRepository,
		DegreeCourseRepository degreeCourseRepository,
		StudyPlanRepository studyPlanRepository,
		PasswordEncoder passwordEncoder
	) {
		this.studentRepository = studentRepository;
		this.degreeCourseRepository = degreeCourseRepository;
		this.studyPlanRepository = studyPlanRepository;
		this.passwordEncoder = passwordEncoder;
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
		// sanity check
		if(register.toString().isBlank())
			throw new IllegalArgumentException("Register cannot be null or empty.");

		try {
			return studentRepository
				.findByRegister(register)
				.map(StudentMapper::toDto)
				.orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT));
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
		if(fullname.isBlank())
			throw new IllegalArgumentException("Name cannot be null or empty.");

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
	 * @return StudentDto object containing the added student's data.
	 * @throws IllegalArgumentException if the form is invalid.
	 * @throws ObjectAlreadyExistsException if a student with the same register
	 * 		   already or with same name and dob exists in the repository.
	 * @throws ObjectNotFoundException if the degree course does not exists
	 * 		   in the repository.
	 * @throws DataAccessServiceException if there is an error accessing the database
	 */
	@Override
	@Transactional(rollbackOn = {IllegalArgumentException.class, ObjectAlreadyExistsException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public StudentDto addNewStudent(RegistrationForm form, DegreeCourse degreeCourse, String ordering)
		throws IllegalArgumentException, ObjectAlreadyExistsException, ObjectNotFoundException, DataAccessServiceException
	{
		try {

			Student student = form.toStudent(passwordEncoder);
			FiscalCode fiscalCode = student.getFiscalCode();
			Register register = student.getRegister();

			// sanity checks
			if(studentRepository.existsByFiscalCode(fiscalCode))
				throw new ObjectAlreadyExistsException(fiscalCode);

			// check if register is unique
			if(studentRepository.existsByRegister(register))
				throw new ObjectAlreadyExistsException(register);

			// check if degree course exists
			if(!degreeCourseRepository.existsByName(degreeCourse.getName()))
				throw new ObjectNotFoundException(DomainType.DEGREE_COURSE);

			// check if ordering is valid
			if(ordering == null || ordering.isBlank())
				ordering = "ORD270";

			// set the degree course
            student.setDegreeCourse(degreeCourse);
            // set the study plan
			Set<Course> courses = degreeCourse.getCourses().isEmpty() ? new HashSet<>() : new HashSet<>(degreeCourse.getCourses());
			student.setStudyPlan(new StudyPlan(student, ordering, courses));

			// save the student
			studentRepository.saveAndFlush(student);
			// save the study plan
			studyPlanRepository.saveAndFlush(student.getStudyPlan());
			// return the student as DTO
			return StudentMapper.toDto(student);
		} catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }


	/**
	 * Updates an existing student's information.
	 * @param form with new data of the student to be updated
	 * @throws IllegalArgumentException if the form is invalid.
	 * @throws ObjectNotFoundException if no student with the given register
	 * 		   exists in the repository or if the specified degree course
	 *         does not exist.
	 * @throws DataAccessServiceException if there is an error accessing the database
	 */
	@Override
	@Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public StudentDto updateStudent(RegistrationForm form)
		throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
	{
		try {
			// retrieve data
			Student updatableStudent = (Student) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();

			// sanity checks
			if(updatableStudent == null)
				throw new ObjectNotFoundException(DomainType.STUDENT);

			// update
			updatableStudent.setUsername(form.getUsername());
			updatableStudent.setFirstName(form.getFirstName());
			updatableStudent.setLastName(form.getLastName());
			updatableStudent.setDob(form.getDob());
			updatableStudent.setFiscalCode(new FiscalCode(form.getFiscalCode()));
			updatableStudent.setPhoneNumber(form.getPhone());
			updatableStudent.setAddress(new Address(form.getStreet(), form.getCity(), form.getState(), form.getZip()));

			// save
			studentRepository.saveAndFlush(updatableStudent);
			return StudentMapper.toDto(updatableStudent);
		} catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }


}
