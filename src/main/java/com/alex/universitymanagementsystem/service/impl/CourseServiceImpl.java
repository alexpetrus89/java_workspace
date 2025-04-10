package com.alex.universitymanagementsystem.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.service.CourseService;

import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    // logger
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CourseServiceImpl.class);

    // constants
    private static final String DATA_ACCESS_ERROR = "An error occurred while accessing the database.";

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
    * Retrieves all courses from the repository.
    * @return List of CourseDto objects representing all courses.
    */
    @Override
    public Set<CourseDto> getCourses() {
        return courseRepository.findAll()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .collect(Collectors.toSet());
    }


    /**
     * Retrieves a course from the repository by its id.
     * @param CourseId id
     * @return CourseDto object representing the course with the given id.
     * @throws NullPointerException if the id is null
     * @throws IllegalArgumentException if the id is empty
     * @throws UnsupportedOperationException if the id is not unique
     */
    @Override
    public CourseDto getCourseById(@NonNull CourseId id)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        try {
            Course course = courseRepository.findById(id).orElse(null);
            return CourseMapper.mapToCourseDto(course);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Retrieves a course from the repository by its name and degree course name.
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @return CourseDto object representing the course with the given name and degree course name.
     * @throws NullPointerException if the course name or degree course name is null.
     * @throws IllegalArgumentException if the course name or degree course name is empty.
     * @throws UnsupportedOperationException if the course name or degree course name is not unique
     */
    @Override
    public CourseDto getCourseByNameAndDegreeCourseName(@NonNull String courseName, @NonNull String degreeCourseName) {

        try {
            // check parameters
            if(courseName.isBlank() || degreeCourseName.isBlank())
                throw new IllegalArgumentException("Course name or degree course name cannot be empty.");
            // retrieve degree course
            DegreeCourse degreeCourse = degreeCourseRepository.findByName(degreeCourseName);
            // find course
            return CourseMapper.mapToCourseDto(courseRepository.findByNameAndDegreeCourse(courseName, degreeCourse.getId()));
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }

    /**
     * Retrieves all courses from the repository by a given professor.
     * @param professor
     * @return List of CourseDto objects representing all courses by
     *         the given professor
     * @throws NullPointerException if the professor is null
     * @throws UnsupportedOperationException if the professor is not unique
     */
    @Override
    public List<CourseDto> getCoursesByProfessor(@NonNull Professor professor)
        throws  NullPointerException, UnsupportedOperationException
    {
        try {
            return courseRepository
                .findByProfessor(professor.getUniqueCode())
                .stream()
                .map(CourseMapper::mapToCourseDto)
                .toList();
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Adds a new course to the repository.
     * @param name the name of the course
     * @param type the type of the course
     * @param cfu the cfu of the course
     * @param uniqueCode the unique code of the professor
     * @param degreeCourseName the name of the degree course
     * @return Course object representing the newly added course.
     * @throws NullPointerException if any of the parameters is null.
     * @throws ObjectAlreadyExistsException if a course with the same name already exists.
     * @throws ObjectNotFoundException if no professor with the given unique code exists
     *         or no degree course with the given name exists.
     * @throws IllegalArgumentException if any of the parameters is invalid.
     * @throws UnsupportedOperationException if any of the parameters is not unique.
     */
    @Override
    @Transactional
    public CourseDto addNewCourse(
        @NonNull String name,
        @NonNull CourseType type,
        @NonNull Integer cfu,
        @NonNull String uniqueCode,
        @NonNull String degreeCourseName
    ) throws NullPointerException,
        ObjectAlreadyExistsException,
        ObjectNotFoundException,
        IllegalArgumentException,
        UnsupportedOperationException
    {

        // sanity checks
        if(name.isBlank() || uniqueCode.isBlank() || degreeCourseName.isBlank())
            throw new IllegalArgumentException("Course name, unique code or degree course name cannot be empty.");

        if(courseRepository.existsByName(name))
            throw new ObjectAlreadyExistsException(DomainType.COURSE);

        try {
            // retrieve all data
            Professor professor = professorRepository.findByUniqueCode(new UniqueCode(uniqueCode));
            DegreeCourse degreeCourse = degreeCourseRepository.findByName(degreeCourseName);

            // sanity checks
            if(professor == null)
                throw new ObjectNotFoundException(DomainType.PROFESSOR);
            if(degreeCourse == null)
                throw new ObjectNotFoundException(DomainType.DEGREE_COURSE);
            if(cfu < 0)
                throw new IllegalArgumentException("cfu must be a positive number");

            // create course
            Course course = new Course(name, type, cfu, professor, degreeCourse);
            // save
            courseRepository.saveAndFlush(course);
            return CourseMapper.mapToCourseDto(course);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Updates a course in the repository.
     * @param oldCourseName the name of the course to be updated
     * @param oldDegreeCourseName the old the degree course
     * @param newName the new name of the course
     * @param newDegreeCourseName the new the degree course
     * @param newType the new type of the course
     * @param newCfu the new cfu of the course
     * @param newUniqueCode the new unique code of the professor
     * @return Course object representing the updated course.
     * @throws NullPointerException if any of the parameters is null.
     * @throws ObjectNotFoundException if no course with the given name exists.
     * @throws IllegalArgumentException if any of the parameters is invalid.
     * @throws UnsupportedOperationException if any of the parameters is not unique.
     */
    @Override
    @Transactional
    public Course updateCourse(
        @NonNull String oldCourseName,
        @NonNull String oldDegreeCourseName,
        @NonNull String newCourseName,
        @NonNull String newDegreeCourseName,
        @NonNull CourseType newType,
        @NonNull Integer newCfu,
        @NonNull String newUniqueCode
    ) throws NullPointerException, ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException
    {
        if(oldCourseName.isBlank() || oldDegreeCourseName.isBlank() || newCourseName.isBlank() || newDegreeCourseName.isBlank() || newUniqueCode.isBlank())
            throw new IllegalArgumentException("Course name, unique code or degree course name cannot be empty.");

        try {
            // retrieve data
            DegreeCourse oldDegreeCourse = degreeCourseRepository.findByName(oldDegreeCourseName);
            Course updatableCourse = courseRepository.findByNameAndDegreeCourse(oldCourseName, oldDegreeCourse.getId());
            DegreeCourse newDegreeCourse = degreeCourseRepository.findByName(newDegreeCourseName);
            Professor professor = professorRepository.findByUniqueCode(new UniqueCode(newUniqueCode));

            // sanity checks
            if(updatableCourse == null)
                throw new ObjectNotFoundException(DomainType.COURSE);
            if(newDegreeCourse == null)
                throw new ObjectNotFoundException(DomainType.DEGREE_COURSE);
            if(professor == null)
                throw new ObjectNotFoundException(DomainType.PROFESSOR);
            if(newCourseName.isEmpty())
                throw new IllegalArgumentException("name must not be null or empty");
            if(newCfu < 0)
                throw new IllegalArgumentException("cfu must be a positive number");

            // update
            updatableCourse.setName(newCourseName);
            updatableCourse.setType(newType);
            updatableCourse.setCfu(newCfu);
            updatableCourse.setProfessor(professor);
            updatableCourse.setDegreeCourse(newDegreeCourse);

            // save
            courseRepository.save(updatableCourse);
            return updatableCourse;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR + " while updating course with name " + oldCourseName, e);
            return null;
        }
    }


    /**
     * Deletes a course from the repository by its id.
     * @param CourseId id
     * @throws NullPointerException if the id is null.
     * @throws ObjectNotFoundException if no course with the given id exists.
     * @throws IllegalArgumentException if the id is empty.
     * @throws UnsupportedOperationException if the id is not unique.
     */
    @Override
    @Transactional
    public void deleteCourse(@NonNull CourseId id)
        throws NullPointerException, ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException
    {
        if(id.toString().isBlank())
            throw new IllegalArgumentException("id cannot be null or empty");

        if(!courseRepository.existsById(id))
                throw new ObjectNotFoundException(DomainType.COURSE);
        try {
            courseRepository.deleteById(id);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR + " while deleting course with id " + id, e);
        }
    }

}
