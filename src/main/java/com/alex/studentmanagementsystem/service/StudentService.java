package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;


public interface StudentService {

    /**
     * Return all students
     * @return List<StudentDto>
     */
    List<StudentDto> getStudents();

    /**
     * Return a student by register
     * @param Register register
     * @return StudentDto object
     * @throws ObjectNotFoundException if the student does not exist
     */
    StudentDto getStudentByRegister(@NonNull Register register)
        throws ObjectNotFoundException;

    /**
     * Retrieves a student by their name.
     * @param name the name of the student.
     * @return StudentDto object containing the student's data.
     * @throws ObjectNotFoundException if no student with the given name exists.
     */
    StudentDto getStudentByName(String name)
        throws ObjectNotFoundException;

    /**
     * Adds a new student to the repository
     * @param StudentDto studentDto the student data transfer object containing
     *                   the details of the student to be added
     * @throws ObjectAlreadyExistsException if a student with the same register
     *                                      already exists in the repository
     * @throws ObjectNotFoundException if the degree course does not exist
     * @throws IllegalArgumentException if the given register is null or empty
     */
    @Transactional
    void addNewStudent(StudentDto studentDto)
        throws ObjectAlreadyExistsException;

    /**
     * Update an existing student's information
     * @param StudentDto studentDto the data transfer object containing the new
     *                   details of the student to be updated
     * @throws ObjectNotFoundException if no student with the given register exists
     *                                 in the repository or if the specified degree
     *                                 course does not exist
     * @throws NullPointerException if the newStudentDto is null
     */
    @Transactional
    void updateStudent(StudentDto studentDto)
        throws ObjectNotFoundException;

    /**
     * Deletes a student from the repository based on their register.
     *
     * @param Register register the register of the student to be deleted.
     * @throws ObjectNotFoundException if no student with the given register
     *                                  exists in the repository.
     */
	@Transactional
    void deleteStudent(@NonNull Register register)
        throws ObjectNotFoundException;

}
