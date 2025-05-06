package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.domain.ExaminationOutcome;

public class OutcomeDto {

    // instance variables
    private final ExaminationOutcome outcome;
    private final String token;

    // constructor
    public OutcomeDto(ExaminationOutcome outcome, String token) {
        this.outcome = outcome;
        this.token = token;
    }

    // getters
    public ExaminationOutcome getOutcome() {
        return outcome;
    }

    public String getToken() {
        return token;
    }


}
