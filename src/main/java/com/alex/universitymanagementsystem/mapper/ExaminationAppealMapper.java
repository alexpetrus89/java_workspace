package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;

public class ExaminationAppealMapper {

    // private constructor to prevent instantiation
    private ExaminationAppealMapper() {}

    public static ExaminationAppeal mapToExaminationAppeal(ExaminationAppealDto examinationAppealDto) {
        return new ExaminationAppeal(
            examinationAppealDto.getCourse(),
            examinationAppealDto.getDescription(),
            examinationAppealDto.getDate(),
            examinationAppealDto.getStudents()
        );
    }

    public static ExaminationAppealDto mapToExaminationAppealDto(ExaminationAppeal examinationAppeal) {
        return new ExaminationAppealDto(
            examinationAppeal.getId(),
            examinationAppeal.getCourse(),
            examinationAppeal.getDescription(),
            examinationAppeal.getDate(),
            examinationAppeal.getStudents()
        );
    }

}
