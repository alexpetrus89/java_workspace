package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;

public class DegreeCourseMapper {

    private DegreeCourseMapper() {} // private constructor to prevent instantiation


    public static DegreeCourse toEntity(DegreeCourseDto dto) {
        if(dto == null) return null;
        return new DegreeCourse(
            dto.getName(),
            dto.getGraduationClass(),
            dto.getDuration()
        );
    }


    public static DegreeCourseDto toDto(DegreeCourse degreeCourse) {
        if(degreeCourse == null) return null;
        return new DegreeCourseDto(
            degreeCourse.getId(),
            degreeCourse.getName(),
            degreeCourse.getGraduationClass(),
            degreeCourse.getDuration()
        );
    }

}
