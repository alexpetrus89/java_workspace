package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.component.CourseSerializer;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "courseId")
@JsonSerialize(using = CourseSerializer.class)
public class CourseDto {

    // instance variables
    private CourseId courseId;
    private String name;
    private CourseType type;
    private Integer cfu;
    private Professor professor;
    private DegreeCourse degreeCourse;

    // constructors
    public CourseDto() {}

    public CourseDto(
        CourseId courseId,
        String name,
        CourseType type,
        Integer cfu,
        Professor professor,
        DegreeCourse degreeCourse

    ) {
        this.courseId = courseId;
        this.name = name;
        this.type = type;
        this.cfu = cfu;
        this.professor = professor;
        this.degreeCourse = degreeCourse;
    }



    // getters
    public CourseId getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public CourseType getType() {
        return type;
    }

    public Integer getCfu() {
        return cfu;
    }

    public Professor getProfessor() {
        return professor;
    }

    public DegreeCourse getDegreeCourse() {
        return degreeCourse;
    }



    // setters
    public void setCourseId(CourseId courseId) {
        this.courseId = courseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public void setCfu(Integer cfu) {
        this.cfu = cfu;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setDegreeCourse(DegreeCourse degreeCourse) {
        this.degreeCourse = degreeCourse;
    }


}
