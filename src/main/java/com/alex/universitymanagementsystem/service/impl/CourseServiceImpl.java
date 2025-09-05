package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.UpdateCourseDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.service.CourseService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    // instance variables
    private final CourseRepository courseRepository;
    private final ServiceHelpers helpers;
    private final ServiceValidators validators;

    // constructor
    public CourseServiceImpl(
        CourseRepository courseRepository,
        ServiceHelpers helpers,
        ServiceValidators validators
    ) {
        this.courseRepository = courseRepository;
        this.helpers = helpers;
        this.validators = validators;
    }


    /**
     * retrieve all courses
     * @return Set<CourseDto>
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public Set<CourseDto> getCourses() throws DataAccessServiceException {
        try {
            return courseRepository
                .findAll()
                .stream()
                .map(CourseMapper::toDto)
                .collect(Collectors.toSet());
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching courses: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves a course from the repository by its name and degree course name.
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @return CourseDto object representing the course with the given name and degree course name.
     * @throws IllegalArgumentException if if either course name or degree course name is blank.
     * @throws ObjectNotFoundException if an object between course or degree course does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public CourseDto getCourseByNameAndDegreeCourseName(String courseName, String degreeCourseName)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
            // sanity checks
            validators.validateCourseExists(courseName, degreeCourseName);
            validators.validateDegreeCourseExists(degreeCourseName);

        try {
            // find course
            return CourseMapper.toDto(helpers.fetchCourse(courseName, degreeCourseName));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(
                "Database access error while retrieving course with name " + courseName + " and degree course name " + degreeCourseName + ": " + e.getMessage(), e
            );
        }
    }


    /**
     * Retrieves all courses from the repository by a given professor.
     * @param professor the professor whose courses are to be retrieved
     * @return List of CourseDto objects representing all courses by
     *         the given professor
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<CourseDto> getCoursesByProfessor(ProfessorDto professor)
        throws DataAccessServiceException
    {
        try {
            return helpers
                .fetchCourses(professor.getUniqueCode())
                .stream()
                .map(CourseMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(
                "Database access error while retrieving courses for professor " + professor.getFirstName() + " " + professor.getLastName() + ": " + e.getMessage(), e
            );
        }
    }


    /**
     * Adds a new course to the repository.
     * @param dto the course data transfer object containing the course details
     * @return CourseDto object representing the new Course
     * @throws ObjectAlreadyExistsException if a course with the same name and degree course already exists
     * @throws ObjectNotFoundException if no professor with the given unique code exists
     *         or no degree course with the given name exists.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {ObjectAlreadyExistsException.class, ObjectNotFoundException.class, IllegalArgumentException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CourseDto addNewCourse(CourseDto dto)
        throws ObjectAlreadyExistsException, ObjectNotFoundException, DataAccessServiceException
    {

        // Check for duplicate course name in the same degree course
        validators.validateCourseExists(dto.getName(), dto.getDegreeCourse().getName());
        try {
            Professor professor = helpers.fetchProfessor(dto.getProfessor().getUniqueCode());
            DegreeCourse degreeCourse = helpers.fetchDegreeCourse(dto.getDegreeCourse().getName());
            Course course = new Course(dto.getName(), dto.getType(), dto.getCfu(), professor, degreeCourse);
            // save
            courseRepository.saveAndFlush(course);
            return CourseMapper.toDto(course);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for course " + dto.getName() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Updates an existing course using data from the provided CourseDto.
     * @param dto the DTO containing new course information
     * @return CourseDto object representing the updated Course
     * @throws ObjectNotFoundException if the course, professor, or degree course cannot be found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CourseDto updateCourse(UpdateCourseDto dto)
        throws ObjectNotFoundException, DataAccessServiceException
    {

        try {
            Course course = helpers.fetchCourse(dto.getNewName(), dto.getNewDegreeCourseName());
            DegreeCourse newDegreeCourse = helpers.fetchDegreeCourse(dto.getNewDegreeCourseName());
            Professor professor = helpers.fetchProfessor(dto.getUniqueCode());

            // Update course fields
            course.setName(dto.getNewName());
            course.setType(dto.getType());
            course.setCfu(dto.getCfu());
            course.setProfessor(professor);
            course.setDegreeCourse(newDegreeCourse);

            // Save changes
            courseRepository.saveAndFlush(course);
            return CourseMapper.toDto(course);

        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for course " + dto.getOldName() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Deletes a course by its name and degree course name.
     * @param courseName the name of the course to be deleted
     * @param degreeCourseName the name of the degree course associated with the course
     * @return CourseDto object representing the deleted course
     * @throws IllegalArgumentException if course name or degree course name is null or blank
     * @throws ObjectNotFoundException if no course with the given name and degree course name exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CourseDto deleteByNameAndDegreeCourse(String courseName, String degreeCourseName)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {

        validators.validateCourseExists(courseName, degreeCourseName);
        validators.validateDegreeCourseExists(degreeCourseName);

        try {
            Course course = helpers.fetchCourse(courseName, degreeCourseName);
            // delete
            courseRepository.delete(course);
            return CourseMapper.toDto(course);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for course " + courseName + ": " + e.getMessage(), e);
        }
    }


}
