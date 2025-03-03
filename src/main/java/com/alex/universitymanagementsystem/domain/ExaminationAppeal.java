package com.alex.universitymanagementsystem.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ExaminationAppeal {

    // instance variables
    private Course course;
    private DegreeCourse degreeCourse;
    private Professor professor;
    private String description;
    private LocalDate date;
    private List<Student> students;

    // constructor
    public ExaminationAppeal() {}

    public ExaminationAppeal(Course course, DegreeCourse degreeCourse, Professor professor, String description, LocalDate date) {
        this.course = course;
        this.degreeCourse = degreeCourse;
        this.professor = professor;
        this.description = description;
        this.date = date;
    }

    // getters
    public Course getCourse() { return course; }
    public DegreeCourse getDegreeCourse() { return degreeCourse; }
    public Professor getProfessor() { return professor; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public List<Student> getStudents() { return students; }

    // setters
    public void setCourse(Course course) { this.course = course; }
    public void setDegreeCourse(DegreeCourse degreeCourse) { this.degreeCourse = degreeCourse; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return "ExaminationAppeal [course=" + course + ", professor=" + professor + ", description=" + description + ", date=" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "]";
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
    public void addStudent(Student student) {
        // check if register is null
        if (student.getRegister() == null)
            throw new IllegalArgumentException("Register cannot be null");

        this.students.add(student);
    }



}
