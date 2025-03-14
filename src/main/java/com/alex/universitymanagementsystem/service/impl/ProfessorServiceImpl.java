package com.alex.universitymanagementsystem.service.impl;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Professor;
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

    // constants
    private static final String EXCEPTION_FISCAL_CODE_IDENTIFIER = "professor_fiscal_code";
    private static final String EXCEPTION_NAME_IDENTIFIER = "professor_name";
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
     * Retrieves a professor by fiscal code.
     * @param fiscalCode the fiscal code of the professor to retrieve.
     * @return ProfessorDto object containing the professor's data.
     * @throws ObjectNotFoundException if no professor with the given fiscal
     *                                 code exists.
     * @throws IllegalArgumentException if the fiscal code is empty
     * @throws UnsupportedOperationException if the fiscal code is not unique
     */
    @Override
    public ProfessorDto getProfessorByFiscalCode(@NonNull String fiscalCode)
        throws ObjectNotFoundException
    {
        return ProfessorMapper.mapToProfessorDto(
            professorRepository
                .findByFiscalCode(fiscalCode)
                .orElseThrow(() -> new ObjectNotFoundException(fiscalCode, EXCEPTION_FISCAL_CODE_IDENTIFIER))
        );
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return ProfessorDto
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                  code is found
     * @throws IllegalArgumentException if the unique code is empty
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Override
    public ProfessorDto getProfessorByUniqueCode(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException
    {
        return ProfessorMapper.mapToProfessorDto(
            professorRepository
                .findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ObjectNotFoundException(uniqueCode))
        );
    }


    /**
     * Retrieves a professor by name.
     * @param name the name of the professor.
     * @return ProfessorDto object containing the professor's data.
     * @throws ObjectNotFoundException if no professor with the given name exists.
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Override
    public ProfessorDto getProfessorByName(@NonNull String name)
        throws ObjectNotFoundException
    {
        return professorRepository
            .findByFullname(name)
            .map(ProfessorMapper::mapToProfessorDto)
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_NAME_IDENTIFIER));
    }


    /**
     * Adds a new professor to the repository.
     *
     * @param professorDto the professor data transfer object containing
     *                     the details of the professor to be added.
     * @throws ObjectAlreadyExistsException if a professor with the same
     *                                      unique code already exists in the
     *                                      repository.
     * @throws IllegalArgumentException if the unique code or fiscal code
     *                                  is empty
     * @throws UnsupportedOperationException if the unique code or fiscal
     *                                       code is not unique
     */
    @Override
    @Transactional
    public void addNewProfessor(@NonNull ProfessorDto professorDto)
        throws ObjectAlreadyExistsException
    {
        UniqueCode uniqueCode = professorDto.getUniqueCode();
        String fiscalCode = professorDto.getFiscalCode();

        if(uniqueCode == null || uniqueCode.toString().isEmpty())
            throw new IllegalArgumentException("Unique Code cannot be null or empty");

        if(fiscalCode == null || fiscalCode.isEmpty())
            throw new IllegalArgumentException("Fiscal Code cannot be null or empty");

        if(professorRepository.existsByUniqueCode(uniqueCode))
            throw new ObjectAlreadyExistsException(uniqueCode);

        if(professorRepository.existsByFiscalCode(fiscalCode))
            throw new ObjectAlreadyExistsException(fiscalCode, EXCEPTION_FISCAL_CODE_IDENTIFIER);

        professorRepository.saveAndFlush(ProfessorMapper.mapToProfessor(professorDto));
    }


    /**
     * Updates an existing professor's information.
     * @param newProfessorDto the data transfer object containing the new details
     *                        of the professor to be updated.
     * @throws ObjectNotFoundException if no professor with the given unique code
     *                                 exists in the repository.
     * @throws IllegalArgumentException if the given register is null or empty.
     * @throws UnsupportedOperationException if the unique code is not
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     */
    @Override
    @Transactional
    public void updateProfessor(@NonNull ProfessorDto newProfessorDto)
        throws ObjectNotFoundException
    {

        Professor updatableProfessor = professorRepository
            .findByUniqueCode(newProfessorDto.getUniqueCode())
            .orElseThrow(() -> new ObjectNotFoundException(newProfessorDto.getUniqueCode()));

        Professor newProfessor = ProfessorMapper.mapToProfessor(newProfessorDto);

        // new fiscal code, name and email
        String newFiscalCode = newProfessor.getFiscalCode();
        String newName = newProfessor.getFullname();
        String newEmail = newProfessor.getEmail();

        // update
        if(newFiscalCode != null &&
            newFiscalCode.length() == 16 &&
                newFiscalCode.matches("\\w{16}"))
            updatableProfessor.setFiscalCode(newFiscalCode);
        if(newName != null && !newName.isEmpty())
            updatableProfessor.setFullname(newName);
		if(newEmail != null && !newEmail.isEmpty())
            updatableProfessor.setEmail(newEmail);

        // save
        professorRepository.saveAndFlush(updatableProfessor);
    }


    /**
     * Deletes a professor by unique code.
     * If the professor with the given unique code exists, it will be
     * removed from the repository.
     *
     * @param uniqueCode the unique code of the professor to delete
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                 code is found
     * @throws IllegalArgumentException if the unique code is empty or null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     */
    @Override
    @Transactional
    public void deleteProfessor(@NonNull UniqueCode uniqueCode) {
        professorRepository
            .findByUniqueCode(uniqueCode)
            .ifPresent(professor ->
                professorRepository
                    .deleteByUniqueCode(professor.getUniqueCode())
            );
    }


}
