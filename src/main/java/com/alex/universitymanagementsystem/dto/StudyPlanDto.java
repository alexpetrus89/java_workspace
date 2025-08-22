package com.alex.universitymanagementsystem.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



public class StudyPlanDto {

    // instance variables
    @NotBlank(message = "Ordering must not be blank")
    private String ordering;

    @NotNull(message = "Courses must not be null")
    @Size(min = 1, message = "At least one course must be selected")
    private Set<CourseDto> courses = new HashSet<>();

    // constructors
    public StudyPlanDto() {}

    public StudyPlanDto(String ordering, Set<CourseDto> courses) {
        this.ordering = ordering;
        this.courses = courses;
    }

    // getters
    public String getOrdering() {
        return ordering;
    }

    public Set<CourseDto> getCourses() {
        return courses;
    }

    // setters
    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public void setCourses(Set<CourseDto> courses) {
        this.courses = courses;
    }

}
