package com.alex.universitymanagementsystem.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.utils.Builder;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "student")
@Access(AccessType.PROPERTY)
public class Student extends User {

    // instance variables
    private Register register;
    private Integer age;
    private DegreeCourse degreeCourse;
    private StudyPlan studyPlan;
    private static AtomicInteger registerCounter = new AtomicInteger(100000);

    // constructors
    public Student() {}

    public Student(Builder builder, PasswordEncoder passwordEncoder) {
        super(builder, passwordEncoder);
        this.register = new Register(String.format("%06d", registerCounter.getAndIncrement()));
        this.age = calculateAge();
    }

    public Student(
        Builder builder,
        PasswordEncoder passwordEncoder,
        Register register,
        DegreeCourse degreeCourse
    ) {
        super(builder, passwordEncoder);
        this.register = register;
        this.degreeCourse = degreeCourse;
        this.age = calculateAge();
    }

    public Student(
        Builder builder,
        PasswordEncoder passwordEncoder,
        Register register,
        DegreeCourse degreeCourse,
        StudyPlan studyPlan
    ) {
        super(builder, passwordEncoder);
        this.register = register;
        this.degreeCourse = degreeCourse;
        this.studyPlan = studyPlan;
        this.age = calculateAge();
    }

    public Student(
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
        this.age = calculateAge();
        this.degreeCourse = degreeCourse;
        this.studyPlan = studyPlan;
        this.age = calculateAge();
    }

    // getters
    @Embedded
    @AttributeOverride(
        name = "register",
        column = @Column(name = "register")
    )
    public Register getRegister() {
        return register;
    }

    @Transient
    public Integer getAge() {
        return age;
    }

    @ManyToOne
    public DegreeCourse getDegreeCourse() {
        return degreeCourse;
    }

    @OneToOne(mappedBy = "student")
    public StudyPlan getStudyPlan() {
        return studyPlan;
    }


    // setters
    public void setRegister(Register register) {
        this.register = register;
    }

    public void setDegreeCourse(DegreeCourse degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }

    // other methods
    @Override
    public String toString() {
        return "Student [id= " + id + ", name= " + fullname +
            "email= " + username + ", dob= " + dob + ", age= " + age + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(register, username, fullname, dob);
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj) return true;

        // null check
        if (obj == null || getClass() != obj.getClass()) return false;

        // cast
        Student other = (Student) obj;

        // check the fields
        return Objects.equals(register, other.getRegister()) &&
            Objects.equals(username, other.getUsername()) &&
            Objects.equals(fullname, other.getFullname()) &&
            Objects.equals(dob, other.getDob());
    }

    private int calculateAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }


}