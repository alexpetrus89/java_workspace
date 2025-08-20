package com.alex.universitymanagementsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ExaminationOutcomeDto {

    // instance variables
    private Long id;

    @NotBlank(message = "Register cannot be blank")
    @Pattern(regexp = "\\d{6}", message = "Register must be a 6-digit number")
    private String register;

    @NotNull
    private ExaminationAppealDto appeal;

    @Min(0)
    @Max(30)
    private int grade;

    private boolean present;
    private boolean withHonors;
    private boolean accepted;

    @Size(max = 255)
    private String message;

    // constructors
    public ExaminationOutcomeDto() {}

    public ExaminationOutcomeDto(ExaminationAppealDto appeal, String register) {
        this.register = register;
        this.appeal = appeal;
        this.grade = 0; // Default grade
        this.present = false; // Default present status
        this.withHonors = false; // Default with honors status
        this.accepted = false; // Default accepted status
        this.message = ""; // Default message
    }

    public ExaminationOutcomeDto(Long id, ExaminationAppealDto appeal, String register) {
        this.id = id;
        this.register = register;
        this.appeal = appeal;
        this.grade = 0; // Default grade
        this.present = false; // Default present status
        this.withHonors = false; // Default with honors status
        this.accepted = false; // Default accepted status
        this.message = ""; // Default message
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getRegister() {
        return register;
    }

    public ExaminationAppealDto getAppeal() {
        return appeal;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isWithHonors() {
        return withHonors;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public void setAppeal(ExaminationAppealDto appeal) {
        this.appeal = appeal;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void setWithHonors(boolean withHonors) {
        this.withHonors = withHonors;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

