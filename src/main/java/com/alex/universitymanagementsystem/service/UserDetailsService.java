package com.alex.universitymanagementsystem.service;


import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;


public interface UserDetailsService {

    /**
	 * Retrieves all users from the repository.
	 * @return List of Users.
     * @throws ObjectNotFoundException if the authenticated user is not found
	 */
    List<User> getUsers() throws ObjectNotFoundException;

    /**
     * Return user details
     * @param username username
     * @return UserDetails
     * @throws UsernameNotFoundException if the user is not found
     * @throws IllegalArgumentException if the username is null
     */
    public UserDetails loadUserByUsername(@NonNull String username)
        throws UsernameNotFoundException;

}
