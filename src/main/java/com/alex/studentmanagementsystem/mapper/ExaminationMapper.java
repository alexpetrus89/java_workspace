package com.alex.studentmanagementsystem.mapper;

import com.alex.studentmanagementsystem.domain.Examination;
import com.alex.studentmanagementsystem.dto.ExaminationDto;

public class ExaminationMapper {

    private ExaminationMapper() {} // private constructor to prevent instantiation

    public static Examination mapToExamination(ExaminationDto examinationDto) {
        return new Examination(
            examinationDto.getCourse(),
            examinationDto.getStudent(),
            examinationDto.getGrade(),
            examinationDto.isWithHonors(),
            examinationDto.getExaminationDob()
        );
    }

    public static ExaminationDto mapToExaminationDto(Examination examination) {
        return new ExaminationDto(
            examination.getCourse(),
            examination.getStudent(),
            examination.getGrade(),
            examination.isWithHonors(),
            examination.getExaminationDob()
        );
    }

}
