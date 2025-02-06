package com.alex.studentmanagementsystem.dto;

import java.time.LocalDate;

import com.alex.studentmanagementsystem.domain.immutable.ExaminationId;
import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.Student;




public class ExaminationDto {

    // instance variables
    private ExaminationId id;
    private Course course;
    private Student student;
    private int grade;
    private boolean withHonors;
    private LocalDate examinationDob;

    // default constructor
    public ExaminationDto() {}

    public ExaminationDto(
        Course course,
        Student student,
        int grade,
        boolean withHonors,
        LocalDate examinationDob
    ) {
        this.course = course;
        this.student = student;
        this.grade = grade;
        this.withHonors = withHonors;
        this.examinationDob = examinationDob;
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

    public LocalDate getExaminationDob() {
        return examinationDob;
    }


}
