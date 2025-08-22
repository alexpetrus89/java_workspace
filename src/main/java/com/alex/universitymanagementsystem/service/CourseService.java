package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.Set;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.UpdateCourseDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;





public interface CourseService {

    /**
     * retrieve all courses
     * @return Set<CourseDto>
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    Set<CourseDto> getCourses() throws DataAccessServiceException;


    /**
     * Retrieves a course from the repository by its name and degree course name.
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @return CourseDto object representing the course with the given name and degree course name.
     * @throws IllegalArgumentException if the course name or degree course name is empty.
     * @throws ObjectNotFoundException if no course with the given name and degree course name exists.
     * @throws DataAccessServiceException if there's a database access issue
     */
    CourseDto getCourseByNameAndDegreeCourseName(String courseName, String degreeCourseName)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * retrieve all courses by professor
     * @param professor
     * @return List<CourseDto>
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    public List<CourseDto> getCoursesByProfessor(ProfessorDto professor)
        throws DataAccessServiceException;


    /**
     * Adds a new course to the repository.
     * @param dto the course data transfer object containing the course details
     * @return CourseDto object representing the new Course
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     * @throws ObjectNotFoundException if no professor with the given unique code exists
     *         or no degree course with the given name exists.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {ObjectAlreadyExistsException.class, ObjectNotFoundException.class, DataAccessServiceException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    CourseDto addNewCourse(CourseDto dto)
        throws ObjectAlreadyExistsException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * Updates an existing course using data from the provided CourseDto.
     * @param dto the DTO containing new course information
     * @return CourseDto object representing the updated Course
     * @throws IllegalArgumentException if old course name or degree course name are null or blank
     * or if the DTO contains invalid data
     * @throws ObjectNotFoundException if the course, professor, or degree course cannot be found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    CourseDto updateCourse(UpdateCourseDto dto)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * Deletes a course by its name and degree course name.
     * @param courseName the name of the course to be deleted
     * @param degreeCourseName the name of the degree course associated with the course
     * @return CourseDto object representing the deleted course
     * @throws IllegalArgumentException if course name or degree course name is null or blank
     * @throws ObjectNotFoundException if no course with the given name and degree course name exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    CourseDto deleteByNameAndDegreeCourse(String courseName, String degreeCourseName)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;

}
