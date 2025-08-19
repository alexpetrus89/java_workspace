package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


public interface ExaminationAppealService {


    /**
     * Retrieves all examination appeals
     * @return a list of examination appeals
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<ExaminationAppealDto> getExaminationAppeals() throws DataAccessServiceException;


    /**
     * Retrieves an examination appeal
     * @param id the ID of the examination appeal
     * @return examination appeal dto
     * @throws IllegalArgumentException if there is an error with id
     * @throws NoSuchElementException if the examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    ExaminationAppealDto getExaminationAppealById(Long id)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Retrieves all examination appeals for a student
     * @param register student register
     * @return a list of dto's of examination appeals available
     * @throws IllegalArgumentException if the register is blank
     * @throws NoSuchElementException if the student does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<ExaminationAppealDto> getExaminationAppealsAvailable(Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Retrieves all examination appeals booked by a student
     * @param register student register
     * @return a list of dto's of examination appeals booked
     * @throws IllegalArgumentException if the register is blank
     * @throws NoSuchElementException if the student does not exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<ExaminationAppealDto> getExaminationAppealsBookedByStudent(Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Retrieves all examination appeals made by professor
     * @param uniqueCode professor unique code
     * @return a list of examination appeals data transfer objects
     * @throws IllegalArgumentException if the unique code is blank
     * @throws NoSuchElementException if the professor does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<ExaminationAppealDto> getExaminationAppealsMadeByProfessor(UniqueCode uniqueCode)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Adds a new examination appeal
     * @param dto examination appeal data transfer object
     * @return examinationAppeal data transfer object
     * @throws IllegalArgumentException if the course name or register is invalid
     * @throws NoSuchElementException if the degree course or professor does not exist
     * @throws IllegalStateException if the professor does not teach the course
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {IllegalArgumentException.class, NoSuchElementException.class, IllegalStateException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ExaminationAppealDto addNewExaminationAppeal(@Valid ExaminationAppealDto dto)
        throws IllegalArgumentException, NoSuchElementException, IllegalStateException, DataAccessServiceException;


    /**
     * deletes an examination appeal
     * @param dto professor data transfer object
     * @param id examination appeal id
     * @return boolean
     * @throws ObjectNotFoundException if the professor does not exists
     * @throws NoSuchElementException if the examination appeal does not exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {ObjectNotFoundException.class, NoSuchElementException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public boolean deleteExaminationAppeal(ProfessorDto professorDto, Long id)
        throws ObjectNotFoundException, NoSuchElementException, DataAccessServiceException;


    /**
     * Adds a student to an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws NoSuchElementException if the student or examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {IllegalArgumentException.class, NoSuchElementException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationAppealDto addStudentToAppeal(Long id, Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;


    /**
     * Removes a student from an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal data transfer object
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws NoSuchElementException if the student or examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {IllegalArgumentException.class, NoSuchElementException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ExaminationAppealDto removeStudentFromAppeal(Long id, Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException;

}
