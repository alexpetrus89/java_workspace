package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.StudentDto;

// This is a utility class that provides static methods for mapping between Student and StudentDto objects.
public class StudentMapper {

    // private constructor to prevent instantiation
    private StudentMapper() {}

    public static Student mapToStudent(StudentDto studentDto) {
        return new Student(
            studentDto.getRegister(),
            studentDto.getUsername(),
            studentDto.getFullname(),
            studentDto.getDob(),
            studentDto.getDegreeCourse(),
            studentDto.getStudyPlan()
        );
    }

    public static StudentDto mapToStudentDto(Student student) {
        return new StudentDto(
            student.getRegister(),
            student.getUsername(),
            student.getFullname(),
            student.getDob(),
            student.getDegreeCourse(),
            student.getStudyPlan()
        );
    }

}
