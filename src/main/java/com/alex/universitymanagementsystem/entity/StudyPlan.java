package com.alex.universitymanagementsystem.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.universitymanagementsystem.entity.immutable.StudyPlanId;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "STUDY_PLANS")
@Access(AccessType.PROPERTY)
public class StudyPlan implements Serializable {

    // instance variables
    private StudyPlanId id;
    private Student student;
    private String ordering;
    private Set<Course> courses = new HashSet<>();

    // constructors
    protected StudyPlan() {}

    public StudyPlan(String ordering, Set<Course> courses) {
        this.id = StudyPlanId.newId();
        this.ordering = ordering;
        this.courses = courses;
    }

    public StudyPlan(Student student, String ordering, Set<Course> courses) {
        this.id = StudyPlanId.newId();
        this.student = student;
        this.ordering = ordering;
        this.courses = courses;
    }

    // getters
    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "study_plan_id"))
    public StudyPlanId getId() { return id; }


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Student getStudent() {
        return student;
    }

    @Column(name = "ordering", length = 255)
    public String getOrdering() {
        return ordering;
    }

    @ManyToMany(targetEntity = Course.class, fetch = FetchType.LAZY)
    @JoinTable(
        name = "study_plan_courses",
        joinColumns = @JoinColumn(name = "study_plan_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "courses")
    public Set<Course> getCourses() {
        return courses;
    }

    // setters
    public void setId(StudyPlanId id) { this.id = id; }
    public void setStudent(Student student) { this.student = student; }
    public void setOrdering(String ordering) { this.ordering = ordering; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }


    // business methods
    public boolean addCourse(Course course) {
        return this.courses.add(course);
    }

    public boolean removeCourse(Course course) {
        return this.courses.remove(course);
    }

    @Override
    public String toString() {
        return "StudyPlan{" +
            "id=" + id +
            ", student=" + student +
            ", ordering='" + ordering + '\'' +
            ", courses=" + courses +
            '}';
    }




}
