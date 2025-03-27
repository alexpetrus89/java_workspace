package com.alex.universitymanagementsystem.service.impl;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.service.UserDetailsService;
import com.alex.universitymanagementsystem.utils.RegistrationForm;
import com.alex.universitymanagementsystem.utils.Role;

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
     * @throws ObjectNotFoundException if the authenticated user is not found
	 */
    @Override
    public List<User> getUsers() {
		return userRepository.findAll();
	}

    /**
     * Retrieves a user by username from the repository.
     * @param username username
     * @return UserDetails
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String username)
        throws UsernameNotFoundException
    {

        return userRepository
            .findByUsername(username)
            .map(UserDetails.class::cast)
            .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form RegistrationForm containing updated user details.
     * @return String representing the redirect URL after the update process.
     * @throws ObjectNotFoundException if the authenticated user is not found.
	 * @throws UserNotFoundException if the user is not found.
     * @throws IllegalArgumentException if the updated user details are invalid.
	 */
	@Transactional
    public String addNewUser(User user) throws ObjectNotFoundException {

        // sanity check
        if(user == null)
            throw new IllegalArgumentException("User cannot be null");

        try {
            // save the user
            userRepository.saveAndFlush(user);
            return REDIRECT_LOGIN;
        } catch (ObjectNotFoundException e) {
            return REDIRECT_LOGIN;
        }
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form RegistrationForm containing updated user details.
     * @return String representing the redirect URL after the update process.
     * @throws ObjectNotFoundException if the authenticated user is not found.
     */
    public String updateUser(RegistrationForm form)
        throws ObjectNotFoundException {
        // get the authenticated user
        User user =  (User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        // check if the user exists
        if(user == null)
            throw new ObjectNotFoundException("User not found", "user");

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
     * @throws IllegalArgumentException if the authenticated user is not an admin
     * @throws UsernameNotFoundException if the user to be deleted is not found
     * @throws ObjectNotFoundException if the authenticated user is not found
     */
    public void deleteUser(String username) {

        // get the authenticated user
        User admin =  (User) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        if(!admin.getRole().equals(Role.ADMIN))
            throw new IllegalArgumentException("Only admin can delete users");

        User userToDelete = (User) userRepository
            .findByUsername(username)
            .map(UserDetails.class::cast)
            .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));

        // delete the user
        userRepository.delete(userToDelete);
    }


}
