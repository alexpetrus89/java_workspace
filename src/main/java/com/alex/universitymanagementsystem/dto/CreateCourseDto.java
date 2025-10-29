package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.annotation.ValidProfessorCode;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.utils.CourseDtoCarrier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateCourseDto implements CourseDtoCarrier {

    @NotBlank(message = "Course name is mandatory")
    private String name;

    @NotNull(message = "Course type is mandatory")
    private CourseType type;

    @Positive(message = "CFU must be positive")
    private Integer cfu;

    @NotNull(message = "Year of study is mandatory")
    private Integer yearOfStudy;

    @NotBlank(message = "Professor code is mandatory")
    @ValidProfessorCode
    private String professorCode;

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

    @Override
    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public String getProfessorCode() {
        return professorCode;
    }

    @Override
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

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setProfessorCode(String professorCode) {
        this.professorCode = professorCode;
    }

    public void setDegreeCourseName(String degreeCourseName) {
        this.degreeCourseName = degreeCourseName;
    }
}
