package com.alex.studentmanagementsystem.dto;

import com.alex.studentmanagementsystem.domain.immutable.DegreeCourseId;

public class DegreeCourseDto {

    // instance variables
    private DegreeCourseId id;
    private String name;
    private String graduationClass;
    private int duration;


    // constructors
    public DegreeCourseDto() {}


    public DegreeCourseDto(
        DegreeCourseId id,
        String name,
        String graduationClass,
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

    public String getGraduationClass() {
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

    public void setGraduationClass(String graduationClass) {
        this.graduationClass = graduationClass;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
