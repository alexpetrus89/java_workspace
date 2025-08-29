package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Address;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.service.ProfessorService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

// business logic
@Service
public class ProfessorServiceImpl implements ProfessorService {

    // instance variables
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    // autowired - dependency injection - constructor
    public ProfessorServiceImpl(ProfessorRepository professorRepository, PasswordEncoder passwordEncoder) {
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Retrieves all professors.
     * @return List of ProfessorDto objects containing all professors' data.
     * @throws DataAccessServiceException if there is an error accessing the database
     * @see ProfessorDto
     */
    @Override
    public List<ProfessorDto> getProfessors() throws DataAccessServiceException {
        try {
            return professorRepository
                .findAll()
                .stream()
                .map(ProfessorMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professors: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return ProfessorDto object containing the professor's data
     * @throws IllegalArgumentException if the unique code is blank
     * @throws ObjectNotFoundException if no professor found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public ProfessorDto getProfessorByUniqueCode(UniqueCode uniqueCode)
        throws IllegalArgumentException, DataAccessServiceException
    {
        // sanity check
        if(uniqueCode.toString().isBlank())
            throw new IllegalArgumentException("Unique Code cannot be null or empty");

        try {
            return professorRepository
                .findByUniqueCode(uniqueCode)
                .map(ProfessorMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.PROFESSOR));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professor by unique code: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves a professor by fiscal code.
     * @param fiscalCode the fiscal code of the professor to retrieve.
     * @return ProfessorDto object containing the professor's data.
     * @throws IllegalArgumentException if the fiscal code is blank
     * @throws ObjectNotFoundException if no professor found
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public ProfessorDto getProfessorByFiscalCode(String fiscalCode)
        throws IllegalArgumentException, DataAccessServiceException
    {
        if(fiscalCode.isBlank())
            throw new IllegalArgumentException("Fiscal Code cannot be null or empty");

        try {
            return professorRepository
                .findByFiscalCode(new FiscalCode(fiscalCode))
                .map(ProfessorMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.PROFESSOR));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professor by fiscal code: " + e.getMessage(), e);
        }
    }


    /**
     * Retrieves a professor by name.
     * @param fullname the name of the professor.
     * @return List<ProfessorDto> object containing the professor's data.
     * @throws IllegalArgumentException if the name is blank
     * @throws DataAccessServiceException if there is an error accessing the database
     * @see ProfessorDto
     */
    @Override
    public List<ProfessorDto> getProfessorsByFullname(String fullname) throws IllegalArgumentException, DataAccessServiceException
    {
        String[] nameParts = fullname.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // sanity check
        if(firstName.isBlank() || lastName.isBlank())
            throw new IllegalArgumentException("First name and last name cannot be null or empty");

        try {
            return professorRepository
                .findByFullname(firstName, lastName)
                .stream()
                .map(ProfessorMapper::toDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for fetching professors by fullname: " + e.getMessage(), e);
        }
    }


    /**
     * Adds a new professor to the repository.
     * @param form with data of the professor to be added
    * @return Optional<ProfessorDto> data transfer object containing the added professor's information
     * @throws ObjectAlreadyExistsException if a professor with the
     *         same unique code already exists in the repository.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = ObjectAlreadyExistsException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<ProfessorDto> addNewProfessor(RegistrationForm form)
        throws ObjectAlreadyExistsException, DataAccessServiceException
    {
        try {
            FiscalCode fiscalCode = new FiscalCode(form.getFiscalCode());

            if(professorRepository.existsByFiscalCode(fiscalCode))
                throw new ObjectAlreadyExistsException(fiscalCode);

            Professor professor = professorRepository.save(form.toProfessor(passwordEncoder));
            return Optional.of(ProfessorMapper.toDto(professor));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }


    /**
     * Updates an existing professor's information.
     * @param form with new data of the professor to be updated
     * @return ProfessorDto
     * @throws ObjectNotFoundException if no professor with the given unique code
     *         exists in the repository.
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ProfessorDto updateProfessor(RegistrationForm form)
        throws ObjectNotFoundException, DataAccessServiceException
    {

        Professor updatableProfessor =  (Professor) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        try {
            // sanity check
            if(updatableProfessor == null)
                throw new ObjectNotFoundException(DomainType.PROFESSOR);

            // update
            updatableProfessor.setUsername(form.getUsername());
            updatableProfessor.setFirstName(form.getFirstName());
            updatableProfessor.setLastName(form.getLastName());
            updatableProfessor.setDob(form.getDob());
            updatableProfessor.setFiscalCode(new FiscalCode(form.getFiscalCode()));
            updatableProfessor.setPhone(form.getPhone());
            updatableProfessor.setAddress(new Address(form.getStreet(), form.getCity(), form.getState(), form.getZip()));


            // save
            professorRepository.saveAndFlush(updatableProfessor);
            return ProfessorMapper.toDto(updatableProfessor);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException("Error accessing database for user " + form.getUsername() + ": " + e.getMessage(), e);
        }
    }


}
