package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.UserId;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.service.UserDetailsService;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserDetailsService{

    // logger
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    // instance variable
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
	 * Retrieves all users from the repository.
	 * @return List of Users.
	 */
    @Override
    public List<User> getUsers() {
		return userRepository.findAll();
	}

    /**
     * Return user details
     * @param username username
     * @return UserDetails
     * @throws NullPointerException if the user is null
     * @throws IllegalArgumentException if the username is blank
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String username)
        throws NullPointerException, IllegalArgumentException
    {
        // sanity check
        if(username.isBlank())
            throw new IllegalArgumentException("username cannot be blank");
        return userRepository.findByUsername(username);
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form RegistrationForm containing updated user details.
     * @return String representing the redirect URL after the update process.
     * @throws NullPointerException if the user is null.
     * @throws ObjectAlreadyExistsException if the user already exists.
	 */
	@Transactional
    public String addNewUser(@NonNull User user)
        throws NullPointerException, ObjectAlreadyExistsException {
        try {
            if(userRepository.existsById(user.getId()))
                throw new ObjectAlreadyExistsException(DomainType.USER);

            if(userRepository.findByFullname(user.getFullname())
                    .stream()
                    .anyMatch(u -> u.getFullname().equals(user.getFullname())) &&
                userRepository.findByDob(user.getDob())
                    .stream()
                    .anyMatch(u -> u.getDob().isEqual(user.getDob())))
                throw new ObjectAlreadyExistsException(DomainType.USER);
            // save the user
            userRepository.saveAndFlush(user);
            return REDIRECT_LOGIN;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return "redirect:error/error";
        }
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form RegistrationForm containing updated user details.
     * @throws NullPointerException if the form is null.
     * @throws IllegalArgumentException if the username is blank.
     * @throws ObjectNotFoundException if the authenticated user is not found.
     */
    @Transactional(rollbackOn = {NullPointerException.class, IllegalArgumentException.class, ObjectNotFoundException.class})
    public User updateUser(@NonNull RegistrationForm form)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException
    {
        // get the authenticated user
        User user =  (User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        // check if the user exists
        if(user == null)
            throw new ObjectNotFoundException(DomainType.USER);

        // check if the username is blank
        if(form.getUsername().isBlank())
            throw new IllegalArgumentException("Username cannot be blank");

        try {
            user.setUsername(form.getUsername());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            user.setFullname(form.getFullname());
            user.setDob(form.getDob());
            user.setStreet(form.getStreet());
            user.setCity(form.getCity());
            user.setState(form.getState());
            user.setZip(form.getZip());
            user.setPhoneNumber(form.getPhone());
            if(form.getRole() != null)
                user.setRole(form.getRole());

            // save the user
            return userRepository.saveAndFlush(user);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Deletes a user from the repository.
     * @param userId user id of the user to be deleted
     * @throws NullPointerException if the username is null
     * @throws IllegalArgumentException if the authenticated user is not an admin
     * @throws UsernameNotFoundException if the user to be deleted is not found
     */
    @Transactional(rollbackOn = {NullPointerException.class, IllegalArgumentException.class, UsernameNotFoundException.class})
    public boolean deleteUser(@NonNull String userId)
        throws NullPointerException, IllegalArgumentException, UsernameNotFoundException
    {
        if(userId.isBlank())
            throw new IllegalArgumentException("User id cannot be blank");

        try {
            // get the authenticated user
            User admin =  (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

            if(!admin.getRole().equals(RoleType.ADMIN))
                throw new IllegalArgumentException("Only admin can delete users");

            User userToDelete = userRepository
                .findById(new UserId(UUID.fromString(userId)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // delete the user
            userRepository.delete(userToDelete);
            return true;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return false;
        }
    }

}
