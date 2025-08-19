package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.annotation.SwapCoursesConstraint;
import com.alex.universitymanagementsystem.domain.immutable.Register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@SwapCoursesConstraint
public class SwapCoursesDto {

    @NotNull(message = "Register must not be null")
    private Register register;

    @NotBlank(message = "New course name is mandatory")
    private String courseToAddName;

    @NotBlank(message = "Old course name is mandatory")
    private String courseToRemoveName;

    @NotBlank(message = "New degree course name is mandatory")
    private String newDegreeCourseName;

    @NotBlank(message = "Old degree course name is mandatory")
    private String oldDegreeCourseName;

    // Getters
    public Register getRegister() {
        return register;
    }

    public String getCourseToAddName() {
        return courseToAddName;
    }

    public String getCourseToRemoveName() {
        return courseToRemoveName;
    }

    public String getNewDegreeCourseName() {
        return newDegreeCourseName;
    }

    public String getOldDegreeCourseName() {
        return oldDegreeCourseName;
    }

    // Setters
    public void setRegister(Register register) {
        this.register = register;
    }

    public void setCourseToAddName(String courseToAddName) {
        this.courseToAddName = courseToAddName;
    }

    public void setCourseToRemoveName(String courseToRemoveName) {
        this.courseToRemoveName = courseToRemoveName;
    }

    public void setNewDegreeCourseName(String newDegreeCourseName) {
        this.newDegreeCourseName = newDegreeCourseName;
    }

    public void setOldDegreeCourseName(String oldDegreeCourseName) {
        this.oldDegreeCourseName = oldDegreeCourseName;
    }

}

