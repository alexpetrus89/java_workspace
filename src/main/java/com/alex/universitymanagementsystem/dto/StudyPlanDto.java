package com.alex.universitymanagementsystem.dto;

import java.util.HashSet;
import java.util.Set;

import com.alex.universitymanagementsystem.domain.Student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



public class StudyPlanDto {

    // instance variables
    @NotNull(message = "Student must not be null")
    private Student student;

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

    public StudyPlanDto(Student student, String ordering, Set<CourseDto> courses) {
        this.student = student;
        this.ordering = ordering;
        this.courses = courses;
    }

    // getters
    public Student getStudent() {
        return student;
    }

    public String getOrdering() {
        return ordering;
    }

    public Set<CourseDto> getCourses() {
        return courses;
    }

    // setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public void setCourses(Set<CourseDto> courses) {
        this.courses = courses;
    }

}
