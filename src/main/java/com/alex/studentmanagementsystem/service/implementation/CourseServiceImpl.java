package com.alex.studentmanagementsystem.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.domain.Professor;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.CourseMapper;
import com.alex.studentmanagementsystem.repository.CourseRepository;
import com.alex.studentmanagementsystem.repository.DegreeCourseRepository;
import com.alex.studentmanagementsystem.repository.ProfessorRepository;
import com.alex.studentmanagementsystem.service.CourseService;
import com.alex.studentmanagementsystem.utility.CourseType;

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
    *
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
     *
     * @param CourseId id
     * @return CourseDto object representing the course with the given id.
     * @throws ObjectNotFoundException if no course with the given id exists.
     * @throws NullPointerException if the id is null.
     */
    @Override
    public CourseDto getCourseById(CourseId id)
        throws ObjectNotFoundException
    {
        Course course = courseRepository
            .findById(id)
			.orElseThrow(() -> new ObjectNotFoundException(id.toString(), EXCEPTION_COURSE_IDENTIFIER));

		return CourseMapper.mapToCourseDto(course);
    }


    /**
     * Retrieves a course from the repository by its name.
     *
     * @param String name
     * @return CourseDto object representing the course with the given name.
     * @throws ObjectNotFoundException if no course with the given name exists.
     * @throws NullPointerException if the name is null.
     */
    @Override
    public CourseDto getCourseByName(String name)
        throws ObjectNotFoundException
    {
        return courseRepository
            .findByName(name)
            .map(CourseMapper::mapToCourseDto)
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_COURSE_IDENTIFIER));
    }


    /**
     * Adds a new course to the repository.
     * @param name
     * @param type
     * @param cfu
     * @param uniqueCode
     * @param degreeCourseName
     * @return Course object representing the newly added course.
     * @throws ObjectAlreadyExistsException if a course with the same name already exists.
     * @throws NullPointerException if any of the parameters is null.
     */
    @Override
    @Transactional
    public Course addNewCourse(
        String name,
        CourseType type,
        Integer cfu,
        String uniqueCode,
        String degreeCourseName
    ) {

        Professor professor = professorRepository
            .findByUniqueCode(new UniqueCode(uniqueCode))
            .orElseThrow(() -> new ObjectNotFoundException(uniqueCode, "professor"));

        DegreeCourse degreeCourse = degreeCourseRepository
            .findByName(degreeCourseName)
            .orElseThrow(() -> new ObjectNotFoundException(degreeCourseName, "degree course"));

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
     * @param oldName
     * @param newName
     * @param newType
     * @param newCfu
     * @param newUniqueCode
     * @param newDegreeCourseName
     * @return Course object representing the updated course.
     * @throws ObjectNotFoundException if no course with the given name exists.
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if any of the parameters is invalid.
     */
    @Override
    @Transactional
    public Course updateCourse(
        String oldName,
        String newName,
        CourseType newType,
        Integer newCfu,
        String newUniqueCode,
        String newDegreeCourseName
    ) {

        // check if exist
        Course updatableCourse = courseRepository
            .findByName(oldName)
            .orElseThrow(() -> new ObjectNotFoundException(oldName, EXCEPTION_COURSE_IDENTIFIER));

        Professor professor = professorRepository
            .findByUniqueCode(new UniqueCode(newUniqueCode))
            .orElseThrow(() -> new ObjectNotFoundException(newUniqueCode, "professor"));

        DegreeCourse degreeCourse = degreeCourseRepository
            .findByName(newDegreeCourseName)
            .orElseThrow(() -> new ObjectNotFoundException(newDegreeCourseName, "degree course"));

        // sanity check
        if(newName == null || newName.isEmpty())
            throw new IllegalArgumentException("name must not be null or empty");

        if(newCfu == null || newCfu < 0)
            throw new IllegalArgumentException("cfu must be a positive number");

        updatableCourse.setName(newName);
        updatableCourse.setType(newType);
        updatableCourse.setCfu(newCfu);
        updatableCourse.setProfessor(professor);
        updatableCourse.setDegreeCourse(degreeCourse);

        // save
        courseRepository.save(updatableCourse);

        return updatableCourse;
    }


    /**
     * Deletes a course from the repository by its id.
     *
     * @param CourseId id
     * @throws ObjectNotFoundException if no course with the given id exists.
     * @throws NullPointerException if the id is null.
     */
    @Override
    @Transactional
    public void deleteCourse(CourseId id)
        throws ObjectNotFoundException
    {
        if(!courseRepository.existsById(id))
            throw new ObjectNotFoundException(id.toString(), EXCEPTION_COURSE_IDENTIFIER);

        courseRepository.deleteById(id);
    }

}
