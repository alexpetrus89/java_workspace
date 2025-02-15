package com.alex.studentmanagementsystem.service.implementation;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.domain.Professor;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.ProfessorMapper;
import com.alex.studentmanagementsystem.repository.ProfessorRepository;
import com.alex.studentmanagementsystem.service.ProfessorService;

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
     *
     * @throws NullPointerException
     */
    @Override
    public ProfessorDto getProfessorByFiscalCode(String fiscalCode)
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
     * @throws NullPointerException if the unique code is null
     */
    @Override
    public ProfessorDto getProfessorByUniqueCode(UniqueCode uniqueCode)
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
     * @param String name the name of the professor.
     * @return ProfessorDto object containing the professor's data.
     * @throws ObjectNotFoundException if no professor with the given name exists.
     * @throws NullPointerException if the name is null.
     */
    @Override
    public ProfessorDto getProfessorByName(String name)
        throws ObjectNotFoundException
    {
        return professorRepository
            .findByName(name)
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
     */
    @Override
    @Transactional
    public void addNewProfessor(ProfessorDto professorDto)
        throws ObjectAlreadyExistsException
    {
        if(professorRepository.existsByUniqueCode(professorDto.getUniqueCode()))
            throw new ObjectAlreadyExistsException(professorDto.getUniqueCode());

        if(professorRepository.existsByFiscalCode(professorDto.getFiscalCode()))
            throw new ObjectAlreadyExistsException(professorDto.getFiscalCode(), EXCEPTION_FISCAL_CODE_IDENTIFIER);

        professorRepository.saveAndFlush(ProfessorMapper.mapToProfessor(professorDto));
    }


    /**
     * Updates an existing professor's information.
     * @param newProfessorDto the data transfer object containing the new details
     *                        of the professor to be updated.
     * @throws ObjectNotFoundException if no professor with the given unique code
     *                                 exists in the repository.
     * @throws NullPointerException if the newProfessorDto is null.
     * @throws IllegalArgumentException if the given register is null or empty.
     */
    @Override
    @Transactional
    public void updateProfessor(ProfessorDto newProfessorDto)
        throws ObjectNotFoundException
    {

        Professor updatableProfessor = professorRepository
            .findByUniqueCode(newProfessorDto.getUniqueCode())
            .orElseThrow(() -> new ObjectNotFoundException(newProfessorDto.getUniqueCode()));

        Professor newProfessor = ProfessorMapper.mapToProfessor(newProfessorDto);

        // new fiscal code, name and email
        String newFiscalCode = newProfessor.getFiscalCode();
        String newName = newProfessor.getName();
        String newEmail = newProfessor.getEmail();

        if(newFiscalCode != null &&
            newFiscalCode.length() == 16 &&
                newFiscalCode.matches("\\w{16}"))
            updatableProfessor.setFiscalCode(newFiscalCode);
        if(newName != null && !newName.isEmpty())
            updatableProfessor.setName(newName);
		if(newEmail != null && !newEmail.isEmpty())
            updatableProfessor.setEmail(newEmail);

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
