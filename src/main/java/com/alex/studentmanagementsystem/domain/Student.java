package com.alex.studentmanagementsystem.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.StudentId;

import java.io.Serializable;



import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.AccessType;
import jakarta.persistence.Access;

@Entity
@Table(name = "student")
@Access(AccessType.PROPERTY)
public class Student implements Serializable {

    // instance variables
    private StudentId id;
    private Register register;
    private String name;
    private String email;
    private LocalDate dob;
    private Integer age;
    private DegreeCourse degreeCourse;

    // constructors
    public Student() {}


    public Student(
        Register register,
        String name,
        String email,
        LocalDate dob,
        DegreeCourse degreeCourse
    ) {
        this.id = new StudentId(UUID.randomUUID());
        this.register = register;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.age = calculateAge();
        this.degreeCourse = degreeCourse;
    }

    // getters
    @EmbeddedId // it's an id
    @Column(name = "student_id")
    public StudentId getId() {
        return id;
    }

    @Embedded
    @AttributeOverride(
        name = "register",
        column = @Column(name = "register")
    )
    public Register getRegister() {
        return register;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "dob")
    public LocalDate getDob() {
        return dob;
    }

    @Transient
    public Integer getAge() {
        return age;
    }

    @ManyToOne
    public DegreeCourse getDegreeCourse() {
        return degreeCourse;
    }


    // setters
    public void setId(StudentId id) {
        this.id = id;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setAge(Integer age) {
        this.age = age;
    }

    // other methods
    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name +
            ",email=" + email + ", dob=" + dob + ", age=" + age + "]";
    }


    private int calculateAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }
}