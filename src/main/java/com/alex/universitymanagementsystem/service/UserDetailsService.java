package com.alex.universitymanagementsystem.service;


import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;

import com.alex.universitymanagementsystem.domain.User;


public interface UserDetailsService {

    /**
	 * Retrieves all users from the repository.
	 * @return List of Users.
	 */
    List<User> getUsers();

    /**
     * Return user details
     * @param username username
     * @return UserDetails
     * @throws NullPointerException if the user is null
     * @throws IllegalArgumentException if the username is blank
     */
    UserDetails loadUserByUsername(@NonNull String username)
        throws NullPointerException, IllegalArgumentException;

}
