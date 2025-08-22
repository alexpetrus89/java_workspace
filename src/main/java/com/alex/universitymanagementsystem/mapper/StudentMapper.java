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
        Student student = new Student();
        student.setUsername(dto.getUsername());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setDob(dto.getDob());
        student.setFiscalCode(new FiscalCode(dto.getFiscalCode()));
        student.setRegister(new Register(dto.getRegister()));
        student.setDegreeCourse(DegreeCourseMapper.toEntity(dto.getDegreeCourse()));
        student.setStudyPlan(StudyPlanMapper.toEntity(dto.getStudyPlan()));
        return student;
    }

    public static StudentDto toDto(Student student) {
        if(student == null) return null;
        StudentDto dto = new StudentDto();
        dto.setUsername(student.getUsername());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setDob(student.getDob());
        dto.setFiscalCode(student.getFiscalCode().toString());
        dto.setRegister(student.getRegister().toString());
        dto.setDegreeCourse(DegreeCourseMapper.toDto(student.getDegreeCourse()));
        dto.setStudyPlan(StudyPlanMapper.toDto(student.getStudyPlan()));
        return dto;
    }

}
