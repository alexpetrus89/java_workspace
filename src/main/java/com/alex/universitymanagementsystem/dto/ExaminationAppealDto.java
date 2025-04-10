package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.Register;

public class ExaminationAppealDto {

    // instance variables
    private Long id;
    private Professor professor;
    private Course course;
    private String degreeCourse;
    private String description;
    private LocalDate date;
    private Set<Register> students = new HashSet<>();

    // constructors
    public ExaminationAppealDto() {}

    public ExaminationAppealDto(Long id, Course course, String description, LocalDate date, Set<Register> students) {
        this.id = id;
        this.course = course;
        this.degreeCourse = course.getDegreeCourse().getName();
        this.professor = course.getProfessor();
        this.description = description;
        this.date = date;
        this.students = students;
    }

    // getters
    public Long getId() { return id; }
    public Professor getProfessor() { return professor; }
    public Course getCourse() { return course; }
    public String getDegreeCourse() { return degreeCourse; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public Set<Register> getStudents() { return students; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setCourse(Course course) { this.course = course; }
    public void setDegreeCourse(String degreeCourse) { this.degreeCourse = degreeCourse; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStudents(Set<Register> students) { this.students = students; }

}
