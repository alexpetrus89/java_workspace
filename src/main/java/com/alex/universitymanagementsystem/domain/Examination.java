package com.alex.universitymanagementsystem.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.universitymanagementsystem.domain.immutable.ExaminationId;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "EXAMINATIONS")
@Access(AccessType.PROPERTY)
public class Examination implements Serializable {

    // instance variables
    private ExaminationId id;
    private Course course;
    private String courseNameSnapshot;
    private String register; // es: matricola
    private String studentFirstName;
    private String studentLastName;
    private int grade;
    private boolean withHonors;
    private LocalDate date;

    // default constructor
    public Examination() {}

    public Examination(Course course, Student student, int grade, boolean withHonors, LocalDate date) {
        this.id = ExaminationId.newId();
        this.course = course;
        this.courseNameSnapshot = course.getName();
        this.register = student.getRegister().toString();
        this.studentFirstName = student.getFirstName();
        this.studentLastName = student.getLastName();
        this.grade = grade;
        this.withHonors = withHonors;
        this.date = date;
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
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    public Course getCourse() {
        return course;
    }

    @Column(name = "course_name_snapshot", nullable = false, length = 255)
    public String getCourseNameSnapshot() {
        return courseNameSnapshot;
    }

    // --- snapshot dello studente ---
    @Column(name = "register", nullable = false, length = 20)
    public String getRegister() { // es: matricola
        return register;
    }

    @Column(name = "student_first_name", nullable = false)
    public String getStudentFirstName() {
        return studentFirstName;
    }

    @Column(name = "student_last_name", nullable = false)
    public String getStudentLastName() {
        return studentLastName;
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
    public LocalDate getDate() {
        return date;
    }

    // setters
    public void setId(ExaminationId id) {
        this.id = id;
    }

    public void setCourseNameSnapshot(String courseNameSnapshot) {
        this.courseNameSnapshot = courseNameSnapshot;
    }

    public void setCourse(Course course) {
        this.course = course;
        if (course != null)
            this.courseNameSnapshot = course.getName(); // aggiorno anche lo snapshot
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public void setGrade(int grade) {
        if (grade < 18 || grade > 30)
            throw new IllegalArgumentException("Grade must be between 18 and 30");
        this.grade = grade;
    }

    public void setWithHonors(boolean withHonors) {
        if(getGrade() != 30)
            this.withHonors = false;
        this.withHonors = withHonors;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStudent(Student student) {
        this.register = student.getRegister().toString();
        this.studentFirstName = student.getFirstName();
        this.studentLastName = student.getLastName();
    }

    @Override
    public String toString() {
        return "Examination [id=" + id +
            ", course=" + course +
            ", student=" + studentFirstName + " " + studentLastName +
            ", register=" + register +
            ", grade=" + grade +
            ", withHonors=" + withHonors
            + ", date=" + date + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Examination)) return false;
        Examination other = (Examination) o;
        return Objects.equals(id, other.id);
    }


}
