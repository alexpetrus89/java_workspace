package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;

import com.alex.universitymanagementsystem.annotation.ValidRegister;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public class UpdateExaminationDto {

    // instance variables
    @NotBlank(message = "Old register must not be blank")
    @ValidRegister
    private String oldRegister;

    @NotBlank(message = "Old course name must not be blank")
    private String oldCourseName;

    @NotBlank(message = "Old degree course name must not be blank")
    private String oldDegreeCourseName;

    @NotBlank(message = "New register must not be blank")
    @ValidRegister
    private String newRegister;

    @NotBlank(message = "New course name must not be blank")
    private String newCourseName;

    @NotBlank(message = "New degree course name must not be blank")
    private String newDegreeCourseName;

    @Pattern(regexp = "^(\\d|[1-2]\\d|30)$", message = "Grade must be a number between 0 and 30")
    private String grade;

    private boolean withHonors;

    @NotNull(message = "Date must not be null")
    @PastOrPresent(message = "Date must be in the past or today")
    private LocalDate date;

    // getters
    public String getOldRegister() {
        return oldRegister;
    }

    public String getOldCourseName() {
        return oldCourseName;
    }

    public String getOldDegreeCourseName() {
        return oldDegreeCourseName;
    }

    public String getNewRegister() {
        return newRegister;
    }

    public String getNewCourseName() {
        return newCourseName;
    }

    public String getNewDegreeCourseName() {
        return newDegreeCourseName;
    }

    public String getGrade() {
        return grade;
    }

    public boolean isWithHonors() {
        return withHonors;
    }

    public LocalDate getDate() {
        return date;
    }

    // setters
    public void setOldRegister(String oldRegister) {
        this.oldRegister = oldRegister.toLowerCase();
    }

    public void setOldCourseName(String oldCourseName) {
        this.oldCourseName = oldCourseName.toLowerCase();
    }

    public void setOldDegreeCourseName(String oldDegreeCourseName) {
        this.oldDegreeCourseName = oldDegreeCourseName.toUpperCase();
    }

    public void setNewRegister(String newRegister) {
        this.newRegister = newRegister.toLowerCase();
    }

    public void setNewCourseName(String newCourseName) {
        this.newCourseName = newCourseName.toLowerCase();
    }

    public void setNewDegreeCourseName(String newDegreeCourseName) {
        this.newDegreeCourseName = newDegreeCourseName.toUpperCase();
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setWithHonors(boolean withHonors) {
        this.withHonors = withHonors;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
