package com.alex.universitymanagementsystem.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.domain.immutable.Register;



public class StudentDto {

    // instance variables
    private Register register;
    private String username;
    private String fullname;
    private LocalDate dob;
    private DegreeCourse degreeCourse;
    private StudyPlan studyPlan;

    // default constructor
    public StudentDto() {}

    // constructors
    public StudentDto(
        Register register,
        String username,
        String fullname,
        LocalDate dob,
        DegreeCourse degreeCourse
    ) {
        this.register = register;
        this.username = username;
        this.fullname = fullname;
        this.dob = dob;
        this.degreeCourse = degreeCourse;
    }

    public StudentDto(
        Register register,
        String username,
        String fullname,
        LocalDate dob,
        DegreeCourse degreeCourse,
        StudyPlan studyPlan
    ) {
        this.register = register;
        this.username = username;
        this.fullname = fullname;
        this.dob = dob;
        this.degreeCourse = degreeCourse;
        this.studyPlan = studyPlan;
    }

    // getters
    public Register getRegister() {
        return register;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public DegreeCourse getDegreeCourse() {
        return degreeCourse;
    }

    public StudyPlan getStudyPlan() {
        return studyPlan;
    }

    // setters
    public void setRegister(Register register) {
        this.register = register;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setDegreeCourse(DegreeCourse degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(register, username, fullname, dob);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Student other = (Student) obj;
        return Objects.equals(register, other.getRegister()) &&
            Objects.equals(username, other.getUsername()) &&
            Objects.equals(fullname, other.getFullname()) &&
            Objects.equals(dob, other.getDob());
    }

}
