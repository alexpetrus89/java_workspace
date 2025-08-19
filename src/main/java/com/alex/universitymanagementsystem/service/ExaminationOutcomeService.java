package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface ExaminationOutcomeService {


    /**
     * retrive outcome by id
     * @param id
     * @return ExaminationOutcome
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    ExaminationOutcomeDto getOutcomeById(Long id)
        throws ObjectNotFoundException, DataAccessServiceException;


    /**
     * Get an examination outcome by student register and course
     * @param course name of the course
     * @param register of the student
     * @param LocalDate date of the examination
     * @return ExaminationOutcome outcome
     * @throws IllegalArgumentException if the course name or register is invalid
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    ExaminationOutcomeDto getOutcomeByCourseAndStudent(String courseName, String register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * Get an examination outcomes by student register
     * @param register of the student
     * @return List of examination outcomes
     * @throws NoSuchElementException if the student does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.  
     */
    List<ExaminationOutcomeDto> getStudentOutcomes(String register)
        throws NoSuchElementException, DataAccessServiceException;


    /**
     * Save an examination outcome
     * @param ExaminationOutcomeDto data transfer object
     * @return ExaminationOutcome outcome
     * @throws NoSuchElementException if the student does not exist
     * @throws ObjectNotFoundException if the appeal does not exist
     * @throws ObjectAlreadyExistsException if
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Transactional(rollbackOn = {NoSuchElementException.class, ObjectNotFoundException.class, ObjectAlreadyExistsException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ExaminationOutcomeDto addNewExaminationOutcome(@Valid ExaminationOutcomeDto dto)
        throws NoSuchElementException, ObjectNotFoundException, ObjectAlreadyExistsException, DataAccessServiceException;


    /**
     * Delete an examination outcome
     * @param outcome examination outcome of the student
     * @return ExaminationOutcome outcome
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ExaminationOutcomeDto deleteExaminationOutcome(@Valid ExaminationOutcomeDto dto)
        throws ObjectNotFoundException, DataAccessServiceException;

}
