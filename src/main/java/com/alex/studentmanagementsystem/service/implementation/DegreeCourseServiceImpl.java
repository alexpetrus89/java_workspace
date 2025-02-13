package com.alex.studentmanagementsystem.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.dto.DegreeCourseDto;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.DegreeCourseMapper;
import com.alex.studentmanagementsystem.mapper.StudentMapper;
import com.alex.studentmanagementsystem.repository.DegreeCourseRepository;
import com.alex.studentmanagementsystem.service.DegreeCourseService;

@Service
public class DegreeCourseServiceImpl implements DegreeCourseService {

    // constants
    private static final String EXCEPTION_DEGREE_COURSE_IDENTIFIER = "degree_course";

    // instance variables
    private final DegreeCourseRepository degreeCourseRepository;

    // autowired - dependency injection - constructor
    public DegreeCourseServiceImpl(
        DegreeCourseRepository degreeCourseRepository
    ) {
        this.degreeCourseRepository = degreeCourseRepository;
    }

    /**
     * Retrieves all degree courses from the repository and maps them to DTOs.
     *
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
     * Retrieves a degree course from the repository by its name and maps it
     * to a DTO.
     *
     * @param String name
     * @return DegreeCourseDto object representing the degree course with the
     *         given name.
     * @throws ObjectNotFoundException if no degree course with the given name
     *                                 exists.
     * @throws NullPointerException if the name is null.
     */
    @Override
    public DegreeCourseDto getDegreeCourseByName(String name)
        throws ObjectNotFoundException
    {
        return  degreeCourseRepository
            .findByName(name)
            .map(DegreeCourseMapper::mapToDegreeCourseDto)
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_DEGREE_COURSE_IDENTIFIER));
    }

    /**
     * Retrieves all students of a given degree course from the repository and
     * maps them to DTOs.
     *
     * @param String name the name of the degree course.
     * @return List of StudentDto objects representing all students of the
     *         given degree course.
     * @throws ObjectNotFoundException if no degree course with the given name
     *                                 exists.
     * @throws NullPointerException if the name is null.
     */
    @Override
    public List<StudentDto> getStudents(String name)
        throws ObjectNotFoundException
    {
        return degreeCourseRepository
            .findByName(name)
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_DEGREE_COURSE_IDENTIFIER))
            .getStudents()
            .stream()
            .map(StudentMapper::mapToStudentDto)
            .toList();
    }

}
