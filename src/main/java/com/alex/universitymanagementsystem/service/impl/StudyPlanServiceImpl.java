package com.alex.universitymanagementsystem.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.service.StudyPlanService;


@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    // constants
    private static final String EXCEPTION_COURSE_IDENTIFIER = "course";

    // instance variables
    private final StudyPlanRepository studyPlanRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;

    public StudyPlanServiceImpl(
        StudyPlanRepository studyPlanRepository,
        StudentRepository studentRepository,
        DegreeCourseRepository degreeCourseRepository,
        CourseRepository courseRepository
    ) {
        this.studyPlanRepository = studyPlanRepository;
        this.studentRepository = studentRepository;
        this.degreeCourseRepository = degreeCourseRepository;
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
    public Set<CourseDto> getCoursesByRegister(@NonNull Register register)
        throws ObjectNotFoundException {
        return studentRepository
            .findByRegister(register)
            .getStudyPlan()
            .getCourses()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .collect(Collectors.toSet());
    }


    /**
     * Adds a course to the study plan
     * @param register student register
     * @param string name of degree course of new course
     * @param string name of degree course of old course
     * @param string name of the course to add
     * @param string name of the course to remove
     * @throws ObjectAlreadyExistsException
     * @throws ObjectNotFoundException
     */
    @Override
    public void changeCourse(
        @NonNull Register register,
        @NonNull String degreeCourseOfNewCourse,
        @NonNull String degreeCourseOfOldCourse,
        @NonNull String courseToAddName,
        @NonNull String courseToRemoveName
    ) throws ObjectAlreadyExistsException, ObjectNotFoundException {

        StudentDto student = StudentMapper.mapToStudentDto(studentRepository.findByRegister(register));

        DegreeCourse degreeCourseNew = degreeCourseRepository.findByName(degreeCourseOfNewCourse.toUpperCase());

        DegreeCourse degreeCourseOld = degreeCourseRepository.findByName(degreeCourseOfOldCourse.toUpperCase());

        Course courseToAdd = courseRepository.findByNameAndDegreeCourse(courseToAddName, degreeCourseNew.getId());

        Course courseToRemove = courseRepository.findByNameAndDegreeCourse(courseToRemoveName, degreeCourseOld.getId());

        if(!courseToAdd.getCfu().equals(courseToRemove.getCfu()))
            throw new IllegalArgumentException("courses needs to have same cfu");

        if(!student.getStudyPlan().addCourse(courseToAdd))
            throw new ObjectAlreadyExistsException(courseToAddName, EXCEPTION_COURSE_IDENTIFIER);

        if(!student.getStudyPlan().removeCourse(courseToRemove))
            throw new ObjectNotFoundException(courseToRemoveName, EXCEPTION_COURSE_IDENTIFIER);

        studyPlanRepository.saveAndFlush(student.getStudyPlan());
    }


}
