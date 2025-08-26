package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;

import com.alex.universitymanagementsystem.annotation.ValidRegister;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class ExaminationDto{

    // instance variables
    @NotBlank(message = "Register is mandatory")
    @ValidRegister
    private String register;

    @NotBlank(message = "Course name must not be blank")
    private String courseName;

    @NotBlank(message = "Degree course name must not be blank")
    private String degreeCourseName;

    @Positive(message = "CFU must be a positive number")
    private Integer courseCfu;

    @Min(value = 0, message = "Grade must be at least 0")
    @Max(value = 30, message = "Grade must be at most 30")
    @NotNull(message = "Grade must not be null")
    private Integer grade;

    private boolean withHonors;

    @NotNull(message = "Date must not be null")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    // constructors
    public ExaminationDto() {}

    public ExaminationDto(String register, String courseName, String degreeCourseName, Integer courseCfu, Integer grade, boolean withHonors, LocalDate date) {
        this.register = register.toLowerCase();
        this.courseName = courseName.toLowerCase();
        this.degreeCourseName = degreeCourseName.toUpperCase();
        this.courseCfu = courseCfu;
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

    public Integer getCourseCfu() {
        return courseCfu;
    }

    public Integer getGrade() {
        return grade;
    }

    public LocalDate getDate() {
        return date;
    }

    // setters
    public void setRegister(String register) {
        this.register = register.toLowerCase();
    }


    public void setCourseName(String courseName) {
        this.courseName = courseName.toLowerCase();
    }

    public void setDegreeCourseName(String degreeCourseName) {
        this.degreeCourseName = degreeCourseName.toUpperCase();
    }

    public void setCourseCfu(Integer courseCfu) {
        this.courseCfu = courseCfu;
    }

    public void setGrade(Integer grade) {
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

