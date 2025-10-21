package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.dto.AdminDto;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.entity.Admin;
import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.AdminMapper;
import com.alex.universitymanagementsystem.repository.AdminRepository;
import com.alex.universitymanagementsystem.service.AdminService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    // constants
    private static final String ADMIN_CODE_ERROR = "Admin code cannot be null or empty";

    // instance variables
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceHelpers helpers;
    private final ServiceValidators validators;

    // autowired - dependency injection - constructor
    public AdminServiceImpl(
        AdminRepository adminRepository,
        PasswordEncoder passwordEncoder,
        ServiceHelpers helpers,
        ServiceValidators validators
    ) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.helpers = helpers;
        this.validators = validators;
    }


    /**
     * get all admins
     * @return List<AdminDto>
     * @throws DataAccessServiceException if there is an error accessing the database
     * @see AdminDto
     */
    @Override
    public List<AdminDto> getAdmins() throws DataAccessServiceException {
        try {
            return adminRepository
                .findAll()
                .stream()
                .map(AdminMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professors: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves an admin by admin code
     * @param adminCode the admin code of the admin to retrieve
     * @return AdminDto object containing the admin's data
     * @throws IllegalArgumentException if the admin code is blank
     * @throws ObjectNotFoundException if no admin found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public AdminDto getAdminByAdminCode(String adminCode)
        throws IllegalArgumentException, DataAccessServiceException
    {
        // sanity check
        validators.validateNotNullOrNotBlank(adminCode, ADMIN_CODE_ERROR);

        try {
            return AdminMapper.toDto(helpers.fetchAdmin(adminCode));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professor by unique code: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves an admin by fiscal code.
     * @param fiscalCode the fiscal code of the admin to retrieve.
     * @return AdminDto object containing the admin's data.
     * @throws IllegalArgumentException if the fiscal code is blank
     * @throws ObjectNotFoundException if no admin found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public AdminDto getAdminByFiscalCode(String fiscalCode)
        throws IllegalArgumentException, DataAccessServiceException
    {
        validators.validateNotNullOrNotBlank(fiscalCode, "Fiscal code cannot be null or empty");

        try {
            return adminRepository
                .findByFiscalCode(new FiscalCode(fiscalCode))
                .map(AdminMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.ADMIN));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professor by fiscal code: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves an admin by name.
     * @param fullname the name of the admin.
     * @return List<AdminDto> object containing the admin's data.
     * @throws IllegalArgumentException if the name is blank
     * @throws DataAccessServiceException if there is an error accessing the database
     * @see AdminDto
     */
    @Override
    public List<AdminDto> getAdminsByFullname(String fullname)
        throws IllegalArgumentException, DataAccessServiceException
    {
        String[] nameParts = fullname.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // sanity check
        validators.validateNotNullOrNotBlank(firstName, "First name cannot be null or empty");
        validators.validateNotNullOrNotBlank(lastName, "Last name cannot be null or empty");

        try {
            return adminRepository
                .findByFullname(firstName, lastName)
                .stream()
                .map(AdminMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professors by fullname: " + e.getMessage(), e);
        }
    }


    /**
     * Adds a new admin to the repository.
     * @param form with data of the admin to be added
     * @return Optional<AdminDto> data transfer object containing the added admin's information
     * @throws ObjectAlreadyExistsException if an admin with the
     *         same admin code or same fiscal code already exists in the repository.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<AdminDto> addNewAdmin(RegistrationForm form)
        throws ObjectAlreadyExistsException, DataAccessServiceException
    {
        try {
            Admin admin = adminRepository.saveAndFlush(form.toAdmin(passwordEncoder));
            return Optional.of(AdminMapper.toDto(admin));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }

}
