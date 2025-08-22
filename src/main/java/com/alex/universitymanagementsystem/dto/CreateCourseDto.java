package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.annotation.ValidUniqueCode;
import com.alex.universitymanagementsystem.enum_type.CourseType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateCourseDto {

    @NotBlank(message = "Course name is mandatory")
    private String name;

    @NotNull(message = "Course type is mandatory")
    private CourseType type;

    @Positive(message = "CFU must be positive")
    private Integer cfu;

    @NotBlank(message = "Professor unique code is mandatory")
    @ValidUniqueCode
    private String uniqueCode;

    @NotBlank(message = "Degree course is mandatory")
    private String degreeCourseName;

    // getters
    public String getName() {
        return name;
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

    public String getDegreeCourseName() {
        return degreeCourseName;
    }

    // setters
    public void setName(String name) {
        this.name = name;
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

    public void setDegreeCourseName(String degreeCourseName) {
        this.degreeCourseName = degreeCourseName;
    }
}
