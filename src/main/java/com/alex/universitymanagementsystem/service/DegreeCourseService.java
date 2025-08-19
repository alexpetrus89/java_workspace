package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;



public interface DegreeCourseService {

    /**
     * Retrieves all degree courses from the repository and maps them to DTOs.
     * @return Set of DegreeCourseDto objects representing all degree courses.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    Set<DegreeCourseDto> getDegreeCourses() throws DataAccessServiceException;


    /**
     * Retrieves a degree course from the repository by its name and
     * maps it to a DTO.
     * @param String name the name of the degree course
     * @return DegreeCourseDto object representing the degree course
     *         with the given name.
     * @throws IllegalArgumentException if the name is blank.
     * @throws NoSuchElementException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    DegreeCourseDto getDegreeCourseByName(String name)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Retrieves all courses of a given degree course from the repository and
     * maps them to DTOs.
     * @param name name of the degree course
     * @return List<CourseDto> objects representing all courses of the given
     *         degree course.
     * @throws IllegalArgumentException if the name is empty.
     * @throws NoSuchElementException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<CourseDto> getCourses(String name)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Retrieves all professors of a given degree course from the
     * repository and maps them to DTOs.
     * @param name name of the degree course
     * @return List<ProfessorDto> representing all professors of the given
     *         degree course
     * @throws IllegalArgumentException if the name is blank.
     * @throws NoSuchElementException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<ProfessorDto> getProfessors(String name)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Retrieves all students of a given degree course from the
     * repository and maps them to DTOs.
     * @param name name of the degree course
     * @return List of StudentDto objects representing all students of the
     *         given degree course.
     * @throws IllegalArgumentException if the name is blank.
     * @throws NoSuchElementException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<StudentDto> getStudents(String name)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


}
