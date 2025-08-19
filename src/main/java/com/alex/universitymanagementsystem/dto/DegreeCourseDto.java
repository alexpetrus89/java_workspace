package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.domain.immutable.DegreeCourseId;
import com.alex.universitymanagementsystem.enum_type.DegreeType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DegreeCourseDto {

    // instance variables
    private DegreeCourseId id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
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
