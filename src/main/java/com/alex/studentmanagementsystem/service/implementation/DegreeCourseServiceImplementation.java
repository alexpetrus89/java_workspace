package com.alex.studentmanagementsystem.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.dto.DegreeCourseDto;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.CourseMapper;
import com.alex.studentmanagementsystem.mapper.DegreeCourseMapper;
import com.alex.studentmanagementsystem.mapper.ProfessorMapper;
import com.alex.studentmanagementsystem.mapper.StudentMapper;
import com.alex.studentmanagementsystem.repository.DegreeCourseRepository;
import com.alex.studentmanagementsystem.service.DegreeCourseService;

@Service
public class DegreeCourseServiceImplementation
    implements DegreeCourseService
{
    // constants
    private static final String EXCEPTION_DEGREE_COURSE_IDENTIFIER = "degree_course";

    // instance variables
    private final DegreeCourseRepository degreeCourseRepository;

    // autowired - dependency injection - constructor
    public DegreeCourseServiceImplementation(
        DegreeCourseRepository degreeCourseRepository
    ) {
        this.degreeCourseRepository = degreeCourseRepository;
    }

    /**
     * @return List<DegreeCourseDto>
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
     * @param name
     * @return DegreeCourseDto
     * @throws ObjectNotFoundException
     */
    @Override
    public DegreeCourseDto getDegreeCourseByName(String name)
        throws ObjectNotFoundException
    {
        return DegreeCourseMapper.mapToDegreeCourseDto(
            degreeCourseRepository
                .findByName(name)
                // throws ObjectNotFoundException
                .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_DEGREE_COURSE_IDENTIFIER))
        );
    }

    /**
     * @param name
     * @return List<CourseDto>
     * @throws ObjectNotFoundException
     */
    @Override
    public List<CourseDto> getCoursesOfDegreeCourse(String name)
        throws ObjectNotFoundException
    {
        return degreeCourseRepository
            .findByName(name)
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_DEGREE_COURSE_IDENTIFIER))
            .getCourses()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .toList();
    }

    /**
     * @param name
     * @return List<StudentDto>
     * @throws ObjectNotFoundException
     */
    @Override
    public List<StudentDto> getStudentsOfDegreeCourse(String name)
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

    /**
     * @param name
     * @return List<ProfessorDto>
     * @throws ObjectNotFoundException
     */
    @Override
    public List<ProfessorDto> getProfessorsOfDegreeCourse(String name)
        throws ObjectNotFoundException
    {
        List<CourseDto> courses = getCoursesOfDegreeCourse(name);
        List<ProfessorDto> professors = new ArrayList<>();

        courses.forEach(courseDto -> professors.add(ProfessorMapper.mapToProfessorDto(courseDto.getProfessor())));

        return professors;
    }


}
