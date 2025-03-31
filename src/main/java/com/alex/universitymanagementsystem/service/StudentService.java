package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;


public interface StudentService {

    /**
     * Return all students
     * @return List<StudentDto>
     */
    List<StudentDto> getStudents();

    /**
	 * Retrieves a student by register.
	 * @param register the register of the student.
	 * @return StudentDto object containing the student's data.
	 * @throws NullPointerException if the register is null
	 * @throws IllegalArgumentException if the register is null.
	 * @throws UnsupportedOperationException if the register is not unique
	 */
	StudentDto getStudentByRegister(@NonNull Register register)
		throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;

    /**
	 * Retrieves a student by fullname.
	 * @param fullname the name of the student.
	 * @return List<StudentDto> List of StudentDto object containing the
	 * 		   student's data.
	 * @throws NullPointerException if the name is null
	 * @throws IllegalArgumentException if the name is blank
	 * @throws UnsupportedOperationException if the fullname is not unique
	 */

	List<StudentDto> getStudentsByFullname(@NonNull String fullname)
		throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;

    /**
	 * Adds a new student to the repository.
	 * @param student the student to be added
	 * @throws NullPointerException if the student is null
	 * @throws IllegalArgumentException if the given register is null or blank
	 * @throws ObjectAlreadyExistsException if a student with the same register
	 * 										already exists in the repository.
	 * @throws ObjectNotFoundException if the degree course does not exists
	 */
	@Transactional
    void addNewStudent(@NonNull Student student)
		throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException, ObjectNotFoundException;

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
	@Transactional
    void updateStudent(@NonNull StudentDto studentDto)
		throws NullPointerException, IllegalArgumentException, ObjectNotFoundException;

    /**
	 * Deletes a student from the repository.
	 * @param register the register of the student to be deleted.
	 * @throws ObjectNotFoundException if no student with the given register exists in
	 * 								   the repository.
	 * @throws NullPointerException if the register is null.
	 * @throws IllegalArgumentException if the given register is empty.
	 * @throws UnsupportedOperationException if the register is not unique
	 */
	@Transactional
	@Retryable(retryFor = DataAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	void deleteStudent(@NonNull Register register)
		throws NullPointerException, IllegalArgumentException, ObjectNotFoundException;

}
