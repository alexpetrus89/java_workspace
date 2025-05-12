package com.alex.universitymanagementsystem.dto;

import java.util.Set;

public class ModifyStudyPlanDto {

    // instance variables
    private final Set<DegreeCourseDto> degreeCourses;
    private final String degreeCourse;
    private final Set<CourseDto> studyPlan;
    private final String token;

    // constructor
    public ModifyStudyPlanDto(
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

}
