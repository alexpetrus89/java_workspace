package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.domain.immutable.DegreeCourseId;
import com.alex.universitymanagementsystem.utils.DegreeType;

public class DegreeCourseDto {

    // instance variables
    private DegreeCourseId id;
    private String name;
    private DegreeType graduationClass;
    private int duration;


    // constructors
    public DegreeCourseDto() {}


    public DegreeCourseDto(
        DegreeCourseId id,
        String name,
        DegreeType graduationClass,
        int duration
    ) {
        this.id = id;
        this.name = name;
        this.graduationClass = graduationClass;
        this.duration = duration;
    }


    // getters
    public DegreeCourseId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DegreeType getGraduationClass() {
        return graduationClass;
    }

    public int getDuration() {
        return duration;
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

}
