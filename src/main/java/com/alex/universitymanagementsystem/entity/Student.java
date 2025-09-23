package com.alex.universitymanagementsystem.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.entity.immutable.Register;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
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
    public Student() { super(); } // public for mapper use

    public Student(RegistrationForm form, PasswordEncoder passwordEncoder) {
        super(form, passwordEncoder);
        this.register = new Register(String.format("%06d", registerCounter.getAndIncrement()));
        this.age = calculateAge();
    }

    public Student(RegistrationForm form, PasswordEncoder encoder, Register register, DegreeCourse degreeCourse) {
        super(form, encoder);
        this.register = register;
        this.degreeCourse = degreeCourse;
        this.age = calculateAge();
    }

    public Student(RegistrationForm form, PasswordEncoder encoder, Register register, DegreeCourse degreeCourse, StudyPlan studyPlan) {
        super(form, encoder);
        this.register = register;
        this.degreeCourse = degreeCourse;
        this.studyPlan = studyPlan;
        this.age = calculateAge();
    }


    // getters
    @Embedded
    public Register getRegister() { return register; }

    @Transient
    public Integer getAge() { return age; }

    // owning side
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
        name = "degree_course_id",
        foreignKey = @ForeignKey(name = "fk_student_degreeCourse")
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public DegreeCourse getDegreeCourse() { return degreeCourse; }

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    public StudyPlan getStudyPlan() { return studyPlan; }


    // setters
    public void setRegister(Register register) { this.register = register; }
    public void setDegreeCourse(DegreeCourse degreeCourse) { this.degreeCourse = degreeCourse; }
    public void setStudyPlan(StudyPlan studyPlan) { this.studyPlan = studyPlan; }

    // --- Object methods ---
    @Override
    public String toString() {
        return "Student [id= " + id +
            ", name= " + firstName +
            " " + lastName +
            ", age= " + age +
            ", dob= " + dob +
            ", fiscal code= " + fiscalCode +
            ", register= " + register +
            ", email= " + username +
            ", degreeCourseId=" + (degreeCourse != null ? degreeCourse.getId() : null) +
            "]";
    }

    /**
     * Returns a hash code value for this object. The hash code is equal to
     * the hash code of the identifier of the student.
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(register);
    }

    /**
     * Two students are considered equal if and only if they have the same identifier.
     * The identifier is immutable and unique for each student.
     * @param o the other object to compare with
     * @return true if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student other = (Student) o;
        return Objects.equals(register, other.register);
    }


    // private methods
    private int calculateAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }


}