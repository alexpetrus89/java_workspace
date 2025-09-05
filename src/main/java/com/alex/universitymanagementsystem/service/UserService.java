package com.alex.universitymanagementsystem.service;


import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.UpdateForm;
import com.alex.universitymanagementsystem.dto.UserDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.DuplicateFiscalCodeException;
import com.alex.universitymanagementsystem.exception.DuplicateUsernameException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

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
     * @return Optional<UserDto>
     * @throws ObjectAlreadyExistsException if user to add already exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
	@Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    Optional<UserDto> addNewUser(RegistrationForm form) throws ObjectAlreadyExistsException, DataAccessServiceException;


	/**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form with new data of the user to be updated.
     * @return Optional<UserDto> data transfer object containing the updated user information.
     * @throws ObjectNotFoundException if the authenticated user is not found.
     * @throws DuplicateUsernameException if the new username is already in use by another user.
     * @throws DuplicateFiscalCodeException if the new fiscal code is already in use by another user
     * @throws DataAccessServiceException if there are trouble accessing the database.
     */
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<UserDto> updateUser(UpdateForm form)
        throws ObjectNotFoundException, DuplicateUsernameException, DuplicateFiscalCodeException, DataAccessServiceException;


	/**
     * Deletes a user from the repository.
     * @param userId user id of the user to be deleted
     * @return Optional<UserDto> data transfer object containing the deleted user information
     * @throws AccessDeniedException if the authenticated user is not an admin
     * @throws UsernameNotFoundException if the user to be deleted is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {AccessDeniedException.class, UsernameNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    Optional<UserDto> deleteUser(String userId)
        throws AccessDeniedException, UsernameNotFoundException, DataAccessServiceException;

}
