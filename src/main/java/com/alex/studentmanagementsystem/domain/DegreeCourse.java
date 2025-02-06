package com.alex.studentmanagementsystem.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.studentmanagementsystem.domain.immutable.DegreeCourseId;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "degreeCourse")
@Access(AccessType.PROPERTY)
public class DegreeCourse implements Serializable {

    // instance variables
    private DegreeCourseId id;
    private String name;
    private String graduationClass;
    private int duration;
    private Collection<Course> courses = new HashSet<>();
    private Collection<Student> students = new HashSet<>();


    // constructors
    public DegreeCourse() {}

    public DegreeCourse(
        String name,
        String graduationClass,
        int duration
    ) {
        this.id = new DegreeCourseId(UUID.randomUUID());
        this.name = name;
        this.graduationClass = graduationClass;
        this.duration = duration;
    }

    public DegreeCourse(
        String name,
        String graduationClass,
        int duration,
        Collection<Course> courses,
        Collection<Student> students
    ) {
        this.id = new DegreeCourseId(UUID.randomUUID());
        this.name = name;
        this.graduationClass = graduationClass;
        this.duration = duration;
        this.courses = courses;
        this.students = students;
    }


    // getters
    @EmbeddedId
    @Column(name = "degreeCourse_id")
    public DegreeCourseId getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "graduationClass")
    public String getGraduationClass() {
        return graduationClass;
    }

    @Column(name = "duration")
    public int getDuration() {
        return duration;
    }

    // DegreeCourse is the owner of the relationship
    @OneToMany
    public Collection<Course> getCourses() {
        return courses;
    }

    @OneToMany // lato inverso
    @JoinColumn(
        name = "degree_course_id",
        foreignKey = @ForeignKey(name = "fk_degreeCourse_student")
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
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

    public void setGraduationClass(String graduationClass) {
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


    // methods
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

}
