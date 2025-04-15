package com.alex.universitymanagementsystem.dto;

import java.util.Set;

public class ChangeCoursesDto {

    // instance variables
    private final Set<DegreeCourseDto> allDegreeCourses;
    private final String degreeCourse;
    private final Set<CourseDto> studyPlan;
    private final String token;

    // constructor
    public ChangeCoursesDto(
        Set<DegreeCourseDto> allDegreeCourses,
        String degreeCourse,
        Set<CourseDto> studyPlan,
        String token
    ) {
        this.allDegreeCourses = allDegreeCourses;
        this.degreeCourse = degreeCourse;
        this.studyPlan = studyPlan;
        this.token = token;
    }

    // getters
    public Set<DegreeCourseDto> getAllDegreeCourses() {
        return allDegreeCourses;
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
