package com.alex.universitymanagementsystem.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.mapper.DegreeCourseMapper;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.service.DegreeCourseService;


@Service
public class DegreeCourseServiceImpl implements DegreeCourseService {

    // logger
	private static final Logger logger = LoggerFactory.getLogger(DegreeCourseServiceImpl.class);

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";
    private static final String DEGREE_COURSE_BLANK_ERROR = "Degree course name cannot be empty";

    // instance variables
    private final DegreeCourseRepository degreeCourseRepository;

    // autowired - dependency injection - constructor
    public DegreeCourseServiceImpl(DegreeCourseRepository degreeCourseRepository) {
        this.degreeCourseRepository = degreeCourseRepository;
    }


    /**
     * Retrieves all degree courses from the repository and maps them to DTOs.
     * @return Set of DegreeCourseDto objects representing all degree courses.
     */
    @Override
    public Set<DegreeCourseDto> getDegreeCourses() {
        return degreeCourseRepository
            .findAll()
            .stream()
            .map(DegreeCourseMapper::mapToDegreeCourseDto)
            .collect(Collectors.toSet());
    }


    /**
     * Retrieves a degree course from the repository by its name and
     * maps it to a DTO.
     * @param name the name of the degree course
     * @return DegreeCourseDto object representing the degree course
     *         with the given name.
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Override
    public DegreeCourse getDegreeCourseByName(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        if(name.isBlank())
            throw new IllegalArgumentException(DEGREE_COURSE_BLANK_ERROR);

        try {
            return degreeCourseRepository.findByName(name.toUpperCase());
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Retrieves all courses of a given degree course from the repository and
     * maps them to DTOs.
     * @param name the name of the degree course
     * @return List<CourseDto> objects representing all courses of the given
     *         degree course.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Override
    public List<CourseDto> getCourses(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        if(name.isBlank())
            throw new IllegalArgumentException(DEGREE_COURSE_BLANK_ERROR);

        try {
            return degreeCourseRepository
                .findByName(name)
                .getCourses()
                .stream()
                .map(CourseMapper::mapToCourseDto)
                .toList();
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Retrieves all professors of a given degree course from the
     * repository and maps them to DTOs.
     * @param name the name of the degree course
     * @return List<ProfessorDto> representing all professors of the given
     *         degree course
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is blank.
     * @throws UnsupportedOperationException if the name is not unique.
     */
    @Override
    public List<ProfessorDto> getProfessors(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        if(name.isBlank())
            throw new IllegalArgumentException(DEGREE_COURSE_BLANK_ERROR);

        try {
            return degreeCourseRepository
                .findByName(name)
                .getCourses()
                .stream()
                .map(Course::getProfessor)
                .distinct()
                .map(ProfessorMapper::mapToProfessorDto)
                .toList();
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Retrieves all students of a given degree course from the
     * repository and maps them to DTOs.
     * @param name the name of the degree course.
     * @return List of StudentDto objects representing all students of the
     *         given degree course.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is blank.
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Override
    public List<StudentDto> getStudents(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {

        if(name.isBlank())
            throw new IllegalArgumentException(DEGREE_COURSE_BLANK_ERROR);

        try {
            return degreeCourseRepository
            .findByName(name)
            .getStudents()
            .stream()
            .map(StudentMapper::mapToStudentDto)
            .toList();
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


}
