package com.alex.universitymanagementsystem.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.exception.UsernameInvalidException;
import com.alex.universitymanagementsystem.repository.UserRepository;

@Service
public class RegistrationServiceImpl {

    // logger
	private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final UserRepository userRepository;

    public RegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Check if the username is already in use by other users
     * @param username
     * @return true if the username is already in use, false otherwise
     * @throws NullPointerException if the username is null
     * @throws IllegalArgumentException if the username is blank
     */
    public boolean isUsernameAlreadyTaken(@NonNull String username) {
        // sanity check
		if(username.isBlank())
            throw new UsernameInvalidException("Username cannot be blank");
        try {
            return userRepository.findByUsername(username) != null;
        } catch (DataAccessException e) {
			logger.error(DATA_ACCESS_ERROR, e);
			return true;
		}
    }

}
