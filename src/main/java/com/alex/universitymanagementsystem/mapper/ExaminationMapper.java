package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.ExaminationDto;

public class ExaminationMapper {

    private ExaminationMapper() {} // private constructor to prevent instantiation

    public static Examination toEntity(ExaminationDto dto, Student student, Course course) {
        if (dto == null || student == null || course == null) return null;
        return new Examination(
            course,
            student,
            dto.getGrade(),
            dto.isWithHonors(),
            dto.getDate()
        );
    }

    public static ExaminationDto toDto(Examination examination) {
        if (examination == null) return null;
        ExaminationDto dto = new ExaminationDto();
        dto.setCourseName(examination.getCourse().getName());
        dto.setRegister(examination.getRegister());
        dto.setDegreeCourseName(examination.getCourse().getDegreeCourse().getName());
        dto.setCourseCfu(examination.getCourse().getCfu());
        dto.setGrade(examination.getGrade());
        dto.setWithHonors(examination.isWithHonors());
        dto.setDate(examination.getDate());
        return dto;
    }

}
