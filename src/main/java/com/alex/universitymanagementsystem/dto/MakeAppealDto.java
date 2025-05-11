package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;
import java.util.List;

public class MakeAppealDto {

    // instance variables
    private Long appealId;
    private LocalDate date;
    private List<StudentDto> students;

    // constructor
    public MakeAppealDto(Long appealId, LocalDate date, List<StudentDto> students) {
        this.appealId = appealId;
        this.date = date;
        this.students = students;
    }

    // getters
    public Long getAppealId() {
        return appealId;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<StudentDto> getStudents() {
        return students;
    }

    // setters
    public void setAppealId(Long appealId) {
        this.appealId = appealId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }

}
