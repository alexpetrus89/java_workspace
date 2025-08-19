package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ExaminationDto{

    // instance variables
    @NotNull(message = "Register must not be null")
    @Size(min = 6, max = 6, message = "Register must be exactly 6 digits")
    @Pattern(regexp = "\\d{6}", message = "Register must contain only digits")
    private String register;

    @NotBlank(message = "Course name must not be blank")
    private String courseName;

    @NotBlank(message = "Degree course name must not be blank")
    private String degreeCourseName;

    @NotBlank(message = "Grade must not be blank")
    @Pattern(regexp = "\\d{1,2}", message = "Grade must be a number between 0 and 30")
    private String grade;

    private boolean withHonors;

    @NotNull(message = "Date must not be null")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    // constructors
    public ExaminationDto(String register, String courseName, String degreeCourseName, String grade, boolean withHonors, LocalDate date) {
        this.register = register.toLowerCase();
        this.courseName = courseName.toLowerCase();
        this.degreeCourseName = degreeCourseName.toUpperCase();
        this.grade = grade;
        this.withHonors = withHonors;
        this.date = date;
    }

    // getters
    public String getRegister() {
        return register;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDegreeCourseName() {
        return degreeCourseName;
    }

    public String getGrade() {
        return grade;
    }

    public LocalDate getDate() {
        return date;
    }

    // setters
    public void setRegister(String register) {
        this.register = register;
    }


    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDegreeCourseName(String degreeCourseName) {
        this.degreeCourseName = degreeCourseName;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isWithHonors() {
        return withHonors;
    }

    public void setWithHonors(boolean withHonors) {
        this.withHonors = withHonors;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

