package com.alex.studentmanagementsystem.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {

    /**
     * Return user details
     * @param String username
     * @return UserDetails
     * @throws UsernameNotFoundException if the user is not found
     */
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException;

}
