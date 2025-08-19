package com.alex.universitymanagementsystem.service;


import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alex.universitymanagementsystem.dto.UserDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;


public interface UserService extends UserDetailsService {

    /**
	 * Retrieves all users.
	 * @return List of Users.
     * @throws DataAccessServiceException if there is an error accessing the database.
	 */
    List<UserDto> getUsers() throws DataAccessServiceException;


    /**
     * Add new user
     * @param form with data of the user to be added
     * @return UserDto
     * @throws ObjectAlreadyExistsException if user to add already exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
	@Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    UserDto addNewUser(RegistrationForm form) throws ObjectAlreadyExistsException, DataAccessServiceException;


	/**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form with new data of the user to be updated
     * @return UserDto
     * @throws ObjectNotFoundException if the authenticated user is not found.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    UserDto updateUser(RegistrationForm form)
        throws ObjectNotFoundException, DataAccessServiceException;


	/**
     * Deletes a user from the repository.
     * @param userId user id of the user to be deleted
     * @return boolean
     * @throws AccessDeniedException if the authenticated user is not an admin
     * @throws UsernameNotFoundException if the user to be deleted is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {AccessDeniedException.class, UsernameNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    boolean deleteUser(String userId)
        throws AccessDeniedException, UsernameNotFoundException, DataAccessServiceException;

}
