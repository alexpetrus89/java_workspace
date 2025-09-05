package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.mapper.DegreeCourseMapper;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.service.DegreeCourseService;

import jakarta.persistence.PersistenceException;


@Service
public class DegreeCourseServiceImpl implements DegreeCourseService {

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final DegreeCourseRepository degreeCourseRepository;
    private final ServiceHelpers helpers;
    private final ServiceValidators validators;


    // autowired - dependency injection - constructor
    public DegreeCourseServiceImpl(
        DegreeCourseRepository degreeCourseRepository,
        ServiceHelpers helpers,
        ServiceValidators validators
    ) {
        this.degreeCourseRepository = degreeCourseRepository;
        this.helpers = helpers;
        this.validators = validators;
    }


    /**
     * Retrieves all degree courses from the repository and maps them to DTOs.
     * @return Set of DegreeCourseDto objects representing all degree courses.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public Set<DegreeCourseDto> getDegreeCourses() throws DataAccessServiceException {
        try {
            return degreeCourseRepository
                .findAll()
                .stream()
                .map(DegreeCourseMapper::toDto)
                .collect(Collectors.toSet());
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves a degree course from the repository by its name and
     * maps it to a DTO.
     * @param name name of the degree course
     * @return DegreeCourseDto object representing the degree course
     *         with the given name.
     * @throws IllegalArgumentException if the name is blank.
     * @throws ObjectNotFoundException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public DegreeCourseDto getDegreeCourseByName(String name)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        validators.validateDegreeCourseExists(name);

        try {
            return DegreeCourseMapper.toDto(helpers.fetchDegreeCourse(name));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves all courses of a given degree course from the repository and
     * maps them to DTOs.
     * @param name name of the degree course
     * @return List<CourseDto> objects representing all courses of the given
     *         degree course.
     * @throws IllegalArgumentException if the name is empty.
     * @throws ObjectNotFoundException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<CourseDto> getCourses(String name)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        validators.validateDegreeCourseExists(name);

        try {
            return helpers
                .fetchDegreeCourse(name)
                .getCourses()
                .stream()
                .map(CourseMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves all professors of a given degree course from the
     * repository and maps them to DTOs.
     * @param name name of the degree course
     * @return List<ProfessorDto> representing all professors of the given
     *         degree course
     * @throws IllegalArgumentException if the name is blank.
     * @throws ObjectNotFoundException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ProfessorDto> getProfessors(String name)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        validators.validateDegreeCourseExists(name);

        try {
            return helpers
                .fetchDegreeCourse(name)
                .getCourses()
                .stream()
                .filter(course -> course.getProfessor() != null)
                .map(Course::getProfessor)
                .distinct()
                .map(ProfessorMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves all students of a given degree course from the
     * repository and maps them to DTOs.
     * @param name name of the degree course.
     * @return List of StudentDto objects representing all students of the
     *         given degree course.
     * @throws IllegalArgumentException if the name is blank.
     * @throws ObjectNotFoundException if the degree course is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<StudentDto> getStudents(String name)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {

        validators.validateDegreeCourseExists(name);

        try {
            return helpers
            .fetchDegreeCourse(name)
            .getStudents()
            .stream()
            .map(StudentMapper::toDto)
            .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


}
