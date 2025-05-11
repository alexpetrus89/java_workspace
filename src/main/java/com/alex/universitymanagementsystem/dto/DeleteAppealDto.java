package com.alex.universitymanagementsystem.dto;

import java.util.List;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;

public class DeleteAppealDto {

    // instance variable
    private final List<ExaminationAppeal> appeals;
    private final String token;

    // constructor
    public DeleteAppealDto(List<ExaminationAppeal> appeals, String token) {
        this.appeals = appeals;
        this.token = token;
    }

    // getters
    public List<ExaminationAppeal> getAppeals() {
        return appeals;
    }

    public String getToken() {
        return token;
    }



}
