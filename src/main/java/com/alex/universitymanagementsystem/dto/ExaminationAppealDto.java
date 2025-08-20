package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExaminationAppealDto {

    // instance variables
    private Long id;

    @NotNull(message = "Course cannot be null")
    private String course;

    @NotBlank(message = "Degree course cannot be blank")
    private String degreeCourse;

    @NotBlank(message = "Professor unique code cannot be blank")
    private String professorCode;

    private String professorFullName;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Date cannot be null")
    @FutureOrPresent(message = "Date must be in the present or future")
    private LocalDate date;

    private Set<StudentDto> students;

    // Getters
    public Long getId() { return id; }

    public String getCourse() { return course; }

    public String getDegreeCourse() { return degreeCourse; }

    public String getProfessorCode() { return professorCode; }

    public String getProfessorFullName() { return professorFullName; }

    public String getDescription() { return description; }

    public LocalDate getDate() { return date; }

    public Set<StudentDto> getStudents() { return students; }

    // Setters
    public void setId(Long id) { this.id = id; }

    public void setCourse(String course) { this.course = course; }

    public void setDegreeCourse(String degreeCourse) { this.degreeCourse = degreeCourse; }

    public void setProfessorCode(String professorCode) { this.professorCode = professorCode; }

    public void setProfessorFullName(String professorFullName) { this.professorFullName = professorFullName; }

    public void setDescription(String description) { this.description = description; }

    public void setDate(LocalDate date) { this.date = date; }

    public void setStudents(Set<StudentDto> students) { this.students = students; }
}

