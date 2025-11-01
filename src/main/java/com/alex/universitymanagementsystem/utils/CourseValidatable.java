package com.alex.universitymanagementsystem.utils;

import com.alex.universitymanagementsystem.annotation.ValidYearOfStudy;


@ValidYearOfStudy
public interface CourseValidatable {
    Integer getYearOfStudy();
    String getDegreeCourseName();
}

