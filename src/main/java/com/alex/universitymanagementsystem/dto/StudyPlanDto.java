package com.alex.universitymanagementsystem.dto;

import java.util.HashSet;
import java.util.Set;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Student;



public class StudyPlanDto {

    // instance variables
    private Student student;
    private String ordering;
    private Set<Course> courses = new HashSet<>();

    // constructors
    public StudyPlanDto() {}

    public StudyPlanDto(
        String ordering,
        Set<Course> courses
    ) {
        this.ordering = ordering;
        this.courses = courses;
    }

    public StudyPlanDto(
        Student student,
        String ordering,
        Set<Course> courses
    ) {
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

    public Set<Course> getCourses() {
        return courses;
    }

    // setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

}
