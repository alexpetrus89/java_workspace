package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.service.StudentService;



@Service
public class StudentServiceImpl implements StudentService {

	// logger
	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";

	// inject repository - instance variable
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final DegreeCourseRepository degreeCourseRepository;
	private final StudyPlanRepository studyPlanRepository;

	// autowired - dependency injection - constructor
	public StudentServiceImpl(
		StudentRepository studentRepository,
		UserRepository userRepository,
		DegreeCourseRepository degreeCourseRepository,
		StudyPlanRepository studyPlanRepository
	) {
		this.studentRepository = studentRepository;
		this.userRepository = userRepository;
		this.degreeCourseRepository = degreeCourseRepository;
		this.studyPlanRepository = studyPlanRepository;
	}


	/**
	 * Retrieves all students.
	 * @return List of StudentDto objects representing all students.
	 */
	@Override
    public List<StudentDto> getStudents() {
		try {
			return studentRepository
				.findAll()
				.stream()
				.map(StudentMapper::mapToStudentDto)
				.toList();
		} catch (DataAccessException e) {
			logger.error(DATA_ACCESS_ERROR, e);
			return Collections.emptyList();
		}
	}

	/**
	 * Retrieves a student by register.
	 * @param register the register of the student.
	 * @return StudentDto object containing the student's data.
	 * @throws NullPointerException if the register is null
	 * @throws IllegalArgumentException if the register is blank.
	 * @throws UnsupportedOperationException if the register is not unique
	 */
	@Override
	public StudentDto getStudentByRegister(@NonNull Register register)
		throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
	{
		// sanity check
		if(register.toString().isBlank())
			throw new IllegalArgumentException("Register cannot be null or empty.");

		try {
			return StudentMapper.mapToStudentDto(studentRepository.findByRegister(register));
		} catch (DataAccessException e) {
			logger.error(DATA_ACCESS_ERROR, e);
			return null;
		}
	}


	/**
	 * Retrieves a student by fullname.
	 * @param fullname the name of the student.
	 * @return List<StudentDto> List of StudentDto object containing the
	 * 		   student's data.
	 * @throws NullPointerException if the name is null
	 * @throws IllegalArgumentException if the name is blank
	 * @throws UnsupportedOperationException if the fullname is not unique
	 */
	@Override
	public List<StudentDto> getStudentsByFullname(@NonNull String fullname)
		throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
	{
		if(fullname.isBlank())
			throw new IllegalArgumentException("Name cannot be null or empty.");

		try {
			return studentRepository
				.findByFullname(fullname)
				.stream()
				.map(StudentMapper::mapToStudentDto)
				.toList();
		} catch (DataAccessException e) {
			logger.error(DATA_ACCESS_ERROR, e);
			return Collections.emptyList();
		}
	}


	/**
	 * Adds a new student to the repository.
	 * @param student the student to be added
	 * @throws NullPointerException if the student is null
	 * @throws IllegalArgumentException if the given register is null or blank
	 * @throws ObjectAlreadyExistsException if a student with the same register
	 * 		   already or with same name and dob exists in the repository.
	 * @throws ObjectNotFoundException if the degree course does not exists
	 */
	@Override
	@Transactional
    public void addNewStudent(@NonNull Student student)
		throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException, ObjectNotFoundException
	{
		try {

			Register register = student.getRegister();

			// sanity check
			if(register == null || register.toString().isBlank())
				throw new IllegalArgumentException("Register cannot be null or empty.");

			// check if register is unique
			if(studentRepository.existsByRegister(register))
				throw new ObjectAlreadyExistsException(register);

			// check if degree course exists
			if(!degreeCourseRepository.existsByName(student.getDegreeCourse().getName()))
				throw new ObjectNotFoundException(student.getDegreeCourse().getName());

			if(userRepository.findByFullname(student.getFullname())
                    .stream()
                    .anyMatch(u -> u.getFullname().equals(student.getFullname())) &&
                userRepository.findByDob(student.getDob())
                    .stream()
                    .anyMatch(u -> u.getDob().equals(student.getDob())))
                throw new ObjectAlreadyExistsException(DomainType.STUDENT);
			// save the student
			studentRepository.saveAndFlush(student);
			// save the study plan
			studyPlanRepository.saveAndFlush(student.getStudyPlan());
		} catch (DataAccessException e) {
			logger.error(DATA_ACCESS_ERROR + " while adding new student", e);
		}
    }


	/**
	 * Updates an existing student's information.
	 * @param studentDto the data transfer object containing the new
	 * 		  details of the student to be updated.
	 * @throws NullPointerException if the studentDto is null
	 * @throws IllegalArgumentException if the newStudentDto is null.
	 * @throws ObjectNotFoundException if no student with the given register
	 * 		   exists in the repository or if the specified degree course
	 *         does not exist.
	 * @throws UnsupportedOperationException if the register is not unique
	 *         or if the degree course is not unique.
	 */
	@Override
	@Transactional
    public void updateStudent(@NonNull StudentDto studentDto)
		throws NullPointerException, IllegalArgumentException, ObjectNotFoundException
	{
		try {
			// retrieve data
			Student updatableStudent = studentRepository.findByRegister(studentDto.getRegister());
			DegreeCourse newDegreeCourse = degreeCourseRepository.findByName(studentDto.getDegreeCourse().getName());

			// sanity checks
			if(updatableStudent == null)
				throw new ObjectNotFoundException(studentDto.getRegister());

			if(newDegreeCourse == null)
				throw new ObjectNotFoundException(studentDto.getDegreeCourse().getName());

			// retrieve new data
			String newUsername = studentDto.getUsername();
			String newFullname = studentDto.getFullname();
			LocalDate newDob = studentDto.getDob();

			// sanity checks
			if(newUsername == null || newUsername.isBlank())
				throw new IllegalArgumentException("Username cannot be null or empty.");
			if(newFullname == null || newFullname.isBlank())
				throw new IllegalArgumentException("Fullname cannot be null or empty.");
			if(newDob == null || newDob == java.time.LocalDate.now())
				throw new IllegalArgumentException("Dob cannot be null or empty.");

			// update
			updatableStudent.setUsername(newUsername);
			updatableStudent.setFullname(newFullname);
			updatableStudent.setDob(newDob);
			updatableStudent.setDegreeCourse(newDegreeCourse);

			// save
			studentRepository.saveAndFlush(updatableStudent);
		} catch (DataAccessException e) {
			logger.error(DATA_ACCESS_ERROR + " while updating student with register " + studentDto.getRegister(), e);
		}
    }


	/**
	 * Deletes a student from the repository.
	 * @param register the register of the student to be deleted.
	 * @throws ObjectNotFoundException if no student with the given register exists in
	 * 								   the repository.
	 * @throws NullPointerException if the register is null.
	 * @throws IllegalArgumentException if the given register is empty.
	 */
	@Override
	@Transactional
	@Retryable(retryFor = DataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public void deleteStudent(@NonNull Register register)
		throws NullPointerException, IllegalArgumentException, ObjectNotFoundException
	{
		try {
			// sanity checks
			if(register.toString().isBlank())
				throw new IllegalArgumentException("Register cannot be blank");

			if(!studentRepository.existsByRegister(register))
				throw new ObjectNotFoundException(register);

			// delete
			studentRepository.deleteByRegister(register);
		} catch (DataAccessException e) {
			logger.error(DATA_ACCESS_ERROR + " while deleting student with register " + register, e);
		}
	}

}
