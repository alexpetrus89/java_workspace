package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.ExaminationDto;

public class ExaminationMapper {

    private ExaminationMapper() {} // private constructor to prevent instantiation

    public static Examination toEntity(ExaminationDto examinationDto, Student student, Course course) {
        return new Examination(
            course,
            student,
            Integer.parseInt(examinationDto.getGrade()),
            examinationDto.isWithHonors(),
            examinationDto.getDate()
        );
    }

    public static ExaminationDto toDto(Examination examination) {
        return new ExaminationDto(
            examination.getStudent().getRegister().toString(),
            examination.getCourse().getName(),
            examination.getCourse().getDegreeCourse().getName(),
            String.valueOf(examination.getGrade()),
            examination.isWithHonors(),
            examination.getDate()
        );
    }

}
