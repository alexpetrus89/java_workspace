package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.service.ProfessorService;

import jakarta.transaction.Transactional;

// business logic
@Service
public class ProfessorServiceImpl implements ProfessorService {

    // logger
	private static final Logger logger = LoggerFactory.getLogger(ProfessorServiceImpl.class);

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final ProfessorRepository professorRepository;

    // autowired - dependency injection - constructor
    public ProfessorServiceImpl(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }


    /**
     * Retrieves all professors.
     * @return List of ProfessorDto objects containing all professors' data.
     */
    @Override
    public List<ProfessorDto> getProfessors() {
        return professorRepository
            .findAll()
            .stream()
            .map(ProfessorMapper::mapToProfessorDto)
            .toList();
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return ProfessorDto object containing the professor's data
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is blank
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Override
    public ProfessorDto getProfessorByUniqueCode(@NonNull UniqueCode uniqueCode)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        // sanity check
        if(uniqueCode.toString().isBlank())
            throw new IllegalArgumentException("Unique Code cannot be null or empty");

        return ProfessorMapper.mapToProfessorDto(professorRepository.findByUniqueCode(uniqueCode));
    }


    /**
     * Retrieves a professor by fiscal code.
     * @param fiscalCode the fiscal code of the professor to retrieve.
     * @return ProfessorDto object containing the professor's data.
     * @throws NullPointerException if the fiscal code is null
     * @throws IllegalArgumentException if the fiscal code is blank
     * @throws UnsupportedOperationException if the fiscal code is not unique
     */
    @Override
    public ProfessorDto getProfessorByFiscalCode(@NonNull String fiscalCode)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        if(fiscalCode.isBlank())
            throw new IllegalArgumentException("Fiscal Code cannot be null or empty");
        return ProfessorMapper.mapToProfessorDto(professorRepository.findByFiscalCode(new FiscalCode(fiscalCode)));
    }


    /**
     * Retrieves a professor by name.
     * @param name the name of the professor.
     * @return List<ProfessorDto> object containing the professor's data.
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is blank
     */
    @Override
    public List<ProfessorDto> getProfessorByName(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        if(name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or empty");
        return professorRepository
            .findByFullname(name)
            .stream()
            .map(ProfessorMapper::mapToProfessorDto)
            .toList();
    }


    /**
     * Adds a new professor to the repository.
     *
     * @param professor the professor to add
     * @throws NullPointerException if the professor is null
     * @throws IllegalArgumentException if the unique code or fiscal
     *         code is Blank
     * @throws ObjectAlreadyExistsException if a professor with the
     *         same unique code already exists in the repository.
     * @throws UnsupportedOperationException if the unique code or
     *         fiscal code is not unique
     */
    @Override
    @Transactional
    public void addNewProfessor(@NonNull Professor professor)
        throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException, UnsupportedOperationException
    {
        try {
            UniqueCode uniqueCode = professor.getUniqueCode();
            FiscalCode fiscalCode = professor.getFiscalCode();

            if(uniqueCode == null || uniqueCode.toString().isBlank())
                throw new IllegalArgumentException("Unique Code cannot be null or empty");

            if(fiscalCode == null || fiscalCode.toString().isBlank())
                throw new IllegalArgumentException("Fiscal Code cannot be null or empty");

            if(professorRepository.existsByUniqueCode(uniqueCode))
                throw new ObjectAlreadyExistsException(uniqueCode);

            if(professorRepository.existsByFiscalCode(fiscalCode))
                throw new ObjectAlreadyExistsException(fiscalCode);

            professorRepository.saveAndFlush(professor);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR + " while adding new professor " + professor.getFullname(), e);
        }
    }


    /**
     * Updates an existing professor's information.
     * @param newProfessorDto the data transfer object containing the new details
     *        of the professor to be updated.
     * @throws NullPointerException if the ProfessorDto is null
     * @throws ObjectNotFoundException if no professor with the given unique code
     *         exists in the repository.
     * @throws IllegalArgumentException if the new username is blank
     *         or if the new full name is null or if the fiscal code is blank or
     *         not 16 alphanumeric characters or if the regular expression's syntax
     *         is invalid
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     */
    @Override
    @Transactional
    public void updateProfessor(@NonNull ProfessorDto newProfessorDto)
        throws NullPointerException, ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException, PatternSyntaxException
    {

        try {
            Professor updatableProfessor = professorRepository.findByUniqueCode(newProfessorDto.getUniqueCode());
            Professor newProfessor = ProfessorMapper.mapToProfessor(newProfessorDto);

            if(updatableProfessor == null)
                throw new ObjectNotFoundException(newProfessorDto.getUniqueCode());

            // retrieve new data
            FiscalCode newFiscalCode = newProfessor.getFiscalCode();
            String newName = newProfessor.getFullname();
            String newUsername = newProfessor.getUsername();

            // sanity check
            if(newFiscalCode.toString().isBlank() || newFiscalCode.toString().length() != 16 || !newFiscalCode.toString().matches("\\w{16}"))
                throw new IllegalArgumentException("Fiscal Code must be 16 alphanumeric characters");
            if(newName == null || newName.isBlank())
                throw new IllegalArgumentException("Name cannot be null or empty");
            if(newUsername == null || newUsername.isBlank())
                throw new IllegalArgumentException("Username cannot be null or empty");

            // update
            updatableProfessor.setFiscalCode(newFiscalCode);
            updatableProfessor.setFullname(newName);
            updatableProfessor.setUsername(newUsername);

            // save
            professorRepository.saveAndFlush(updatableProfessor);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR + " while updating professor with unique code " + newProfessorDto.getUniqueCode(), e);
        }
    }


}
