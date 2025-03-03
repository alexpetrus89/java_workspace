package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.dto.CourseDto;

public class CourseMapper {

    private CourseMapper() {} // private constructor to prevent instantiation

    public static Course mapToCourse(CourseDto courseDto) {
        return new Course(
            courseDto.getName(),
            courseDto.getType(),
            courseDto.getCfu(),
            courseDto.getProfessor(),
            courseDto.getDegreeCourse()
        );
    }

    public static CourseDto mapToCourseDto(Course course) {
        return new CourseDto(
            course.getCourseId(),
            course.getName(),
            course.getType(),
            course.getCfu(),
            course.getProfessor(),
            course.getDegreeCourse()
        );
    }

}
