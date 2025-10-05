package com.alex.universitymanagementsystem.dto;


import com.alex.universitymanagementsystem.annotation.ValidRegister;

import jakarta.validation.constraints.NotBlank;

public class ChangeDegreeCourseForm {

    @NotBlank(message = "Register is required")
    @ValidRegister
    private String register;

    @NotBlank(message = "Degree course must be selected")
    private String degreeCourse;

    public String getRegister() { return register; }
    public void setRegister(String register) { this.register = register; }

    public String getDegreeCourse() { return degreeCourse; }
    public void setDegreeCourse(String degreeCourse) { this.degreeCourse = degreeCourse; }
}

