package com.alex.universitymanagementsystem.utils;

import com.alex.universitymanagementsystem.annotation.ValidYearOfStudy;


@ValidYearOfStudy
public interface CourseDtoCarrier {

    Integer getYearOfStudy();
    String getDegreeCourseName();

}
