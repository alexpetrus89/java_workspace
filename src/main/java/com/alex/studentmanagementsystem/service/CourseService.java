package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;



public interface CourseService {

    /**
     * retrieve all courses
     * @return List<CourseDto>
     */
    List<CourseDto> getCourses();

    /**
     * retrieve a course by id
     * @param CourseId id
     * @return CourseDto object
     * @throws ObjectNotFoundException if no course with the given id exists
     */
    CourseDto getCourseById(CourseId id)
        throws ObjectNotFoundException;

    /**
     * retrieve a course by name
     * @param String name
     * @return CourseDto object
     * @throws ObjectNotFoundException if no course with the given name exists
     */
    CourseDto getCourseByName(String name)
        throws ObjectNotFoundException;

    /**
     * add a new course
     * @param CourseDto courseDto
     * @throws ObjectAlreadyExistsException if a course with the same name
     *                                      already exists
     */
    @Transactional
    void addNewCourse(CourseDto courseDto)
        throws ObjectAlreadyExistsException;

    /**
     * update a course
     * @param CourseDto courseDto
     * @throws ObjectNotFoundException if no course with the given id exists
     */
    @Transactional
    public void updateCourse(CourseDto courseDto)
        throws ObjectNotFoundException;

    /**
     * delete a course
     * @param CourseId id
     * @throws ObjectNotFoundException if no course with the given id exists
     */
    @Transactional
    public void deleteCourse(CourseId id)
        throws ObjectNotFoundException;

}
