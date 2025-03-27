package com.alex.universitymanagementsystem.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "examination_appeal")
@Access(AccessType.PROPERTY)
public class ExaminationAppeal implements Serializable {

    // instance variables
    private Long id;
    private Course course;
    private String degreeCourse;
    private UniqueCode professor;
    private String description;
    private LocalDate date;
    private Set<Register> students = new HashSet<>();

    // constructor
    public ExaminationAppeal() {}

    public ExaminationAppeal(Course course, String description, LocalDate date) {
        this.course = course;
        this.degreeCourse = course.getDegreeCourse().getName();
        this.professor = course.getProfessor().getUniqueCode();
        this.description = description;
        this.date = date;
    }

    // getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_appeal_id")
    public Long getId() { return id; }

    @OneToOne
    @JoinColumn(name = "course_id")
    public Course getCourse() { return course; }

    @Column(name = "degreeCourse")
    public String getDegreeCourse() { return degreeCourse; }

    @Column(name = "professor")
    public UniqueCode getProfessor() { return professor; }

    @Column(name = "description")
    public String getDescription() { return description; }

    @Column(name = "date")
    public LocalDate getDate() { return date; }

    @Column(name = "students")
    @ElementCollection
    public Set<Register> getStudents() { return students; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setCourse(Course course) { this.course = course; }
    public void setDegreeCourse(String degreeCourse) { this.degreeCourse = degreeCourse; }
    public void setProfessor(UniqueCode professor) { this.professor = professor; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStudents(Set<Register> students) { this.students = students; }

    @Override
    public String toString() {
        return "ExaminationAppeal [course=" + course.getName() + ", professor=" + professor.toString() + ", description=" + description + ", date=" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, degreeCourse, professor, description, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExaminationAppeal other = (ExaminationAppeal) obj;
        return Objects.equals(course, other.getCourse()) &&
            Objects.equals(degreeCourse, other.getDegreeCourse()) &&
            Objects.equals(professor, other.getProfessor()) &&
            Objects.equals(description, other.getDescription()) &&
            Objects.equals(date, other.getDate());
    }

    // other methods
    public boolean addStudent(Register student) {
        // check if register is null
        if (student == null)
            throw new IllegalArgumentException("Register cannot be null");

        return this.students.add(student);
    }

    public boolean removeStudent(Register student) {
        if (student == null)
            throw new IllegalArgumentException("Register cannot be null");

        return this.students.remove(student);
    }



}
