package com.alex.studentmanagementsystem.dto;

import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.domain.Professor;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;

public class CourseDto {

    // instance variables
    private CourseId courseId;
    private String name;
    private String category;
    private Integer cfu;
    private Professor professor;
    private DegreeCourse degreeCourse;

    // constructors
    public CourseDto() {}

    public CourseDto(
        CourseId courseId,
        String name,
        String category,
        Integer cfu,
        Professor professor,
        DegreeCourse degreeCourse

    ) {
        this.courseId = courseId;
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
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

    public void setCategory(String category) {
        this.category = category;
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
