package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.annotation.ValidProfessorCode;
import com.alex.universitymanagementsystem.enum_type.CourseType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateCourseDto {


    @NotBlank(message = "Course name is mandatory")
    private String newName;

    @NotBlank(message = "Degree course is mandatory")
    private String newDegreeCourseName;

    @NotBlank(message = "Old course name is mandatory")
    private String oldName;

    @NotBlank(message = "Old degree course name is mandatory")
    private String oldDegreeCourseName;

    @NotNull(message = "Course type is mandatory")
    private CourseType type;

    @Positive(message = "CFU must be positive")
    private Integer cfu;

    @NotBlank(message = "Professor code is mandatory")
    @ValidProfessorCode
    private String professorCode;


    // getters
    public String getNewName() {
        return newName;
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

    public CourseType getType() {
        return type;
    }

    public Integer getCfu() {
        return cfu;
    }

    public String getProfessorCode() {
        return professorCode;
    }

    // setters
    public void setNewName(String newName) {
        this.newName = newName;
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

      public void setType(CourseType type) {
        this.type = type;
    }

    public void setCfu(Integer cfu) {
        this.cfu = cfu;
    }

    public void setProfessorCode(String professorCode) {
        this.professorCode = professorCode;
    }

}
