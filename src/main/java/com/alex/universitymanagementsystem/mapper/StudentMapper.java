package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.StudentDto;

public class StudentMapper {

    private StudentMapper() {} // private constructor to prevent instantiation

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
            student.getFullname(),
            student.getFullname(),
            student.getDob(),
            student.getDegreeCourse(),
            student.getStudyPlan()
        );
    }

}
