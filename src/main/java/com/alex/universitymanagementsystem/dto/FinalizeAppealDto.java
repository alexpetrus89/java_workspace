package com.alex.universitymanagementsystem.dto;

import java.util.List;

public class FinalizeAppealDto {

    // instance variables
    private Long appealId;
    private List<StudentDto> students;

    // constructor
    public FinalizeAppealDto(Long appealId, List<StudentDto> students) {
        this.appealId = appealId;
        this.students = students;
    }

    // getters
    public Long getAppealId() {
        return appealId;
    }

    public List<StudentDto> getStudents() {
        return students;
    }

    // setters
    public void setAppealId(Long appealId) {
        this.appealId = appealId;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }

}
