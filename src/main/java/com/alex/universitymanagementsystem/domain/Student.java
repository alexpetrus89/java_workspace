package com.alex.universitymanagementsystem.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
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
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "STUDENTS")
@Access(AccessType.PROPERTY)
@PrimaryKeyJoinColumn(name = "id")
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

    public Student(Builder builder, PasswordEncoder encoder, Register register, DegreeCourse degreeCourse) {
        super(builder, encoder);
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
        String username,
        String firstName,
        String lastName,
        LocalDate dob,
        FiscalCode fiscalCode,
        Register register,
        DegreeCourse degreeCourse,
        StudyPlan studyPlan
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.fiscalCode = fiscalCode;
        this.register = register;
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


    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }

    // other methods
    @Override
    public String toString() {
        return "Student [id= " + id + ", name= " + firstName + " " + lastName +
            ", age= " + age + ", dob= " + dob + ", fiscal code= " + fiscalCode + ", register= " + register + ", email= " + username + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student other = (Student) o;
        return Objects.equals(id, other.id);
    }


    // private methods
    private int calculateAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }


}