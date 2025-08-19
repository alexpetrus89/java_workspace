package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

public interface ProfessorService {

    /**
     * get all professors
     * @return List<ProfessorDto>
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<ProfessorDto> getProfessors() throws DataAccessServiceException;


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return ProfessorDto object containing the professor's data
     * @throws IllegalArgumentException if the unique code is blank
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    ProfessorDto getProfessorByUniqueCode(UniqueCode uniqueCode)
        throws IllegalArgumentException, DataAccessServiceException;


    /**
     * Retrieves a professor by fiscal code.
     * @param fiscalCode the fiscal code of the professor to retrieve.
     * @return ProfessorDto object containing the professor's data.
     * @throws IllegalArgumentException if the fiscal code is blank
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    ProfessorDto getProfessorByFiscalCode(String fiscalCode)
        throws IllegalArgumentException, DataAccessServiceException;


    /**
     * Retrieves a professor by name.
     * @param fullname the name of the professor.
     * @return List<ProfessorDto> object containing the professor's data.
     * @throws IllegalArgumentException if the name is blank
     * @throws DataAccessServiceException if there is an error accessing the database
     * @see ProfessorDto
     */
    List<ProfessorDto> getProfessorsByFullname(String fullname)
        throws IllegalArgumentException, DataAccessServiceException;


    /**
     * Adds a new professor to the repository.
     * @param form with data of the professor to be added
     * @return ProfessorDto
     * @throws ObjectAlreadyExistsException if a professor with the
     *         same unique code or same fiscal code already exists in the repository.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ProfessorDto addNewProfessor(RegistrationForm form)
        throws ObjectAlreadyExistsException, DataAccessServiceException;


    /**
     * Updates an existing professor's information.
     * @param form with new data of the professor to be updated
     * @return ProfessorDto
     * @throws ObjectNotFoundException if no professor with the given unique code
     *         exists in the repository.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ProfessorDto updateProfessor(RegistrationForm form)
        throws ObjectNotFoundException, DataAccessServiceException;


}
