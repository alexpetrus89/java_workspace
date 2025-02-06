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
public class StudentServiceImplementation
	implements StudentService
{
	// constants
	private static final String EXCEPTION_STUDENT_IDENTIFIER = "student";
	private static final String EXCEPTION_DEGREE_COURSE_IDENTIFIER = "degree course";

	// inject repository - instance variable
	private final StudentRepository studentRepository;
	private final DegreeCourseRepository degreeCourseRepository;

	// autowired - dependency injection - constructor
	public StudentServiceImplementation(
		StudentRepository studentRepository,
		DegreeCourseRepository degreeCourseRepository
	) {
		this.studentRepository = studentRepository;
		this.degreeCourseRepository = degreeCourseRepository;
	}


	/**
	 * @return List<StudentDto>
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
	 * @param Register studentRegister
	 * @return StudentDto
	 * @throws ObjectNotFoundException without identifier
	 */
	@Override
	public StudentDto getStudentByRegister(Register register)
		throws ObjectNotFoundException
	{
		Student student = studentRepository
			.findByRegister(register)
			.orElseThrow(() -> new ObjectNotFoundException(register));

		return StudentMapper.mapToStudentDto(student);
	}


	/**
	 * @param String name
	 * @return StudentDto
	 * @throws ObjectNotFoundException with identifier
	 */
	@Override
	public StudentDto getStudentByName(String name)
		throws ObjectNotFoundException
	{
		Student student = studentRepository
			.findByName(name)
			.orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_STUDENT_IDENTIFIER));

		return StudentMapper.mapToStudentDto(student);
	}


	/**
	 * @param StudentDto studentDto
	 * @throws ObjectAlreadyExistsException without identifier
	 */
	@Override
	@Transactional
    public void addNewStudent(StudentDto studentDto)
		throws ObjectAlreadyExistsException
	{
		if(studentRepository.existsByRegister(studentDto.getRegister()))
			throw new ObjectAlreadyExistsException(studentDto.getRegister());

		studentRepository.save(StudentMapper.mapToStudent(studentDto));
    }


	/**
	 * @param StudentDto newStudentDto
	 * @throws ObjectNotFoundException without identifier
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
	 * @param Register register
	 * @throws ObjectNotFoundException
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
