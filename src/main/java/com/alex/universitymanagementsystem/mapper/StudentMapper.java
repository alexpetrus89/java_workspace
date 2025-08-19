package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.StudentDto;

// This is a utility class that provides static methods for mapping between Student and StudentDto objects.
public class StudentMapper {

    // private constructor to prevent instantiation
    private StudentMapper() {}

    public static Student toEntity(StudentDto dto) {
        if (dto == null) return null;
        return new Student(
            dto.getUsername(),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getDob(),
            new FiscalCode(dto.getFiscalCode()),
            new Register(dto.getRegister()),
            DegreeCourseMapper.toEntity(dto.getDegreeCourse()),
            StudyPlanMapper.toEntity(dto.getStudyPlan())
        );
    }

    public static StudentDto toDto(Student student) {
        if(student == null) return null;
        return new StudentDto(
            student.getUsername(),
            student.getFirstName(),
            student.getLastName(),
            student.getDob(),
            student.getFiscalCode().toString(),
            student.getRegister().toString(),
            DegreeCourseMapper.toDto(student.getDegreeCourse()),
            StudyPlanMapper.toDto(student.getStudyPlan())
        );
    }

}
