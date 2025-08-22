package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.annotation.ValidUniqueCode;
import com.alex.universitymanagementsystem.enum_type.CourseType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateCourseDto {


    @NotBlank(message = "Course name is mandatory")
    private String newName;

    @NotNull(message = "Course type is mandatory")
    private CourseType type;

    @Positive(message = "CFU must be positive")
    private Integer cfu;

    @NotBlank(message = "Professor unique code is mandatory")
    @ValidUniqueCode
    private String uniqueCode;

    @NotBlank(message = "Degree course is mandatory")
    private String newDegreeCourseName;

    @NotBlank(message = "Old course name is mandatory")
    private String oldName;

    @NotBlank(message = "Old degree course name is mandatory")
    private String oldDegreeCourseName;

    // getters

    public String getNewName() {
        return newName;
    }

    public CourseType getType() {
        return type;
    }

    public Integer getCfu() {
        return cfu;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public String getNewDegreeCourseName() {
        return newDegreeCourseName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getOldDegreeCourseName() {
        return oldDegreeCourseName;
    }

    // setters

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public void setCfu(Integer cfu) {
        this.cfu = cfu;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setNewDegreeCourseName(String newDegreeCourseName) {
        this.newDegreeCourseName = newDegreeCourseName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public void setOldDegreeCourseName(String oldDegreeCourseName) {
        this.oldDegreeCourseName = oldDegreeCourseName;
    }
}
