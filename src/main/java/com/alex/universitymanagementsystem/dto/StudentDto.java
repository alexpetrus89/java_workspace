package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;
import java.time.Period;

import com.alex.universitymanagementsystem.annotation.ValidFiscalCode;
import com.alex.universitymanagementsystem.annotation.ValidRegister;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;



public class StudentDto {

    // instance variables
    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    private String username;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Fiscal code is mandatory")
    @ValidFiscalCode
    private String fiscalCode;

    @NotBlank(message = "Register is mandatory")
    @ValidRegister
    private String register;

    private Integer age;
    private DegreeCourseDto degreeCourse;
    private StudyPlanDto studyPlan;

    // default constructor
    public StudentDto() {}

    // constructors
    public StudentDto(
        String username,
        String firstName,
        String lastName,
        LocalDate dob,
        String fiscalCode,
        String register,
        DegreeCourseDto degreeCourse
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = fiscalCode;
        this.register = register;
        this.dob = dob;
        this.age = calculateAge();
        this.degreeCourse = degreeCourse;
    }

    public StudentDto(
        String username,
        String firstName,
        String lastName,
        LocalDate dob,
        String fiscalCode,
        String register,
        DegreeCourseDto degreeCourse,
        StudyPlanDto studyPlan
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.fiscalCode = fiscalCode;
        this.register = register;
        this.age = calculateAge();
        this.degreeCourse = degreeCourse;
        this.studyPlan = studyPlan;
    }

    // getters
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getRegister() {
        return register;
    }

    public Integer getAge() {
        return age;
    }

    public DegreeCourseDto getDegreeCourse() {
        return degreeCourse;
    }

    public StudyPlanDto getStudyPlan() {
        return studyPlan;
    }

    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public void setDegreeCourse(DegreeCourseDto degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public void setStudyPlan(StudyPlanDto studyPlan) {
        this.studyPlan = studyPlan;
    }


    // private method
    private int calculateAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

}
