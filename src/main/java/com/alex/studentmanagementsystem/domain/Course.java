package com.alex.studentmanagementsystem.domain;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.utils.CourseType;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
@Access(value = AccessType.PROPERTY)
public class Course implements Serializable {

    // instance variables
    private CourseId courseId;
    private String name;
    private CourseType type;
    private Integer cfu;
    private Professor professor;
    private DegreeCourse degreeCourse;

    // default constructor
    public Course() {}

    public Course(
        String name,
        CourseType type,
        Integer cfu
    ) {
        this.courseId = new CourseId(UUID.randomUUID());
        this.name = name;
        this.type = type;
        this.cfu = cfu;
    }

    public Course(
        String name,
        CourseType type,
        Integer cfu,
        Professor professor,
        DegreeCourse degreeCourse
    ) {
        this.courseId = new CourseId(UUID.randomUUID());
        this.name = name;
        this.type = type;
        this.cfu = cfu;
        this.professor = professor;
        this.degreeCourse = degreeCourse;
    }


    // getters
    @EmbeddedId
    @Column(name = "course_id")
    public CourseId getCourseId() {
        return courseId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "type")
    public CourseType getType() {
        return type;
    }

    @Column(name = "cfu")
    public Integer getCfu() {
        return cfu;
    }

    @ManyToOne // owning side
    public Professor getProfessor() {
        return professor;
    }

    @ManyToOne // owning side
    @JoinColumn(name = "degree_course_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public DegreeCourse getDegreeCourse() {
        return degreeCourse;
    }


    // setters
    public void setCourseId(CourseId courseId) {
        this.courseId = courseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public void setCfu(Integer cfu) {
        this.cfu = cfu;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setDegreeCourse(DegreeCourse degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    @Override
    public String toString() {
        return "Course{" +
            "courseId=" + courseId +
            ", name='" + name + '\'' +
            ", type='" + type.name() + '\'' +
            ", cfu=" + cfu +
            ", professor=" + professor +
            ", degreeCourse=" + degreeCourse +
            '}';
    }



}
