package com.alex.studentmanagementsystem.service;


import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface UserDetailsService {

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
