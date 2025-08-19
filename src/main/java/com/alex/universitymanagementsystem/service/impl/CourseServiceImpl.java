package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.service.CourseService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    // instance variables
    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final DegreeCourseRepository degreeCourseRepository;

    // constructor
    public CourseServiceImpl(
        CourseRepository courseRepository,
        ProfessorRepository professorRepository,
        DegreeCourseRepository degreeCourseRepository
    ) {
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
        this.degreeCourseRepository = degreeCourseRepository;
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
     * @throws IllegalArgumentException if the course name or degree course name is empty.
     * @throws ObjectNotFoundException if no course with the given name and degree course name exists.
     * @throws DataAccessServiceException if there's a database access issue
     */
    @Override
    public CourseDto getCourseByNameAndDegreeCourseName(String courseName, String degreeCourseName)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {

        try {
            // sanity checks
            if(courseName.isBlank() || degreeCourseName.isBlank())
                throw new IllegalArgumentException("Course name or degree course name cannot be empty.");

            if(!degreeCourseRepository.existsByName(degreeCourseName.toUpperCase()))
                throw new ObjectNotFoundException(DomainType.DEGREE_COURSE);

            // find course
            return courseRepository
                .findByNameAndDegreeCourseName(courseName, degreeCourseName)
                .map(CourseMapper::toDto)
                .orElse(null);
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
            return courseRepository
                .findByProfessor(new UniqueCode(professor.getUniqueCode()))
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
     * @param course the course data transfer object containing the course details
     * @return Course
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     * @throws ObjectNotFoundException if no professor with the given unique code exists
     *         or no degree course with the given name exists.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {ObjectAlreadyExistsException.class, ObjectNotFoundException.class, IllegalArgumentException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CourseDto addNewCourse(CourseDto courseDto)
        throws ObjectAlreadyExistsException, ObjectNotFoundException, DataAccessServiceException
    {
        try {

            // Check for duplicate course name in the same degree course
            if(courseRepository.existsByNameAndDegreeCourse(courseDto.getName(), courseDto.getDegreeCourse().toString()))
                throw new ObjectAlreadyExistsException(DomainType.COURSE);

            // Validate professor existence
            Professor professor = professorRepository
                .findByUniqueCode(new UniqueCode(courseDto.getProfessor().getUniqueCode()))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.PROFESSOR));

            // Validate degree course existence
            DegreeCourse degreeCourse = degreeCourseRepository
                .findByName(courseDto.getDegreeCourse().getName())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE));

            // create course
            Course course = new Course(courseDto.getName(), courseDto.getType(), courseDto.getCfu(), professor, degreeCourse);
            // save
            courseRepository.saveAndFlush(course);
            return CourseMapper.toDto(course);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for course " + courseDto.getName() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Updates an existing course using data from the provided CourseDto.
     * @param oldCourseName the current name of the course to be updated
     * @param oldDegreeCourseName the current degree course name associated with the course
     * @param updatedCourseDto the DTO containing new course information
     * @return the updated Course
     * @throws IllegalArgumentException if old course name or degree course name are null or blank
     * or if the DTO contains invalid data
     * @throws ObjectNotFoundException if the course, professor, or degree course cannot be found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CourseDto updateCourse(String oldCourseName, String oldDegreeCourseName, CourseDto updatedCourseDto)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {

        if (oldCourseName == null || oldCourseName.isBlank() ||
            oldDegreeCourseName == null || oldDegreeCourseName.isBlank())
            throw new IllegalArgumentException("Old course name and degree course name must not be null or blank");

        try {
            // Retrieve existing course
            Course course = courseRepository
                .findByNameAndDegreeCourseName(oldCourseName, oldDegreeCourseName)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

            // Validate new degree course and professor
            DegreeCourse newDegreeCourse = degreeCourseRepository
                .findByName(updatedCourseDto.getDegreeCourse().getName())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE));

            Professor professor = professorRepository
                .findByUniqueCode(new UniqueCode(updatedCourseDto.getProfessor().getUniqueCode()))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.PROFESSOR));

            // Update course fields
            course.setName(updatedCourseDto.getName());
            course.setType(updatedCourseDto.getType());
            course.setCfu(updatedCourseDto.getCfu());
            course.setProfessor(professor);
            course.setDegreeCourse(newDegreeCourse);

            // Save changes
            courseRepository.saveAndFlush(course);
            return CourseMapper.toDto(course);

        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for course " + updatedCourseDto.getName() + ": " + e.getMessage(), e);
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

        if (courseName == null || courseName.isBlank() ||
            degreeCourseName == null || degreeCourseName.isBlank())
            throw new IllegalArgumentException("Course name and degree course name must not be null or blank");

        try {
            Course course = courseRepository
                .findByNameAndDegreeCourseName(courseName, degreeCourseName)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

            // delete
            courseRepository.delete(course);
            return CourseMapper.toDto(course);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for course " + courseName + ": " + e.getMessage(), e);
        }
    }


}
