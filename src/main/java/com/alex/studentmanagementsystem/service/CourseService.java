package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.utils.CourseType;



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
     * @throws NullPointerException if the id is null
     * @throws IllegalArgumentException if the id is empty
     * @throws UnsupportedOperationException if the id is not unique
     */
    CourseDto getCourseById(@NonNull CourseId id)
        throws ObjectNotFoundException;


    /**
     * retrieve a course by name
     * @param String name
     * @return CourseDto object
     * @throws ObjectNotFoundException if no course with the given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    CourseDto getCourseByName(@NonNull String name)
        throws ObjectNotFoundException;


    /**
     * add a new course
     * @param name of the course
     * @param type of the course
     * @param cfu of the course
     * @param uniqueCode of the course
     * @param degreeCourseName of the course
     * @return Course
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is empty
     * @throws UnsupportedOperationException if any of the parameters is not unique
     */
    @NonNull
    @Transactional
    Course addNewCourse(String name, CourseType type, Integer cfu, String uniqueCode, String degreeCourseName)
        throws ObjectAlreadyExistsException;


    /**
     * update a course
     * @param oldName the name of the course to be updated
     * @param newName the new name of the course
     * @param newType the new type of the course
     * @param newCfu the new cfu of the course
     * @param newUniqueCode the new unique code of the course
     * @param newDegreeCourseName the new name of the degree course
     * @return Course
     * @throws ObjectNotFoundException if no course with the given name exists
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is empty
     * @throws UnsupportedOperationException if any of the parameters is not unique
     */
    @NonNull
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
     * @param CourseId id to be deleted
     * @throws ObjectNotFoundException if no course with the given id exists
     * @throws NullPointerException if the id is null
     * @throws IllegalArgumentException if the id is empty
     * @throws UnsupportedOperationException if the id is not unique
     */
    @Transactional
    public void deleteCourse(@NonNull CourseId id)
        throws ObjectNotFoundException;

}
