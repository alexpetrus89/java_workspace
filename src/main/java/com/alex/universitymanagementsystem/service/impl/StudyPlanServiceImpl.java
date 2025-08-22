package com.alex.universitymanagementsystem.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.dto.SwapCoursesDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.service.StudyPlanService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    // constants
    private static final String DATA_ACCESS_ERROR = "An error occurred while accessing the database.";

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
     * return study plan ordering
     * @param register the student register
     * @return String
     * @throws ObjectNotFoundException if the study plan does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public String getOrderingByRegister(Register register)
        throws ObjectNotFoundException, DataAccessServiceException
    {
        try {
            // retrieve student ordering
            return studentRepository
                .findByRegister(register)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDY_PLAN))
                .getStudyPlan()
                .getOrdering();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * return set of courses
     * @param register the student register
     * @return Set<CourseDto>
     * @throws ObjectNotFoundException if the student with the given register does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public Set<CourseDto> getCoursesByRegister(Register register)
        throws ObjectNotFoundException, DataAccessServiceException
    {
        try {
            // retrieve the courses of the study plan
            return studentRepository
                .findByRegister(register)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT))
                .getStudyPlan()
                .getCourses()
                .stream()
                .map(CourseMapper::toDto)
                .collect(Collectors.toSet());
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves the study plan of the student
     * @param register the register of the student
     * @return the study plan of the student
     * @throws ObjectNotFoundException if the student with the given register does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    public StudyPlanDto getStudyPlanByRegister(Register register)
        throws ObjectNotFoundException, DataAccessServiceException
    {
        try {
            // retrieve the ordering of the study plan
            String ordering = studentRepository
                .findByRegister(register)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT))
                .getStudyPlan()
                .getOrdering();
            // retrieve the courses of the study plan
            Set<CourseDto> courses =  studentRepository
                .findByRegister(register)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT))
                .getStudyPlan()
                .getCourses()
                .stream()
                .map(CourseMapper::toDto)
                .collect(Collectors.toSet());

            return new StudyPlanDto(ordering, courses);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Change courses to the study plan
     * @param dto with courses to swap
     * @throws IllegalArgumentException if cannot replace a course with itself or if the cfu
     * of the new course is not equal to the cfu of the old course
     * @throws ObjectNotFoundException if a degree course with the given name does not exist
     * @throws IllegalStateException if the course has examinations
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class, IllegalStateException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void swapCourses(@Valid SwapCoursesDto dto)
        throws IllegalArgumentException, ObjectNotFoundException, IllegalStateException, DataAccessServiceException
    {
        try {
            // retrieve all data
            Student student = studentRepository
                .findByRegister(new Register(dto.getRegister()))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT));

            // retrieve the courses to add and remove
            if(student.getStudyPlan() == null)
                throw new ObjectNotFoundException(DomainType.STUDY_PLAN);

            // retrieve the degree courses
            String degreeCourseNew = degreeCourseRepository
                .findByName(dto.getDegreeCourseOfNewCourse().toUpperCase())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE))
                .getName();

            String degreeCourseOld = degreeCourseRepository
                .findByName(dto.getDegreeCourseOfOldCourse().toUpperCase())
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE))
                .getName();

            // retrieve the course to add
            Course courseToAdd = courseRepository
                .findByNameAndDegreeCourseName(dto.getCourseToAdd(), degreeCourseNew)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

            // retrieve the course to remove
            Course courseToRemove = courseRepository
                .findByNameAndDegreeCourseName(dto.getCourseToRemove(), degreeCourseOld)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));

            // add the new course
            student.getStudyPlan().addCourse(courseToAdd);
            // remove the old course
            student.getStudyPlan().removeCourse(courseToRemove);
            // save
            studyPlanRepository.saveAndFlush(student.getStudyPlan());
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }



}
