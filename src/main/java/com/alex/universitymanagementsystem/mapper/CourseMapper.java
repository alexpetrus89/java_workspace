package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.entity.Course;

public class CourseMapper {

    private CourseMapper() {} // private constructor to prevent instantiation

    public static Course toEntity(CourseDto dto) {
        if(dto == null) return null;
        return new Course(
            dto.getName(),
            dto.getType(),
            dto.getCfu(),
            ProfessorMapper.toEntity(dto.getProfessor()),
            DegreeCourseMapper.toEntity(dto.getDegreeCourse())
        );
    }

    public static CourseDto toDto(Course course) {
        if(course == null) return null;
        return new CourseDto(
            course.getName(),
            course.getType(),
            course.getCfu(),
            ProfessorMapper.toDto(course.getProfessor()),
            DegreeCourseMapper.toDto(course.getDegreeCourse())
        );
    }

}
