package com.alex.universitymanagementsystem.service.impl;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.mapper.DegreeCourseMapper;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.service.DegreeCourseService;


@Service
public class DegreeCourseServiceImpl implements DegreeCourseService {

    // instance variables
    private final DegreeCourseRepository degreeCourseRepository;

    // autowired - dependency injection - constructor
    public DegreeCourseServiceImpl(DegreeCourseRepository degreeCourseRepository) {
        this.degreeCourseRepository = degreeCourseRepository;
    }


    /**
     * Retrieves all degree courses from the repository and maps them to DTOs.
     * @return List of DegreeCourseDto objects representing all degree courses.
     */
    @Override
    public List<DegreeCourseDto> getDegreeCourses() {
        return degreeCourseRepository
            .findAll()
            .stream()
            .map(DegreeCourseMapper::mapToDegreeCourseDto)
            .toList();
    }


    /**
     * Retrieves a degree course from the repository by its name and
     * maps it to a DTO.
     * @param name the name of the degree course
     * @return DegreeCourseDto object representing the degree course
     *         with the given name.
     * @throws ObjectNotFoundException if no degree course with the given
     *                                 name exists.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Override
    public DegreeCourse getDegreeCourseByName(@NonNull String name)
        throws ObjectNotFoundException
    {
        return degreeCourseRepository.findByName(name.toUpperCase());
    }


    /**
     * Retrieves all courses of a given degree course from the repository and
     * maps them to DTOs.
     * @param name the name of the degree course
     * @return List<CourseDto> objects representing all courses of the given
     *         degree course.
     * @throws ObjectNotFoundException if no degree course with the given name
     *                                 exists.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Override
    public List<CourseDto> getCourses(@NonNull String name)
        throws ObjectNotFoundException
    {
        return degreeCourseRepository
            .findByName(name)
            .getCourses()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .toList();
    }


    /**
     * Retrieves all professors of a given degree course from the
     * repository and maps them to DTOs.
     * @param name the name of the degree course
     * @return List<ProfessorDto> representing all professors of the given
     *         degree course
     * @throws ObjectNotFoundException if no degree course with the given name
     *                                 exists.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique.
     */
    @Override
    public List<ProfessorDto> getProfessors(@NonNull String name)
        throws ObjectNotFoundException
    {
        return degreeCourseRepository
            .findByName(name)
            .getCourses()
            .stream()
            .map(Course::getProfessor)
            .distinct()
            .map(ProfessorMapper::mapToProfessorDto)
            .toList();
    }


    /**
     * Retrieves all students of a given degree course from the
     * repository and maps them to DTOs.
     * @param name the name of the degree course.
     * @return List of StudentDto objects representing all students of the
     *         given degree course.
     * @throws ObjectNotFoundException if no degree course with the given
     *                                 name exists.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Override
    public List<StudentDto> getStudents(@NonNull String name)
        throws ObjectNotFoundException
    {
        return degreeCourseRepository
            .findByName(name)
            .getStudents()
            .stream()
            .map(StudentMapper::mapToStudentDto)
            .toList();
    }


}
