package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;



public interface StudentService {

    /**
     * Return all students
     * @return List<StudentDto>
	 * @throws DataAccessServiceException if there is an error accessing the database.
     */
    List<StudentDto> getStudents() throws DataAccessServiceException;


    /**
	 * Retrieves a student by register.
	 * @param register the register of the student.
	 * @return StudentDto object containing the student's data.
	 * @throws IllegalArgumentException if the register is null or blank.
	 * @throws ObjectNotFoundException if no student found
	 * @throws DataAccessServiceException if there is an error accessing the database.
	 */
	StudentDto getStudentByRegister(Register register)
		throws IllegalArgumentException, DataAccessServiceException;


    /**
	 * Retrieves a student by fullname.
	 * @param fullname the name of the student.
	 * @return List<StudentDto> List of StudentDto object containing the
	 * 		   student's data.
	 * @throws IllegalArgumentException if the fullname is null or blank.
	 * @throws DataAccessServiceException if there is an error accessing the database.
	 */
	List<StudentDto> getStudentsByFullname(String fullname)
		throws IllegalArgumentException, DataAccessServiceException;


    /**
	 * Adds a new student to the repository.
	 * @param form with data of the student to be added
	 * @param degreeCourse degree course of the new student
	 * @param ordering the ordering of the courses in the study plan
	 * @return Optional<StudentDto> object containing the added student's data.
	 * @throws IllegalArgumentException if the form is invalid.
	 * @throws ObjectAlreadyExistsException if a student with the same register
	 * 		   already or with same name and dob exists in the repository.
	 * @throws ObjectNotFoundException if the degree course does not exists
	 * 		   in the repository.
	 * @throws DataAccessServiceException if there is an error accessing the database
	 */
	@Transactional(rollbackOn = {IllegalArgumentException.class, ObjectAlreadyExistsException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    Optional<StudentDto> addNewStudent(RegistrationForm form, DegreeCourse degreeCourse, String ordering)
		throws IllegalArgumentException, ObjectAlreadyExistsException, ObjectNotFoundException, DataAccessServiceException;


	/**
	 * Deletes the relationship between a student and their associated entities.
	 * @param student the student whose relationships are to be deleted
	 * @return
	 */
	@Transactional
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	void deleteStudentRelationship(Student student);

}
