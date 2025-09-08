package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

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
     * @throws ObjectNotFoundException if no professor found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    ProfessorDto getProfessorByUniqueCode(String uniqueCode)
        throws IllegalArgumentException, DataAccessServiceException;


    /**
     * Retrieves a professor by fiscal code.
     * @param fiscalCode the fiscal code of the professor to retrieve.
     * @return ProfessorDto object containing the professor's data.
     * @throws IllegalArgumentException if the fiscal code is blank
     * @throws ObjectNotFoundException if no professor found
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
     * @return Optional<ProfessorDto> data transfer object containing the added professor's information
     * @throws ObjectAlreadyExistsException if a professor with the
     *         same unique code or same fiscal code already exists in the repository.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    Optional<ProfessorDto> addNewProfessor(RegistrationForm form)
        throws ObjectAlreadyExistsException, DataAccessServiceException;



}
