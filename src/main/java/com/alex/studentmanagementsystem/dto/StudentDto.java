package com.alex.studentmanagementsystem.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.Register;

public class StudentDto {

    // instance variables
    private Register register;
    private String name;
    private String email;
    private LocalDate dob;
    private DegreeCourse degreeCourse;

    // default constructor
    public StudentDto() {}

    // constructor
    public StudentDto(
        Register register,
        String name,
        String email,
        LocalDate dob,
        DegreeCourse degreeCourse
    ) {
        this.register = register;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.degreeCourse = degreeCourse;
    }

    // getters
    public Register getRegister() {
        return register;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public DegreeCourse getDegreeCourse() {
        return degreeCourse;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setDegreeCourse(DegreeCourse degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(register, name, email, dob);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Student other = (Student) obj;
        return Objects.equals(register, other.getRegister()) &&
            Objects.equals(name, other.getName()) &&
            Objects.equals(email, other.getEmail()) &&
            Objects.equals(dob, other.getDob());
    }

}
