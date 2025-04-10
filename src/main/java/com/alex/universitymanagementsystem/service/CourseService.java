package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;





public interface CourseService {

    /**
     * retrieve all courses
     * @return Set<CourseDto>
     */
    Set<CourseDto> getCourses();


    /**
     * retrieve a course by id
     * @param CourseId id
     * @return CourseDto object representing the course with the given id
     * @throws NullPointerException if the id is null
     * @throws IllegalArgumentException if the id is empty
     * @throws UnsupportedOperationException if the id is not unique
     */
    CourseDto getCourseById(@NonNull CourseId id)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * retrieve a course by name and degree course name
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @return CourseDto object representing the course with the given name and degree course name
     * @throws NullPointerException if the course name or degree course name is null
     * @throws IllegalArgumentException if the course name or degree course name is empty
     * @throws UnsupportedOperationException if the course name or degree course name is not unique
     */
    CourseDto getCourseByNameAndDegreeCourseName(@NonNull String courseName, @NonNull String degreeCourseName)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;

    /**
     * retrieve all courses by professor
     * @param professor
     * @return List<CourseDto>
     * @throws NullPointerException if the professor is null
     * @throws UnsupportedOperationException if the professor is not unique
     */
    public List<CourseDto> getCoursesByProfessor(@NonNull Professor professor)
        throws NullPointerException, UnsupportedOperationException;

    /**
     * add a new course
     * @param name of the course
     * @param type of the course
     * @param cfu of the course
     * @param uniqueCode of the course
     * @param degreeCourseName of the course
     * @return Course
     * @throws NullPointerException if any of the parameters is null
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     * @throws ObjectNotFoundException if no professor with the given unique code exists
     *         or no degree course with the given name exists.
     * @throws IllegalArgumentException if any of the parameters is empty
     * @throws UnsupportedOperationException if any of the parameters is not unique
     */
    @Transactional
    CourseDto addNewCourse(
        @NonNull String name,
        @NonNull CourseType type,
        @NonNull Integer cfu,
        @NonNull String uniqueCode,
        @NonNull String degreeCourseName
    ) throws NullPointerException,
        ObjectAlreadyExistsException,
        ObjectNotFoundException,
        IllegalArgumentException,
        UnsupportedOperationException;


    /**
     * update a course
     * @param oldCourseName the name of the course to be updated
     * @param oldDegreeCourseName the old degree course
     * @param newCourseName the new name of the course
     * @param newDegreeCourseName the new degree course
     * @param newType the new type of the course
     * @param newCfu the new cfu of the course
     * @param newUniqueCode the new unique code of the course
     * @return Course
     * @throws ObjectNotFoundException if no course with the given name exists
     * @throws IllegalArgumentException if any of the parameters is empty
     * @throws NullPointerException if any of the parameters is null
     * @throws UnsupportedOperationException if any of the parameters is not unique
     */
    @Transactional
    Course updateCourse(
        @NonNull String oldCourseName,
        @NonNull String oldDegreeCourseName,
        @NonNull String newCourseName,
        @NonNull String newDegreeCourseName,
        @NonNull CourseType newType,
        @NonNull Integer newCfu,
        @NonNull String newUniqueCode
    ) throws NullPointerException,ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * delete a course
     * @param CourseId id to be deleted
     * @throws NullPointerException if the id is null
     * @throws ObjectNotFoundException if no course with the given id exists
     * @throws IllegalArgumentException if the id is empty
     * @throws UnsupportedOperationException if the id is not unique
     */
    @Transactional
    void deleteCourse(@NonNull CourseId id)
        throws NullPointerException, ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException;

}
