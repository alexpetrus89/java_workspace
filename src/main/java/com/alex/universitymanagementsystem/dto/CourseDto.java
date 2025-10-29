package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.utils.CourseDtoCarrier;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public class CourseDto implements CourseDtoCarrier {

    // instance variables
    @NotBlank(message = "Course name is mandatory")
    private String name;

    @NotNull
    private CourseType type;

    @Positive(message = "CFU must be a positive number")
    private Integer cfu;

    @NotNull
    private Integer yearOfStudy;

    private ProfessorDto professor;

    private DegreeCourseDto degreeCourse;

    // constructors
    public CourseDto() {}

    public CourseDto(String name, CourseType type, Integer cfu, Integer yearOfStudy, ProfessorDto professor, DegreeCourseDto degreeCourse) {
        this.name = name;
        this.type = type;
        this.cfu = cfu;
        this.yearOfStudy = yearOfStudy;
        this.professor = professor;
        this.degreeCourse = degreeCourse;
    }



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

    public ProfessorDto getProfessor() {
        return professor;
    }

    public DegreeCourseDto getDegreeCourse() {
        return degreeCourse;
    }

    @Override
    public String getDegreeCourseName() {
        return degreeCourse != null ? degreeCourse.getName() : null;
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

    public void setProfessor(ProfessorDto professor) {
        this.professor = professor;
    }

    public void setDegreeCourse(DegreeCourseDto degreeCourse) {
        this.degreeCourse = degreeCourse;
    }


}
