package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;



public interface CourseService {

    List<CourseDto> getCourses();

    CourseDto getCourseById(CourseId id)
        throws ObjectNotFoundException;

    CourseDto getCourseByName(String name)
        throws ObjectNotFoundException;

    @Transactional
    void addNewCourse(CourseDto courseDto)
        throws ObjectAlreadyExistsException;

    @Transactional
    public void updateCourse(CourseDto courseDto)
        throws ObjectNotFoundException;

    @Transactional
    public void deleteCourse(CourseId id)
        throws ObjectNotFoundException;

}
