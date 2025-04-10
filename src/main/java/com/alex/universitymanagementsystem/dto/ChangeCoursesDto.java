package com.alex.universitymanagementsystem.dto;

import java.util.Set;

public class ChangeCoursesDto {

    // instance variables
    private final Set<DegreeCourseDto> allDegreeCourses;
    private final Set<CourseDto> coursesOfStudyPlan;
    private final Set<CourseDto> allCourses;
    private final String degreeCourse;
    private final String authorization;

    // constructor
    public ChangeCoursesDto(
        Set<DegreeCourseDto> allDegreeCourses,
        Set<CourseDto> coursesOfStudyPlan,
        Set<CourseDto> allCourses,
        String degreeCourse,
        String authorization
    ) {
        this.allDegreeCourses = allDegreeCourses;
        this.coursesOfStudyPlan = coursesOfStudyPlan;
        this.allCourses = allCourses;
        this.degreeCourse = degreeCourse;
        this.authorization = authorization;
    }

    // getters
    public Set<DegreeCourseDto> getAllDegreeCourses() {
        return allDegreeCourses;
    }
    public Set<CourseDto> getCoursesOfStudyPlan() {
        return coursesOfStudyPlan;
    }

    public Set<CourseDto> getAllCourses() {
        return allCourses;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    public String getAuthorization() {
        return authorization;
    }
}
