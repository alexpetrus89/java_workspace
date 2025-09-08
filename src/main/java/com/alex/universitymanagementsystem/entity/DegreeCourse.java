package com.alex.universitymanagementsystem.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import com.alex.universitymanagementsystem.entity.immutable.DegreeCourseId;
import com.alex.universitymanagementsystem.enum_type.DegreeType;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "DEGREE_COURSES")
@Access(AccessType.PROPERTY)
public class DegreeCourse implements Serializable {

    // instance variables
    private DegreeCourseId id;
    private String name;
    private DegreeType graduationClass;
    private int duration;
    private Collection<Course> courses = new HashSet<>();
    private Collection<Student> students = new HashSet<>();


    // constructors
    protected DegreeCourse() {}

    public DegreeCourse(String name, DegreeType graduationClass, int duration) {
        this.id = DegreeCourseId.newId();
        this.name = name;
        this.graduationClass = graduationClass;
        this.duration = duration;
    }

    public DegreeCourse(String name, DegreeType graduationClass, int duration, Collection<Course> courses, Collection<Student> students) {
        this.id = DegreeCourseId.newId();
        this.name = name;
        this.graduationClass = graduationClass;
        this.duration = duration;
        this.courses = courses;
        this.students = students;
    }


    // getters
    @EmbeddedId
    public DegreeCourseId getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "graduationClass")
    public DegreeType getGraduationClass() {
        return graduationClass;
    }

    @Column(name = "duration")
    public int getDuration() {
        return duration;
    }

    // DegreeCourse is the owner of the relationship
    @OneToMany(mappedBy = "degreeCourse",
        fetch = FetchType.EAGER, // carica i corsi immediatamente
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    public Collection<Course> getCourses() {
        return courses;
    }

    // reverse side
    @OneToMany(
        mappedBy = "degreeCourse",
        fetch = FetchType.LAZY, // carica gli studenti con ritardo
        cascade = CascadeType.ALL,
        orphanRemoval = false
    )
    public Collection<Student> getStudents() {
        return students;
    }


    // setters
    public void setId(DegreeCourseId id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGraduationClass(DegreeType graduationClass) {
        this.graduationClass = graduationClass;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCourses(Collection<Course> courses) {
        this.courses = courses;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }


    // Bi-directional helpers
    public void addCourse(Course course) {
        if (courses.contains(course))
            return; // already added
        courses.add(course);
        course.setDegreeCourse(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.setDegreeCourse(null);
    }

    public void addStudent(Student student) {
        if (students.contains(student))
            return; // already added
        this.students.add(student);
        student.setDegreeCourse(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setDegreeCourse(null);
    }

    // --- Object methods ---
    @Override
    public String toString() {
        return "DegreeCourse{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", graduationClass=" + graduationClass +
            ", duration=" + duration +
            ", coursesCount=" + (courses != null ? courses.size() : 0) +
            ", studentsCount=" + (students != null ? students.size() : 0) +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DegreeCourse dc)) return false;
        return Objects.equals(id, dc.id);
    }

}
