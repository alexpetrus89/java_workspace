package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;

import jakarta.transaction.Transactional;


public interface StudentService {

    List<StudentDto> getStudents();

    StudentDto getStudentByRegister(Register register)
        throws ObjectNotFoundException;

    StudentDto getStudentByName(String name)
        throws ObjectNotFoundException;

    @Transactional
    void addNewStudent(StudentDto studentDto)
        throws ObjectAlreadyExistsException;

    @Transactional
    public void updateStudent(StudentDto studentDto)
        throws ObjectNotFoundException;

	@Transactional
    void deleteStudent(@NonNull Register studentRegister)
        throws ObjectNotFoundException;

}
