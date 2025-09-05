package com.alex.universitymanagementsystem.service.impl;

import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.repository.UserRepository;

import jakarta.persistence.PersistenceException;

@Service
public class RegistrationServiceImpl {

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final UserRepository userRepository;

    public RegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Check if the username is already in use by other users
     * @param username
     * @return true if the username is already in use, false otherwise
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    public boolean isUsernameAlreadyTaken(String username) throws DataAccessServiceException
    {
        try {
            return userRepository.findByUsername(username).isPresent();
        } catch (PersistenceException e) {
			throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
		}
    }


    /**
     * Check if the fiscal code is already in use by other users
     * @param fiscalCode
     * @return true if the fiscal code is already in use, false otherwise
     * @throws DataAccessServiceException
     */
    public boolean isFiscalCodeAlreadyTaken(String fiscalCode) throws DataAccessServiceException
    {
        try {
            return userRepository.findByFiscalCode_FiscalCode(fiscalCode).isPresent();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }

}
