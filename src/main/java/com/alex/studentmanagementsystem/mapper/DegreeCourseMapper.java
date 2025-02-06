package com.alex.studentmanagementsystem.mapper;

import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.dto.DegreeCourseDto;

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
