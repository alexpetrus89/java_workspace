package com.alex.studentmanagementsystem.dto;

import java.time.LocalDate;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.ExaminationId;




public class ExaminationDto {

    // instance variables
    private ExaminationId id;
    private Course course;
    private Student student;
    private int grade;
    private boolean withHonors;
    private LocalDate date;

    // default constructor
    public ExaminationDto() {}

    public ExaminationDto(
        Course course,
        Student student,
        int grade,
        boolean withHonors,
        LocalDate date
    ) {
        this.course = course;
        this.student = student;
        this.grade = grade;
        this.withHonors = withHonors;
        this.date = date;
    }

    // getters
    public ExaminationId getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isWithHonors() {
        return withHonors;
    }

    public LocalDate getDate() {
        return date;
    }


}
