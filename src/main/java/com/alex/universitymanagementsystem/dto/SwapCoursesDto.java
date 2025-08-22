package com.alex.universitymanagementsystem.dto;

import java.util.Set;

import com.alex.universitymanagementsystem.annotation.SwapCoursesConstraint;
import com.alex.universitymanagementsystem.domain.immutable.Register;

import jakarta.validation.constraints.NotBlank;

@SwapCoursesConstraint
public class SwapCoursesDto {

    private final Set<DegreeCourseDto> degreeCourses;
    private final String degreeCourse;
    private final Set<CourseDto> studyPlan;
    private final String token;

    private Register register;

    @NotBlank(message = "New course name is mandatory")
    private String courseToAdd;

    @NotBlank(message = "Old course name is mandatory")
    private String courseToRemove;

    @NotBlank(message = "New degree course name is mandatory")
    private String degreeCourseOfNewCourse;

    @NotBlank(message = "Old degree course name is mandatory")
    private String degreeCourseOfOldCourse;

    // constructor
    public SwapCoursesDto(
        Set<DegreeCourseDto> degreeCourses,
        String degreeCourse,
        Set<CourseDto> studyPlan,
        String token
    ) {
        this.degreeCourses = degreeCourses;
        this.degreeCourse = degreeCourse;
        this.studyPlan = studyPlan;
        this.token = token;
    }

    // getters
    public Set<DegreeCourseDto> getDegreeCourses() {
        return degreeCourses;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    public Set<CourseDto> getStudyPlan() {
        return studyPlan;
    }

    public String getToken() {
        return token;
    }

    public Register getRegister() {
        return register;
    }

    public String getCourseToAdd() {
        return courseToAdd;
    }

    public String getCourseToRemove() {
        return courseToRemove;
    }

    public String getDegreeCourseOfNewCourse() {
        return degreeCourseOfNewCourse;
    }

    public String getDegreeCourseOfOldCourse() {
        return degreeCourseOfOldCourse;
    }

    // setters
    public void setRegister(Register register) {
        this.register = register;
    }

    public void setCourseToAdd(String courseToAdd) {
        this.courseToAdd = courseToAdd;
    }

    public void setCourseToRemove(String courseToRemove) {
        this.courseToRemove = courseToRemove;
    }

    public void setDegreeCourseOfNewCourse(String degreeCourseOfNewCourse) {
        this.degreeCourseOfNewCourse = degreeCourseOfNewCourse;
    }

    public void setDegreeCourseOfOldCourse(String degreeCourseOfOldCourse) {
        this.degreeCourseOfOldCourse = degreeCourseOfOldCourse;
    }


}

