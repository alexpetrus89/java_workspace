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
        return new ExaminationDto(
            examination.getStudent().getRegister().toString(),
            examination.getCourse().getName(),
            examination.getCourse().getDegreeCourse().getName(),
            examination.getCourse().getCfu(),
            examination.getGrade(),
            examination.isWithHonors(),
            examination.getDate()
        );
    }

}
