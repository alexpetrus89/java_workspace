package com.alex.universitymanagementsystem.service;

import java.util.Set;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.dto.SwapCoursesDto;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;



public interface StudyPlanService {

    /**
     * return study plan ordering
     * @param register the student register
     * @return String
     * @throws ObjectNotFoundException if the study plan does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    String getOrderingByRegister(Register register)
        throws ObjectNotFoundException, DataAccessServiceException;


    /**
     * return set of courses
     * @param register the student register
     * @return Set<CourseDto>
     * @throws ObjectNotFoundException if the student with the given register does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    Set<CourseDto> getCoursesByRegister(Register register)
        throws ObjectNotFoundException, DataAccessServiceException;


    /**
     * Retrieves the study plan of the student
     * @param register the register of the student
     * @return the study plan of the student
     * @throws ObjectNotFoundException if the student with the given register does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    StudyPlanDto getStudyPlanByRegister(Register register)
        throws ObjectNotFoundException, DataAccessServiceException;


    /**
     * Change courses to the study plan
     * @param dto with courses to swap
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     * @throws ObjectNotFoundException if a degree course with the given name does not exist
     * @throws IllegalArgumentException if the cfu of the new course is not equal to the cfu
     *                                  of the old course
     * @throws IllegalStateException if the course has examinations
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Transactional(rollbackOn = {ObjectAlreadyExistsException.class, ObjectNotFoundException.class, IllegalArgumentException.class, IllegalStateException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    void swapCourses(@Valid SwapCoursesDto dto)
        throws ObjectAlreadyExistsException, ObjectNotFoundException, IllegalArgumentException, IllegalStateException, DataAccessServiceException;


}
