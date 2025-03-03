package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.dto.ExaminationDto;

public class ExaminationMapper {

    private ExaminationMapper() {} // private constructor to prevent instantiation

    public static Examination mapToExamination(ExaminationDto examinationDto) {
        return new Examination(
            examinationDto.getCourse(),
            examinationDto.getStudent(),
            examinationDto.getGrade(),
            examinationDto.isWithHonors(),
            examinationDto.getDate()
        );
    }

    public static ExaminationDto mapToExaminationDto(Examination examination) {
        return new ExaminationDto(
            examination.getCourse(),
            examination.getStudent(),
            examination.getGrade(),
            examination.isWithHonors(),
            examination.getDate()
        );
    }

}
