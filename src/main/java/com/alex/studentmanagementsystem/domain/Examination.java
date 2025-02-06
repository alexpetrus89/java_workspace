package com.alex.studentmanagementsystem.domain;

import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.studentmanagementsystem.domain.immutable.ExaminationId;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;

@Entity
@Table(name = "examination")
@Access(AccessType.PROPERTY)
public class Examination implements Serializable {

    // instance variables
    private ExaminationId id;
    private Course course;
    private Student student;
    private int grade;
    private boolean withHonors;
    private LocalDate examinationDob;

    // default constructor
    public Examination() {}

    public Examination(
        Course course,
        Student student,
        int grade,
        boolean withHonors,
        LocalDate examinationDob
    ) {
        this.id = new ExaminationId(UUID.randomUUID());
        this.course = course;
        this.student = student;
        this.grade = grade;
        this.withHonors = withHonors;
        this.examinationDob = examinationDob;
    }


    // getters
    @EmbeddedId
    @Column(name = "examination_id")
    public ExaminationId getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(
        name = "course_id",
        foreignKey = @ForeignKey(name = "fk_examination_course")
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @PrimaryKeyJoinColumn(name = "course_id")
    public Course getCourse() {
        return course;
    }

    @ManyToOne
    @JoinColumn(
        name = "student_id",
        foreignKey = @ForeignKey(name = "fk_examination_student")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @PrimaryKeyJoinColumn(name = "student_id")
    public Student getStudent() {
        return student;
    }

    @Min(18)
    @Max(30)
    @Column(name = "grade")
    public int getGrade() {
        return grade;
    }

    @Column(name = "with_honors")
    public boolean isWithHonors() {
        return withHonors;
    }

    @Column(name = "examination_dob")
    public LocalDate getExaminationDob() {
        return examinationDob;
    }

    // setters
    public void setId(ExaminationId id) {
        this.id = id;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setGrade(int grade) {
        if (grade < 18 || grade > 30) {
            throw new IllegalArgumentException("Grade must be between 18 and 30");
        }
        this.grade = grade;
    }

    public void setWithHonors(boolean withHonors) {
        if(getGrade() != 30) {
            this.withHonors = false;
        }
        this.withHonors = withHonors;
    }

    public void setExaminationDob(LocalDate examinationDob) {
        this.examinationDob = examinationDob;
    }


}
