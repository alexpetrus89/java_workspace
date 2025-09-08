package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.entity.ExaminationAppeal;
import com.alex.universitymanagementsystem.entity.ExaminationOutcome;

public class ExaminationOutcomeMapper {

    private ExaminationOutcomeMapper() {}

    public static ExaminationOutcome toEntity(ExaminationOutcomeDto dto, ExaminationAppeal appeal) {
        if(dto == null) return null;
        ExaminationOutcome outcome = new ExaminationOutcome();
        outcome.setId(dto.getId());
        outcome.setAppeal(appeal);
        outcome.setRegister(dto.getRegister());
        outcome.setGrade(dto.getGrade());
        outcome.setPresent(dto.isPresent());
        outcome.setWithHonors(dto.isWithHonors());
        outcome.setAccepted(dto.isAccepted());
        outcome.setMessage(dto.getMessage());
        return outcome;
    }

    public static ExaminationOutcomeDto toDto(ExaminationOutcome outcome, ExaminationAppealDto appeal) {
        if(outcome == null) return null;
        ExaminationOutcomeDto dto = new ExaminationOutcomeDto();
        dto.setId(outcome.getId());
        dto.setRegister(outcome.getRegister());
        dto.setAppeal(appeal);
        dto.setGrade(outcome.getGrade());
        dto.setPresent(outcome.isPresent());
        dto.setWithHonors(outcome.isWithHonors());
        dto.setAccepted(outcome.isAccepted());
        dto.setMessage(outcome.getMessage());
        return dto;
    }


}

