package com.alex.studentmanagementsystem.mapper;

import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.dto.StudentDto;

public class StudentMapper {

    private StudentMapper() {} // private constructor to prevent instantiation

    public static Student mapToStudent(StudentDto studentDto) {
        return new Student(
            studentDto.getRegister(),
            studentDto.getName(),
            studentDto.getEmail(),
            studentDto.getDob(),
            studentDto.getDegreeCourse()
        );
    }

    public static StudentDto mapToStudentDto(Student student) {
        return new StudentDto(
            student.getRegister(),
            student.getName(),
            student.getEmail(),
            student.getDob(),
            student.getDegreeCourse()
        );
    }

}
