package com.alex.universitymanagementsystem.service.impl;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.User;
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

    // constants
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
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String username)
        throws NullPointerException, IllegalArgumentException, UsernameNotFoundException
    {
        return userRepository.findByUsername(username);
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form RegistrationForm containing updated user details.
     * @return String representing the redirect URL after the update process.
     * @throws NullPointerException if the user is null.
     * @throws ObjectNotFoundException if the authenticated user is not found.
	 */
	@Transactional
    public String addNewUser(@NonNull User user)
        throws NullPointerException, ObjectNotFoundException
    {
        try {
            // save the user
            userRepository.saveAndFlush(user);
            return REDIRECT_LOGIN;
        } catch (ObjectAlreadyExistsException e) {
            return REDIRECT_LOGIN;
        }
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form RegistrationForm containing updated user details.
     * @return String representing the redirect URL after the update process.
     * @throws NullPointerException if the form is null.
     * @throws IllegalArgumentException if the username is blank.
     * @throws ObjectNotFoundException if the authenticated user is not found.
     */
    public String updateUser(@NonNull RegistrationForm form)
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
            user.setRole(form.getRole());

            // save the
            userRepository.saveAndFlush(user);
            return REDIRECT_LOGIN;

        } catch (ObjectNotFoundException e) {
            return "redirect:/user/update";
        }
    }


    /**
     * Deletes a user from the repository.
     * @param username the username of the user to be deleted
     * @throws NullPointerException if the username is null
     * @throws IllegalArgumentException if the authenticated user is not an admin
     * @throws UsernameNotFoundException if the user to be deleted is not found
     */
    @Transactional
    public void deleteUser(@NonNull String username)
        throws NullPointerException, IllegalArgumentException, UsernameNotFoundException
    {

        // get the authenticated user
        User admin =  (User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        if(!admin.getRole().equals(RoleType.ADMIN))
            throw new IllegalArgumentException("Only admin can delete users");

        User userToDelete = userRepository.findByUsername(username);

        if(userToDelete == null)
            throw new UsernameNotFoundException("User not found");

        // delete the user
        userRepository.delete(userToDelete);
    }


}
