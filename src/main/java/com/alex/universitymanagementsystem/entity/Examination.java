package com.alex.universitymanagementsystem.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.universitymanagementsystem.entity.immutable.ExaminationId;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "EXAMINATIONS")
@Access(AccessType.PROPERTY)
public class Examination implements Serializable {

    // instance variables
    private ExaminationId id;
    private Course course;
    private String courseNameSnapshot;

    // student's snapshot
    private String register; // es: matricola
    private String studentFirstName;
    private String studentLastName;

    private int grade;
    private boolean withHonors;
    private LocalDate date;

    // constructors
    public Examination() {}

    public Examination(Course course, Student student, int grade, boolean withHonors, LocalDate date) {
        this.id = ExaminationId.newId();
        this.course = course;
        this.courseNameSnapshot = course.getName();
        this.register = student.getRegister().toString();
        this.studentFirstName = student.getFirstName();
        this.studentLastName = student.getLastName();
        initializeGrade(grade);
        initializeWithHonors(withHonors);
        this.date = date;
    }

    // Factory method for easier creation
    public static Examination of(Course course, Student student, int grade, boolean withHonors, LocalDate date) {
        return new Examination(course, student, grade, withHonors, date);
    }


    // getters
    @EmbeddedId
    public ExaminationId getId() { return id; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "course_id",
        foreignKey = @ForeignKey(name = "fk_examination_course")
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    public Course getCourse() { return course; }

    @Column(name = "course_name_snapshot", nullable = false, length = 255)
    public String getCourseNameSnapshot() { return courseNameSnapshot; }

    // --- snapshot dello studente ---
    @Column(name = "register", nullable = false, length = 20)
    public String getRegister() { return register; }

    @Column(name = "student_first_name", nullable = false, length = 50)
    public String getStudentFirstName() { return studentFirstName; }

    @Column(name = "student_last_name", nullable = false, length = 50)
    public String getStudentLastName() { return studentLastName; }

    @NotNull
    @Min(18)
    @Max(30)
    @Column(name = "grade")
    public Integer getGrade() { return grade; }

    @Column(name = "with_honors")
    public boolean isWithHonors() { return withHonors; }

    @NotNull
    @Column(name = "examination_date")
    public LocalDate getDate() { return date; }

    // setters
    public void setId(ExaminationId id) { this.id = id; }

    public void setCourse(Course course) {
        this.course = course;
        if (course != null)
            setCourseNameSnapshot(course.getName()); // aggiorno anche lo snapshot
    }

    public void setCourseNameSnapshot(String name) { this.courseNameSnapshot = name; }
    public void setRegister(String register) { this.register = register; }
    public void setStudentFirstName(String studentFirstName) { this.studentFirstName = studentFirstName; }
    public void setStudentLastName(String studentLastName) { this.studentLastName = studentLastName; }

    public void setGrade(int grade) {
        if (grade < 18 || grade > 30)
            throw new IllegalArgumentException("Grade must be between 18 and 30");
        this.grade = grade;
    }

    public void setWithHonors(boolean withHonors) { this.withHonors = (grade == 30) && withHonors; }
    public void setDate(LocalDate date) { this.date = date; }

    public void setStudentSnapshot(Student student) {
        this.register = student.getRegister().toString();
        this.studentFirstName = student.getFirstName();
        this.studentLastName = student.getLastName();
    }


    // --- Object methods ---
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


    // initialization
    private void initializeGrade(int grade) {
        setGrade(grade);
    }

    private void initializeWithHonors(boolean withHonors) {
        setWithHonors(withHonors);
    }


}
