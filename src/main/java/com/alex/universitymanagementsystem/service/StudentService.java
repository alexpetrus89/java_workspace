package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

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
     * Return a student by register
     * @param register the register of the student
     * @return StudentDto object
     * @throws ObjectNotFoundException if the student does not exist
     * @throws IllegalArgumentException if the register is null
     */
    StudentDto getStudentByRegister(@NonNull Register register)
        throws ObjectNotFoundException;

    /**
     * Retrieves a student by their name.
     * @param name the name of the student.
     * @return List<StudentDto> List of StudentDto objects containing
     *         the student's data.
     * @throws ObjectNotFoundException if no student with the given name exists.
     * @throws IllegalArgumentException if the name is null.
     */
    List<StudentDto> getStudentsByName(@NonNull String name)
        throws ObjectNotFoundException;

    /**
     * Adds a new student to the repository
     * @param studentDto the student data transfer object containing
     *                   the details of the student to be added
     * @throws ObjectAlreadyExistsException if a student with the same register
     *                                      already exists in the repository
     * @throws IllegalArgumentException if the StudentDto object is null
     */
    @Transactional
    void addNewStudent(@NonNull StudentDto studentDto)
        throws ObjectAlreadyExistsException;

    /**
     * Update an existing student's information
     * @param studentDto the data transfer object containing the new
     *                   details of the student to be updated
     * @throws ObjectNotFoundException if no student with the given register exists
     *                                 in the repository or if the specified degree
     *                                 course does not exist
     * @throws IllegalArgumentException if the StudentDto object is null
     */
    @Transactional
    void updateStudent(@NonNull StudentDto studentDto)
        throws ObjectNotFoundException;

    /**
     * Deletes a student from the repository based on their register.
     * @param register the register of the student to be deleted.
     * @throw ObjectNotFoundException if no student with the given register
     *                                  exists in the repository.
     * @throws IllegalArgumentException if the register is null.
     * @throws NullPointerException if the register is null.
     */
	@Transactional
    void deleteStudent(@NonNull Register register) throws ObjectNotFoundException;

}
