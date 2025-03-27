package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.utils.CourseType;





public interface CourseService {

    /**
     * retrieve all courses
     * @return List<CourseDto>
     */
    List<CourseDto> getCourses();


    /**
     * retrieve a course by id
     * @param CourseId id
     * @return CourseDto object representing the course with the given id
     * @throws ObjectNotFoundException if no course with the given id exists
     * @throws NullPointerException if the id is null
     * @throws IllegalArgumentException if the id is empty
     * @throws UnsupportedOperationException if the id is not unique
     */
    CourseDto getCourseById(@NonNull CourseId id)
        throws ObjectNotFoundException;


    /**
     * retrieve a course by name and degree course name
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @return CourseDto object representing the course with the given name and degree course name
     * @throws ObjectNotFoundException if no course with the given name and degree course name exists
     * @throws NullPointerException if the course name or degree course name is null
     * @throws IllegalArgumentException if the course name or degree course name is empty
     * @throws UnsupportedOperationException if the course name or degree course name is not unique
     */
    CourseDto getCourseByNameAndDegreeCourseName(@NonNull String courseName, @NonNull String degreeCourseName)
        throws ObjectNotFoundException;

    /**
     * retrieve all courses by professor
     * @param professor
     * @return List<CourseDto>
     * @throws NullPointerException if the professor is null
     * @throws IllegalArgumentException if the professor is not found
     * @throws UnsupportedOperationException if the professor is not unique
     */
    public List<CourseDto> getCoursesByProfessor(@NonNull Professor professor);

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
     * @param oldCourseName the name of the course to be updated
     * @param oldDegreeCourseName the old degree course
     * @param newCourseName the new name of the course
     * @param newDegreeCourseName the new degree course
     * @param newType the new type of the course
     * @param newCfu the new cfu of the course
     * @param newUniqueCode the new unique code of the course
     * @return Course
     * @throws ObjectNotFoundException if no course with the given name exists
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is empty
     * @throws UnsupportedOperationException if any of the parameters is not unique
     */
    @NonNull
    @Transactional
    Course updateCourse(
        String oldCourseName,
        String oldDegreeCourseName,
        String newCourseName,
        String newDegreeCourseName,
        CourseType newType,
        Integer newCfu,
        String newUniqueCode
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
