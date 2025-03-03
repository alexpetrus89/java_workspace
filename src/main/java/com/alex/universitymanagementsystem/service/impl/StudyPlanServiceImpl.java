package com.alex.universitymanagementsystem.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.service.StudyPlanService;


@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    // constant
    private static final String EXCEPTION_COURSE_IDENTIFIER = "course";

    // instance variables
    private final StudyPlanRepository studyPlanRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudyPlanServiceImpl(
        StudyPlanRepository studyPlanRepository,
        StudentRepository studentRepository,
        CourseRepository courseRepository
    ) {
        this.studyPlanRepository = studyPlanRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves the ordering of the study plan
     * @param register the register of the student
     * @return the ordering of the study plan
     * @throws ObjectNotFoundException if the study plan does not exist
     */
    @Override
    public String getOrderingByRegister(Register register) throws ObjectNotFoundException {

        return studentRepository
            .findByRegister(register)
			.map(StudentMapper::mapToStudentDto)
			.orElseThrow(() -> new ObjectNotFoundException(register))
            .getStudyPlan()
            .getOrdering();
    }


    /**
     * Retrieves the courses of the study plan
     * @param register the register of the student
     * @return the courses of the study plan
     * @throws ObjectNotFoundException if the study plan does not exist
     */
    @Override
    public Set<CourseDto> getCoursesByRegister(@NonNull Register register) throws ObjectNotFoundException {
        return studentRepository
            .findByRegister(register)
			.map(StudentMapper::mapToStudentDto)
			.orElseThrow(() -> new ObjectNotFoundException(register))
            .getStudyPlan()
            .getCourses()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .collect(Collectors.toSet());
    }


    /**
     * Adds a course to the study plan
     * @param register student register
     * @param string name of the course to add
     * @throws ObjectAlreadyExistsException
     * @throws ObjectNotFoundException
     */
    @Override
    public void addCourse(@NonNull Register register, @NonNull String name) throws ObjectAlreadyExistsException {

        StudentDto student = studentRepository
            .findByRegister(register)
            .map(StudentMapper::mapToStudentDto)
            .orElseThrow(() -> new ObjectNotFoundException(register));

        CourseDto course = courseRepository.findByNameAndDegreeCourse(name, student.getDegreeCourse().getId())
            .map(CourseMapper::mapToCourseDto)
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_COURSE_IDENTIFIER));

        if(!student.getStudyPlan().addCourse(CourseMapper.mapToCourse(course)))
            throw new ObjectAlreadyExistsException(name, EXCEPTION_COURSE_IDENTIFIER);

        studyPlanRepository.saveAndFlush(student.getStudyPlan());
    }

    @Override
    public void removeCourse(@NonNull Register register, @NonNull String name) throws ObjectAlreadyExistsException {

        StudentDto student = studentRepository
            .findByRegister(register)
            .map(StudentMapper::mapToStudentDto)
            .orElseThrow(() -> new ObjectNotFoundException(register));

        CourseDto course = courseRepository.findByNameAndDegreeCourse(name, student.getDegreeCourse().getId())
            .map(CourseMapper::mapToCourseDto)
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_COURSE_IDENTIFIER));

        if(!student.getStudyPlan().removeCourse(CourseMapper.mapToCourse(course)))
            throw new ObjectNotFoundException(name, EXCEPTION_COURSE_IDENTIFIER);

        studyPlanRepository.saveAndFlush(student.getStudyPlan());
    }

}
