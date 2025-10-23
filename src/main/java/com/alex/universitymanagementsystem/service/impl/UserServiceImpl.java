package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.UpdateForm;
import com.alex.universitymanagementsystem.dto.UserDto;
import com.alex.universitymanagementsystem.entity.Address;
import com.alex.universitymanagementsystem.entity.Student;
import com.alex.universitymanagementsystem.entity.User;
import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.DuplicateFiscalCodeException;
import com.alex.universitymanagementsystem.exception.DuplicateUsernameException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.UserMapper;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.service.StudentService;
import com.alex.universitymanagementsystem.service.UserService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

    // constants
    private static final String USER_NOT_FOUND = "User not found: ";

    // instance variable
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, StudentService studentService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }


    /**
	 * Retrieves all users.
	 * @return List of Users.
     * @throws DataAccessServiceException if there is an error accessing the database.
	 */
    @Override
    public List<UserDto> getUsers() throws DataAccessServiceException {
        try {
            // fetch all users from the repository
            return userRepository
                .findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching users: " + e.getMessage(), e);
        }
	}


    /**
     * Return user details
     * @param username username
     * @return UserDetails
     * @throws UsernameNotFoundException if the user could not be found or the user has no
	 * GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));
    }


    /**
     * Add new user
     * @param form with data of the user to be added
     * @return Optional<UserDto>
     * @throws ObjectAlreadyExistsException if user to add already exists
     * @throws DataAccessServiceException if there are trouble accessing the database
     */
    @Override
	@Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<UserDto> addNewUser(RegistrationForm form) throws ObjectAlreadyExistsException, DataAccessServiceException {
        try {
            // save the user
            User user = userRepository.saveAndFlush((form.toUser(passwordEncoder)));
            // if the user was created successfully, return the DTO
            return Optional.of(UserMapper.toDto(user));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form with new data of the user to be updated.
     * @return Optional<UserDto> data transfer object containing the updated user information.
     * @throws UsernameNotFoundException if the authenticated user is not found.
     * @throws DuplicateUsernameException if the new username is already in use by another user.
     * @throws DuplicateFiscalCodeException if the new fiscal code is already in use by another user
     * @throws DataAccessServiceException if there are trouble accessing the database.
     */
    @Override
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<UserDto> updateUser(UpdateForm form)
        throws UsernameNotFoundException, DuplicateUsernameException, DuplicateFiscalCodeException, DataAccessServiceException
    {
        User updatableUser = userRepository
            .findByUsername(form.getUsernameOriginal())
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + form.getUsernameOriginal()));

        try {

            // sanity checks
            checkDuplicateUsername(updatableUser, form);
            checkDuplicateFiscalCode(updatableUser, form);

            if (!form.getUsername().isBlank()) updatableUser.setUsername(form.getUsername());
            if (!form.getPassword().isBlank()) updatableUser.setPassword(passwordEncoder.encode(form.getPassword()));
            if (!form.getFirstName().isBlank()) updatableUser.setFirstName(form.getFirstName());
            if (!form.getLastName().isBlank()) updatableUser.setLastName(form.getLastName());
            if (form.getDob() != null) updatableUser.setDob(form.getDob());
            if (!form.getFiscalCode().isBlank()) updatableUser.setFiscalCode(new FiscalCode(form.getFiscalCode()));
            if (!form.getPhone().isBlank()) updatableUser.setPhone(form.getPhone());
            updatableUser.setAddress(new Address(
                form.getStreet() != null ? form.getStreet() : updatableUser.getAddress().getStreet(),
                form.getCity() != null ? form.getCity() : updatableUser.getAddress().getCity(),
                form.getCountry() != null ? form.getCountry() : updatableUser.getAddress().getCountry(),
                form.getZip() != null ? form.getZip() : updatableUser.getAddress().getZipCode()
            ));
            Optional.ofNullable(form.getRole()).ifPresent(updatableUser::setRole);

            // save the user
            User updatedUser = userRepository.saveAndFlush(updatableUser);
            return Optional.of(UserMapper.toDto(updatedUser));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + updatableUser.getId() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Deletes a user from the repository.
     * @param username of the user to be deleted
     * @return Optional<UserDto> data transfer object containing the deleted user information
     * @throws AccessDeniedException if the authenticated user is not an admin
     * @throws UsernameNotFoundException if the user to be deleted is not found
     * @throws DataAccessServiceException if there are trouble accessing the database
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(rollbackOn = {AccessDeniedException.class, UsernameNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<UserDto> deleteUser(String username)
        throws AccessDeniedException, UsernameNotFoundException, DataAccessServiceException
    {
        try {

            User userToDelete = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));

            if(userToDelete instanceof Student student)
                studentService.deleteStudentRelationship(student);

            // delete the user
            userRepository.delete(userToDelete);
            return Optional.of(UserMapper.toDto(userToDelete));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + username + ": " + e.getMessage(), e);
        }
    }


    /**
     * Converts a user to an UpdateForm
     * @param username of the user to be converted
     * @return an UpdateForm
     * @throws UsernameNotFoundException if the user to be converted is not found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public UpdateForm getUpdateFormByUsername(String username)
        throws UsernameNotFoundException, DataAccessServiceException {

        try {
            // Retry user
            User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username));

            // Map User -> UpdateForm
            UpdateForm form = new UpdateForm();
            form.setUsername(user.getUsername());
            form.setUsernameOriginal(user.getUsername());
            form.setFirstName(user.getFirstName());
            form.setLastName(user.getLastName());
            form.setDob(user.getDob());
            form.setFiscalCode(String.valueOf(user.getFiscalCode()));
            form.setStreet(user.getAddress().getStreet());
            form.setCity(user.getAddress().getCity());
            form.setCountry(user.getAddress().getCountry());
            form.setZip(user.getAddress().getZipCode());
            form.setPhone(user.getPhone());
            form.setRole(user.getRole());

            return form;
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + username + ": " + e.getMessage(), e);
        }

    }







    // helper methods

    /**
     * Checks if the username of the user to be updated is already taken by another user.
     * @param updatableUser the user to be updated
     * @param form the update form
     */
    private void checkDuplicateUsername(User updatableUser, UpdateForm form)
        throws DuplicateUsernameException
    {
        if (!updatableUser.getUsername().equals(form.getUsername()) &&
                userRepository.existsByUsernameAndIdNot(form.getUsername(), updatableUser.getId()))
            throw new DuplicateUsernameException(form.getUsername());
    }


    private void checkDuplicateFiscalCode(User updatableUser, UpdateForm form)
        throws DuplicateFiscalCodeException
    {
        if (!updatableUser.getFiscalCode().toString().equals(form.getFiscalCode()) &&
                userRepository.existsByFiscalCodeAndIdNot(new FiscalCode(form.getFiscalCode()), updatableUser.getId()))
            throw new DuplicateFiscalCodeException(form.getFiscalCode());
    }



}
