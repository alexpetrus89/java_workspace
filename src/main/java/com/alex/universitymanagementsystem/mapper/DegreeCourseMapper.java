package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;

public class DegreeCourseMapper {

    private DegreeCourseMapper() {} // private constructor to prevent instantiation


    public static DegreeCourse mapToDegreeCourse(DegreeCourseDto degreeCourseDto) {
        return new DegreeCourse(
            degreeCourseDto.getName(),
            degreeCourseDto.getGraduationClass(),
            degreeCourseDto.getDuration()
        );
    }


    public static DegreeCourseDto mapToDegreeCourseDto(DegreeCourse degreeCourse) {
        return new DegreeCourseDto(
            degreeCourse.getId(),
            degreeCourse.getName(),
            degreeCourse.getGraduationClass(),
            degreeCourse.getDuration()
        );
    }

}
