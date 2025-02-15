package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.utility.CourseType;



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
     * @param name
     * @param type
     * @param cfu
     * @param uniqueCode
     * @param degreeCourseName
     * @return Course
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     */
    @Transactional
    Course addNewCourse(String name, CourseType type, Integer cfu, String uniqueCode, String degreeCourseName)
        throws ObjectAlreadyExistsException;


    /**
     * update a course
     * @param oldName
     * @param newName
     * @param newType
     * @param newCfu
     * @param newUniqueCode
     * @param newDegreeCourseName
     * @return Course
     * @throws ObjectNotFoundException if no course with the given name exists
     */
    @Transactional
    Course updateCourse(
        String oldName,
        String newName,
        CourseType newType,
        Integer newCfu,
        String newUniqueCode,
        String newDegreeCourseName
    ) throws ObjectNotFoundException;


    /**
     * delete a course
     * @param CourseId id
     * @throws ObjectNotFoundException if no course with the given id exists
     */
    @Transactional
    public void deleteCourse(CourseId id)
        throws ObjectNotFoundException;

}
