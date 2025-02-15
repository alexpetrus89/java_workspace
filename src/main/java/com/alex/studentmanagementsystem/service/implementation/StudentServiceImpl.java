package com.alex.studentmanagementsystem.service.implementation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.StudentMapper;
import com.alex.studentmanagementsystem.repository.DegreeCourseRepository;
import com.alex.studentmanagementsystem.repository.StudentRepository;
import com.alex.studentmanagementsystem.service.StudentService;

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
	 * @param Register register the register of the student.
	 * @return StudentDto object containing the student's data.
	 * @throws ObjectNotFoundException if the student does not exist.
	 * @throws NullPointerException if the register is null.
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
	 * @param String name the name of the student.
	 * @return StudentDto object containing the student's data.
	 * @throws ObjectNotFoundException if no student with the given name exists.
	 * @throws NullPointerException if the name is null.
	 */
	@Override
	public StudentDto getStudentByName(String name)
		throws ObjectNotFoundException
	{
		return studentRepository
			.findByName(name)
			.map(StudentMapper::mapToStudentDto)
			.orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_STUDENT_IDENTIFIER));
	}


	/**
	 * Adds a new student to the repository.
	 * @param StudentDto studentDto the student data transfer object containing
	 * 					 the details of the student to be added.
	 * @throws ObjectAlreadyExistsException if a student with the same register
	 * 										already exists in the repository.
	 * @throws ObjectNotFoundException if the degree course does not exist.
	 * @throws IllegalArgumentException if the given register is null or empty.
	 */
	@Override
	@Transactional
    public void addNewStudent(StudentDto studentDto)
		throws ObjectAlreadyExistsException
	{
		if(studentRepository.existsByRegister(studentDto.getRegister()))
			throw new ObjectAlreadyExistsException(studentDto.getRegister());

		if(!degreeCourseRepository.existsByName(studentDto.getDegreeCourse().getName()))
			throw new ObjectNotFoundException(studentDto.getDegreeCourse().getName(), EXCEPTION_DEGREE_COURSE_IDENTIFIER);

		studentRepository.saveAndFlush(StudentMapper.mapToStudent(studentDto));
    }


	/**
	 * Updates an existing student's information.
	 * @param newStudentDto the data transfer object containing the new details of the
	 * 						student to be updated.
	 * @throws ObjectNotFoundException if no student with the given register exists in
	 * 								   the repository or if the specified degree course
	 *  							   does not exist.
	 * @throws NullPointerException if the newStudentDto is null.
	 * @throws IllegalArgumentException if the given register is null or empty.
	 */
	@Override
	@Transactional
    public void updateStudent(StudentDto newStudentDto)
		throws ObjectNotFoundException
	{
		Student updatableStudent = studentRepository
			.findByRegister(newStudentDto.getRegister())
			.orElseThrow(() -> new ObjectNotFoundException(newStudentDto.getRegister()));

		// new name, email and dob
		String newName = newStudentDto.getName();
		String newEmail = newStudentDto.getEmail();
		LocalDate newDob = newStudentDto.getDob();
		DegreeCourse newDegreeCourse = degreeCourseRepository
			.findByName(newStudentDto.getDegreeCourse().getName())
			.orElseThrow(() -> new ObjectNotFoundException(newStudentDto.getDegreeCourse().getName(), EXCEPTION_DEGREE_COURSE_IDENTIFIER));

		// update
		if(newName != null && !newName.isEmpty())
			updatableStudent.setName(newName);
		if(newEmail != null && !newEmail.isEmpty())
			updatableStudent.setEmail(newEmail);
		if(newDob != null && newDob != java.time.LocalDate.now())
			updatableStudent.setDob(newDob);

		updatableStudent.setDegreeCourse(newDegreeCourse);

		// save
		studentRepository.saveAndFlush(updatableStudent);
    }


	/**
	 * Deletes a student from the repository.
	 * @param Register register the register of the student to be deleted.
	 * @throws ObjectNotFoundException if no student with the given register exists in
	 * 								   the repository.
	 * @throws NullPointerException if the register is null.
	 * @throws IllegalArgumentException if the given register is empty.
	 */
	@Override
	@Transactional
	public void deleteStudent(@NonNull Register register)
		throws ObjectNotFoundException
	{
		if(!studentRepository.existsByRegister(register))
			throw new ObjectNotFoundException(register);

		studentRepository.deleteByRegister(register);
    }

}
