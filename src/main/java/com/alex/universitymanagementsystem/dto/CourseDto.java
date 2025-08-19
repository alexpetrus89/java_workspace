package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.component.CourseSerializer;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "courseId")
@JsonSerialize(using = CourseSerializer.class)
public class CourseDto {

    // instance variables
    @NotBlank(message = "Course name is mandatory")
    private String name;

    private CourseType type;

    @Positive(message = "CFU must be a positive number")
    private Integer cfu;
    private ProfessorDto professor;

    @NotBlank(message = "Degree course name is mandatory")
    private DegreeCourseDto degreeCourse;

    // constructors
    public CourseDto() {}

    public CourseDto(
        String name,
        CourseType type,
        Integer cfu,
        ProfessorDto professor,
        DegreeCourseDto degreeCourse

    ) {
        this.name = name;
        this.type = type;
        this.cfu = cfu;
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

    public ProfessorDto getProfessor() {
        return professor;
    }

    public DegreeCourseDto getDegreeCourse() {
        return degreeCourse;
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

    public void setProfessor(ProfessorDto professor) {
        this.professor = professor;
    }

    public void setDegreeCourse(DegreeCourseDto degreeCourse) {
        this.degreeCourse = degreeCourse;
    }


}
