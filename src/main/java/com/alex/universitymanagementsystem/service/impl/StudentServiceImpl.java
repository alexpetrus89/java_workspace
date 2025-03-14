package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.StudentService;

import jakarta.transaction.Transactional;


@Service
public class StudentServiceImpl implements StudentService {

	// constants
	private static final String EXCEPTION_STUDENT_IDENTIFIER = "student";
	private static final String EXCEPTION_DEGREE_COURSE_IDENTIFIER = "degree course";

	// inject repository - instance variable
	private final StudentRepository studentRepository;
	private final DegreeCourseRepository degreeCourseRepository;

	// autowired - dependency injection - constructor
	public StudentServiceImpl(
		StudentRepository studentRepository,
		DegreeCourseRepository degreeCourseRepository
	) {
		this.studentRepository = studentRepository;
		this.degreeCourseRepository = degreeCourseRepository;
	}


	/**
	 * Retrieves all students.
	 * @return List of StudentDto objects representing all students.
	 */
	@Override
    public List<StudentDto> getStudents() {
		return studentRepository
			.findAll()
            .stream()
            .map(StudentMapper::mapToStudentDto)
            .toList();
	}

	/**
	 * Retrieves a student by register.
	 * @param register the register of the student.
	 * @return StudentDto object containing the student's data.
	 * @throws ObjectNotFoundException if the student does not exist.
	 * @throws IllegalArgumentException if the register is null.
	 * @throws UnsupportedOperationException if the register is not unique
	 */
	@Override
	public StudentDto getStudentByRegister(@NonNull Register register)
		throws ObjectNotFoundException
	{
		return studentRepository
			.findByRegister(register)
			.map(StudentMapper::mapToStudentDto)
			.orElseThrow(() -> new ObjectNotFoundException(register));
	}


	/**
	 * Retrieves a student by name.
	 * @param fullname the name of the student.
	 * @return List<StudentDto> List of StudentDto object containing the
	 * 		   student's data.
	 * @throws ObjectNotFoundException if no student with the given name exists.
	 * @throws IllegalArgumentException if the name is null.
	 * @throws UnsupportedOperationException if the name is not unique
	 */
	@Override
	public List<StudentDto> getStudentsByFullname(@NonNull String fullname)
		throws ObjectNotFoundException
	{
		return studentRepository
			.findByFullname(fullname)
			.orElseThrow(() -> new ObjectNotFoundException(fullname, EXCEPTION_STUDENT_IDENTIFIER))
			.stream()
			.map(StudentMapper::mapToStudentDto)
			.toList();
	}


	/**
	 * Adds a new student to the repository.
	 * @param studentDto the student data transfer object containing
	 * 					 the details of the student to be added.
	 * @throws ObjectAlreadyExistsException if a student with the same register
	 * 										already exists in the repository.
	 * @throws ObjectNotFoundException if the degree course does not exist.
	 * @throws IllegalArgumentException if the given register is null or empty
	 */
	@Override
	@Transactional
    public void addNewStudent(@NonNull Student student)
		throws ObjectAlreadyExistsException
	{
		Register register = student.getRegister();

		// sanity check
		if(register == null || register.toString().isEmpty())
			throw new IllegalArgumentException("Register cannot be null or empty.");

		if(studentRepository.existsByRegister(register))
			throw new ObjectAlreadyExistsException(register);

		// save
		studentRepository.saveAndFlush(student);
    }


	/**
	 * Updates an existing student's information.
	 * @param studentDto the data transfer object containing the new
	 * 					 details of the student to be updated.
	 * @throws ObjectNotFoundException if no student with the given register exists
	 *                                 in the repository or if the specified degree
	 *                                 course does not exist.
	 * @throws IllegalArgumentException if the newStudentDto is null.
	 * @throws UnsupportedOperationException if the register is not unique or if
	 * 										the degree course is not unique.
	 */
	@Override
	@Transactional
    public void updateStudent(@NonNull StudentDto studentDto)
		throws ObjectNotFoundException
	{
		Student updatableStudent = studentRepository
			.findByRegister(studentDto.getRegister())
			.orElseThrow(() -> new ObjectNotFoundException(studentDto.getRegister()));

		// new name, email and dob
		String newFullname = studentDto.getFullname();
		String newUsername = studentDto.getUsername();
		LocalDate newDob = studentDto.getDob();
		DegreeCourse newDegreeCourse = degreeCourseRepository
			.findByName(studentDto.getDegreeCourse().getName())
			.orElseThrow(() -> new ObjectNotFoundException(studentDto.getDegreeCourse().getName(), EXCEPTION_DEGREE_COURSE_IDENTIFIER));

		// update
		if(newFullname != null && !newFullname.isEmpty())
			updatableStudent.setFullname(newFullname);
		if(newUsername != null && !newUsername.isEmpty())
			updatableStudent.setFullname(newUsername);
		if(newDob != null && newDob != java.time.LocalDate.now())
			updatableStudent.setDob(newDob);
		updatableStudent.setDegreeCourse(newDegreeCourse);

		// save
		studentRepository.saveAndFlush(updatableStudent);
    }


	/**
	 * Deletes a student from the repository.
	 * @param register the register of the student to be deleted.
	 * @throws ObjectNotFoundException if no student with the given register exists in
	 * 								   the repository.
	 * @throws IllegalArgumentException if the given register is empty.
	 */
	@Override
	@Transactional
	public void deleteStudent(@NonNull Register register)
		throws ObjectNotFoundException
	{
		// sanity check
		if(!studentRepository.existsByRegister(register))
			throw new ObjectNotFoundException(register);

		// delete
		studentRepository.deleteByRegister(register);
    }

}
