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
public class CourseServiceImplementation
    implements CourseService
{

    // constant
    private static final String EXCEPTION_COURSE_IDENTIFIER = "course";

    // instance variables
    private final CourseRepository courseRepository;

    // constructor
    public CourseServiceImplementation(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * @return List<CourseDto>
     */
    @Override
    public List<CourseDto> getCourses() {
        return courseRepository.findAll()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .toList();
    }


    /**
     * @param id
     * @return CourseDto
     * @throws ObjectNotFoundException
     */
    @Override
    public CourseDto getCourseById(CourseId id)
        throws ObjectNotFoundException
    {
        Course course = courseRepository
            .findById(id)
			// throw exception
			.orElseThrow(() -> new ObjectNotFoundException(id.toString(), EXCEPTION_COURSE_IDENTIFIER));

		return CourseMapper.mapToCourseDto(course);
    }

    /**
     * @param name
     * @return CourseDto
     * @throws ObjectNotFoundException
     */
    @Override
    public CourseDto getCourseByName(String name)
        throws ObjectNotFoundException
    {
        Course course = courseRepository
            .findByName(name)
            // throw exception
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_COURSE_IDENTIFIER));
        return CourseMapper.mapToCourseDto(course);
    }

    /**
     * @param courseDto
     * @throws ObjectAlreadyExistsException
     * @throws ObjectNotFoundException
     */
    @Override
    @Transactional
    public void addNewCourse(CourseDto courseDto) {

        if(courseRepository.existsByName(courseDto.getName()))
            // throw exception
            throw new ObjectAlreadyExistsException(courseDto.getName(), EXCEPTION_COURSE_IDENTIFIER);

        courseRepository.save(CourseMapper.mapToCourse(courseDto));
    }

    /**
     * @param newCourseDto
     * @throws ObjectNotFoundException
     */
    @Override
    @Transactional
    public void updateCourse(CourseDto newCourseDto) {

        // check if exist
        Course updatableCourse = courseRepository
            .findByName(newCourseDto.getName())
            // else throw exception
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
     * @param id
     * @throws ObjectNotFoundException
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
