package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Address;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.UserId;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.UserDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.UserMapper;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.service.UserService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

    // instance variable
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
        return userRepository.findByUsername(username)
            .map(UserMapper::toUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    /**
     * Add new user
     * @param form with data of the user to be added
     * @return UserDto
     * @throws ObjectAlreadyExistsException if user to add already exists
     * @throws DataAccessServiceException if there are trouble accessing the database
     */
    @Override
	@Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public UserDto addNewUser(RegistrationForm form) throws ObjectAlreadyExistsException, DataAccessServiceException {
        try {
            // check if the user already exists
            if(userRepository.existsByUsername(form.getUsername()))
                throw new ObjectAlreadyExistsException(DomainType.USER);

            // check if user with same fiscal code already exists
            if(userRepository.existsByFiscalCode_FiscalCode(form.getFiscalCode()))
                throw new ObjectAlreadyExistsException(DomainType.USER);

            // check if exists username with same fullname and dob
            if(userRepository
                    .findByFullname(form.getFirstName(), form.getLastName())
                    .stream()
                    .anyMatch(u -> (u.getFirstName() + " " + u.getLastName()).equals(form.getFirstName() + " " + form.getLastName()) &&
                userRepository
                    .findByDob(form.getDob())
                    .stream()
                    .anyMatch(uDob -> uDob.getDob().isEqual(form.getDob()))))
                throw new ObjectAlreadyExistsException(DomainType.USER);

            // save the user
            User user = userRepository.saveAndFlush((form.toUser(passwordEncoder)));
            return UserMapper.toDto(user);

        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form with new data of the user to be updated
     * @return UserDto
     * @throws ObjectNotFoundException if the authenticated user is not found.
     * @throws DataAccessServiceException if there are trouble accessing the database
     */
    @Override
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public UserDto updateUser(RegistrationForm form)
        throws ObjectNotFoundException, DataAccessServiceException
    {
        // SecurityContextHolder.getContext()	Recupera il contesto di sicurezza
        // getAuthentication()	Ottiene info sull’utente loggato
        // getPrincipal()	Ritorna l’oggetto utente (tipicamente UserDetails)
        User user =  (User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        // check if the user exists
        if(user == null)
            throw new ObjectNotFoundException(DomainType.USER);

        try {
            user.setUsername(form.getUsername());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            user.setFirstName(form.getFirstName());
            user.setLastName(form.getLastName());
            user.setDob(form.getDob());
            user.setFiscalCode(new FiscalCode(form.getFiscalCode()));
            user.setPhone(form.getPhone());
            user.setAddress(new Address(form.getStreet(), form.getCity(), form.getState(), form.getZip()));
            if(form.getRole() != null)
                user.setRole(form.getRole());

            // save the user
            return UserMapper.toDto(userRepository.saveAndFlush(user));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + user.getId() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Deletes a user from the repository.
     * @param userId user id of the user to be deleted
     * @return the deleted user dto
     * @throws AccessDeniedException if the authenticated user is not an admin
     * @throws UsernameNotFoundException if the user to be deleted is not found
     * @throws DataAccessServiceException if there are trouble accessing the database
     */
    @Override
    @Transactional(rollbackOn = {AccessDeniedException.class, UsernameNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public UserDto deleteUser(String userId)
        throws AccessDeniedException, UsernameNotFoundException, DataAccessServiceException
    {

        try {
            // get the authenticated user
            User admin =  (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

            if(!admin.getRole().equals(RoleType.ADMIN))
                throw new AccessDeniedException("Only admin can delete users");

            User userToDelete = userRepository
                .findById(new UserId(UUID.fromString(userId)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // delete the user
            userRepository.delete(userToDelete);
            return UserMapper.toDto(userToDelete);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + userId + ": " + e.getMessage(), e);
        }
    }

}
