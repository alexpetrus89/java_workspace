package com.alex.studentmanagementsystem.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.CourseMapper;
import com.alex.studentmanagementsystem.repository.CourseRepository;
import com.alex.studentmanagementsystem.service.CourseService;

import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

    // constant
    private static final String EXCEPTION_COURSE_IDENTIFIER = "course";

    // instance variables
    private final CourseRepository courseRepository;

    // constructor
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
    *
    * @param courseDto the course data transfer object containing the details of the course to be added.
    * @throws ObjectAlreadyExistsException if a course with the same name already exists in the repository.
    */
    @Override
    @Transactional
    public void addNewCourse(CourseDto courseDto) {

        if(courseRepository.existsByName(courseDto.getName()))
            throw new ObjectAlreadyExistsException(courseDto.getName(), EXCEPTION_COURSE_IDENTIFIER);

        courseRepository.save(CourseMapper.mapToCourse(courseDto));
    }

    /**
     * Updates an existing course in the repository.
     *
     * @param courseDto the course data transfer object containing the new details of the course to be updated.
     * @throws ObjectNotFoundException if no course with the given name exists in the repository.
     * @throws NullPointerException if the courseDto is null.
     * @throws IllegalArgumentException if the given course name is null or empty.
     */
    @Override
    @Transactional
    public void updateCourse(CourseDto newCourseDto) {

        // check if exist
        Course updatableCourse = courseRepository
            .findByName(newCourseDto.getName())
            .orElseThrow(() -> new ObjectNotFoundException(newCourseDto.getName(), EXCEPTION_COURSE_IDENTIFIER));

        // new name, category and cfu
        String newName = newCourseDto.getName();
        String newCategory = newCourseDto.getCategory();
        Integer newCfu = newCourseDto.getCfu();

        // update
        if(newName != null && !newName.isEmpty())
            updatableCourse.setName(newName);
        if(newName != null &&!newCategory.isEmpty())
            updatableCourse.setCategory(newCategory);
        if(newCfu != null && newCfu >= 0)
            updatableCourse.setCfu(newCfu);

        // save
        courseRepository.save(updatableCourse);
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
