package com.alex.universitymanagementsystem.domain;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.MiurCourseCode;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.enum_type.MiurAcronymType;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "COURSES")
@Access(value = AccessType.PROPERTY)
public class Course implements Serializable {

    // instance variables
    private CourseId id;
    private MiurCourseCode code;
    private String name;
    private CourseType type;
    private Integer cfu;
    private Professor professor;
    private DegreeCourse degreeCourse;

    // default constructor
    public Course() {}

    public Course(String name, CourseType type, Integer cfu) {
        this.id = CourseId.newId();
        this.name = name;
        this.type = type;
        this.cfu = cfu;
    }

    public Course(String name, CourseType type, Integer cfu, Professor professor, DegreeCourse degreeCourse) {
        this.id = CourseId.newId();
        this.name = name;
        this.type = type;
        this.cfu = cfu;
        this.professor = professor;
        this.degreeCourse = degreeCourse;
    }

    public Course(MiurAcronymType acronym, String name, CourseType type, Integer cfu, Professor professor, DegreeCourse degreeCourse) {
        this.id = CourseId.newId();
        this.code = MiurCourseCode.generate(acronym);
        this.name = name;
        this.type = type;
        this.cfu = cfu;
        this.professor = professor;
        this.degreeCourse = degreeCourse;
    }


    // getters
    @EmbeddedId
    @Column(name = "course_id")
    public CourseId getId() {
        return id;
    }

    @Embedded
    @Column(name = "miur_course_code", unique = true, nullable = false)
    public MiurCourseCode getCode() {
        return code;
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
    @OnDelete(action = OnDeleteAction.SET_NULL)
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
    public void setId(CourseId id) {
        this.id = id;
    }

    public void setCode(MiurCourseCode code) {
        this.code = code;
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
            "id=" + id +
            ", code=" + code +
            ", name='" + name + '\'' +
            ", type='" + type.name() + '\'' +
            ", cfu=" + cfu +
            ", professor=" + professor +
            ", degreeCourse=" + degreeCourse +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course other = (Course) o;
        return Objects.equals(id, other.id);
    }



}
