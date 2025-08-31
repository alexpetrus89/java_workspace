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
import com.alex.universitymanagementsystem.dto.UpdateCourseDto;
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
     * @throws IllegalArgumentException if if either course name or degree course name is blank.
     * @throws ObjectNotFoundException if an object between course or degree course does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
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
                .findByNameAndDegreeCourseName(courseName, degreeCourseName.toUpperCase())
                .map(CourseMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));
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
        try {

            // Check for duplicate course name in the same degree course
            if(courseRepository.existsByNameAndDegreeCourse(dto.getName(), dto.getDegreeCourse().toString()))
                throw new ObjectAlreadyExistsException(DomainType.COURSE);

            // Validate professor existence
            Professor professor = professorRepository
                .findByUniqueCode(new UniqueCode(dto.getProfessor().getUniqueCode()))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.PROFESSOR));

            // Validate degree course existence
            DegreeCourse degreeCourse = degreeCourseRepository
                .findByName(dto.getDegreeCourse().getName())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE));

            // create course
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
            // Retrieve existing course
            Course course = courseRepository
                .findByNameAndDegreeCourseName(dto.getNewName(), dto.getNewDegreeCourseName())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

            // Validate new degree course and professor
            DegreeCourse newDegreeCourse = degreeCourseRepository
                .findByName(dto.getNewDegreeCourseName())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE));

            Professor professor = professorRepository
                .findByUniqueCode(new UniqueCode(dto.getUniqueCode()))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.PROFESSOR));

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
