package com.alex.universitymanagementsystem.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.dto.SwapCoursesDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
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
    private final ServiceHelpers helpers;
    private final ServiceValidators validators;

    public StudyPlanServiceImpl(
        StudyPlanRepository studyPlanRepository,
        ServiceHelpers helpers,
        ServiceValidators validators
    ) {
        this.studyPlanRepository = studyPlanRepository;
        this.helpers = helpers;
        this.validators = validators;
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
        return getStudyPlan(register).getOrdering();
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
        // retrieve the courses of the study plan
        return getStudyPlan(register)
            .getCourses()
            .stream()
            .map(CourseMapper::toDto)
            .collect(Collectors.toSet());
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
        // retrieve the ordering of the study plan
        String ordering = getStudyPlan(register).getOrdering();
        // retrieve the courses of the study plan
        Set<CourseDto> courses = getStudyPlan(register)
            .getCourses()
            .stream()
            .map(CourseMapper::toDto)
            .collect(Collectors.toSet());

        return new StudyPlanDto(ordering, courses);
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
            // retrieve study plan
            StudyPlan studyPlan = Optional.ofNullable(getStudyPlan(new Register(dto.getRegister())))
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDY_PLAN));

            // retrieve course to add
            Course courseToAdd = getCourse(dto.getCourseToAdd(), dto.getDegreeCourseOfNewCourse());
            // retrieve course to remove
            Course courseToRemove = getCourse(dto.getCourseToRemove(), dto.getDegreeCourseOfOldCourse());

            // add the new course
            studyPlan.addCourse(courseToAdd);
            // remove the old course
            studyPlan.removeCourse(courseToRemove);
            // save changes
            studyPlanRepository.saveAndFlush(studyPlan);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }



    // --- Helper methods ---

    /**
     * Retrieves a student by their register.
     * @param register the student's register
     * @return the student
     */
    private StudyPlan getStudyPlan(Register register) {
        try {
            return helpers
                .fetchStudent(register.toString())
                .getStudyPlan();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves a course by its name and degree course name.
     * @param courseName
     * @param degreeCourseName
     * @return the course
     */
    private Course getCourse(String courseName, String degreeCourseName) {
        try {
            String normalizedDegreeCourse = degreeCourseName.toUpperCase();

            validators.validateDegreeCourseExists(normalizedDegreeCourse);

            return helpers.fetchCourse(courseName, normalizedDegreeCourse);

        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }



}
