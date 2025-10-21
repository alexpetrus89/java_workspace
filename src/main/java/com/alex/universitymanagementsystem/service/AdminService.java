package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.dto.AdminDto;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

public interface AdminService {

    /**
     * get all admins
     * @return List<AdminDto>
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    List<AdminDto> getAdmins() throws DataAccessServiceException;


    /**
     * Retrieves an admin by admin code
     * @param adminCode the admin code of the admin to retrieve
     * @return AdminDto object containing the admin's data
     * @throws IllegalArgumentException if the admin code is blank
     * @throws ObjectNotFoundException if no admin found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    AdminDto getAdminByAdminCode(String adminCode)
        throws IllegalArgumentException, DataAccessServiceException;


    /**
     * Retrieves an admin by fiscal code.
     * @param fiscalCode the fiscal code of the admin to retrieve.
     * @return AdminDto object containing the admin's data.
     * @throws IllegalArgumentException if the fiscal code is blank
     * @throws ObjectNotFoundException if no admin found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    AdminDto getAdminByFiscalCode(String fiscalCode)
        throws IllegalArgumentException, DataAccessServiceException;


    /**
     * Retrieves an admin by name.
     * @param fullname the name of the admin.
     * @return List<AdminDto> object containing the admin's data.
     * @throws IllegalArgumentException if the name is blank
     * @throws DataAccessServiceException if there is an error accessing the database
     * @see AdminDto
     */
    List<AdminDto> getAdminsByFullname(String fullname)
        throws IllegalArgumentException, DataAccessServiceException;


    /**
     * Adds a new admin to the repository.
     * @param form with data of the admin to be added
     * @return Optional<AdminDto> data transfer object containing the added admin's information
     * @throws ObjectAlreadyExistsException if an admin with the
     *         same admin code or same fiscal code already exists in the repository.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    Optional<AdminDto> addNewAdmin(RegistrationForm form)
        throws ObjectAlreadyExistsException, DataAccessServiceException;

}
