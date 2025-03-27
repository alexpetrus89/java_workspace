package com.alex.universitymanagementsystem.service.impl;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.service.CourseService;
import com.alex.universitymanagementsystem.utils.CourseType;

import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    // constant
    private static final String EXCEPTION_COURSE_IDENTIFIER = "course";

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
    public List<CourseDto> getCourses() {
        return courseRepository.findAll()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .toList();
    }


    /**
     * Retrieves a course from the repository by its id.
     * @param CourseId id
     * @return CourseDto object representing the course with the given id.
     * @throws ObjectNotFoundException if no course with the given id exists.
     * @throws NullPointerException if the id is null.
     */
    @Override
    public CourseDto getCourseById(@NonNull CourseId id)
        throws ObjectNotFoundException
    {
        Course course = courseRepository
            .findById(id)
			.orElseThrow(() -> new ObjectNotFoundException(id.toString(), EXCEPTION_COURSE_IDENTIFIER));

		return CourseMapper.mapToCourseDto(course);
    }


    /**
     * Retrieves a course from the repository by its name and degree course name.
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @return CourseDto object representing the course with the given name and degree course name.
     * @throws ObjectNotFoundException if no course with the given name and degree course name exists.
     * @throws NullPointerException if the course name or degree course name is null.
     * @throws IllegalArgumentException if the course name or degree course name is empty.
     * @throws UnsupportedOperationException if the course name or degree course name is not unique
     */
    @Override
    public CourseDto getCourseByNameAndDegreeCourseName(@NonNull String courseName, @NonNull String degreeCourseName) {
        // retrieve degree course
        DegreeCourse degreeCourse = degreeCourseRepository.findByName(degreeCourseName);
        // find course
        return CourseMapper.mapToCourseDto(courseRepository.findByNameAndDegreeCourse(courseName, degreeCourse.getId()));
    }

    /**
     * Retrieves all courses from the repository by a given professor.
     * @param professor
     * @return List of CourseDto objects representing all courses by
     *         the given professor
     * @throws NullPointerException if the professor is null
     * @throws IllegalArgumentException if the professor is not found
     * @throws UnsupportedOperationException if the professor is not unique
     */
    @Override
    public List<CourseDto> getCoursesByProfessor(@NonNull Professor professor) {
        return courseRepository
            .findByProfessor(professor.getUniqueCode())
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .toList();
    }


    /**
     * Adds a new course to the repository.
     * @param name the name of the course
     * @param type the type of the course
     * @param cfu the cfu of the course
     * @param uniqueCode the unique code of the professor
     * @param degreeCourseName the name of the degree course
     * @return Course object representing the newly added course.
     * @throws ObjectAlreadyExistsException if a course with the same name already exists.
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if any of the parameters is invalid.
     * @throws UnsupportedOperationException if any of the parameters is not unique.
     */
    @Override
    @NonNull
    @Transactional
    public Course addNewCourse(String name, CourseType type, Integer cfu, String uniqueCode, String degreeCourseName) {

        Professor professor = professorRepository.findByUniqueCode(new UniqueCode(uniqueCode));

        DegreeCourse degreeCourse = degreeCourseRepository.findByName(degreeCourseName);

        // sanity check
        if(cfu == null || cfu < 0)
            throw new IllegalArgumentException("cfu must be a positive number");
        // create course
        Course course = new Course(name, type, cfu, professor, degreeCourse);
        // save
        courseRepository.saveAndFlush(course);

        return course;
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
     * @throws ObjectNotFoundException if no course with the given name exists.
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if any of the parameters is invalid.
     * @throws UnsupportedOperationException if any of the parameters is not unique.
     */
    @Override
    @NonNull
    @Transactional
    public Course updateCourse(
        String oldCourseName,
        String oldDegreeCourseName,
        String newCourseName,
        String newDegreeCourseName,
        CourseType newType,
        Integer newCfu,
        String newUniqueCode
    ) {

        // check if exist
        DegreeCourse oldDegreeCourse = degreeCourseRepository.findByName(oldDegreeCourseName);

        Course updatableCourse = courseRepository.findByNameAndDegreeCourse(oldCourseName, oldDegreeCourse.getId());

        Professor professor = professorRepository.findByUniqueCode(new UniqueCode(newUniqueCode));

        DegreeCourse newDegreeCourse = degreeCourseRepository.findByName(newDegreeCourseName);

        // sanity check
        if(newCourseName == null || newCourseName.isEmpty())
            throw new IllegalArgumentException("name must not be null or empty");

        if(newCfu == null || newCfu < 0)
            throw new IllegalArgumentException("cfu must be a positive number");

        updatableCourse.setName(newCourseName);
        updatableCourse.setType(newType);
        updatableCourse.setCfu(newCfu);
        updatableCourse.setProfessor(professor);
        updatableCourse.setDegreeCourse(newDegreeCourse);

        // save
        courseRepository.save(updatableCourse);

        return updatableCourse;
    }


    /**
     * Deletes a course from the repository by its id.
     * @param CourseId id
     * @throws ObjectNotFoundException if no course with the given id exists.
     * @throws NullPointerException if the id is null.
     * @throws IllegalArgumentException if the id is empty.
     * @throws UnsupportedOperationException if the id is not unique.
     */
    @Override
    @Transactional
    public void deleteCourse(@NonNull CourseId id)
        throws ObjectNotFoundException
    {
        if(!courseRepository.existsById(id))
            throw new ObjectNotFoundException(id.toString(), EXCEPTION_COURSE_IDENTIFIER);

        courseRepository.deleteById(id);
    }

}
