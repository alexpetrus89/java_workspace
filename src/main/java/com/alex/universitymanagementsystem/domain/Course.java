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
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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

    // constructors
    protected Course() {}

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
    public CourseId getId() {
        return id;
    }

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "miur_course_code", unique = true, nullable = false)
    )
    @Column(name = "miur_course_code", unique = true, nullable = false)
    public MiurCourseCode getCode() {
        return code;
    }

    @Column(name = "name", nullable=false)
    public String getName() {
        return name;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    public CourseType getType() {
        return type;
    }

    @Column(name = "cfu", nullable = false)
    public Integer getCfu() {
        return cfu;
    }

    // owning side
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "professor_id", foreignKey = @ForeignKey(name = "fk_course_professor"))
    public Professor getProfessor() {
        return professor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "degree_course_id", foreignKey = @ForeignKey(name = "fk_course_degree_course"))
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

    // --- Object methods ---
    @Override
    public String toString() {
        return "Course{" +
            "id=" + id +
            ", code=" + code +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", cfu=" + cfu +
            ", professorId=" + (professor != null ? professor.getId() : null) +
            ", degreeCourseId=" + (degreeCourse != null ? degreeCourse.getId() : null) +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course c)) return false;
        return Objects.equals(code, c.code);
    }



}
