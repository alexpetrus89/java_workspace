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
public class ProfessorServiceImplementation
    implements ProfessorService
{
    // constants
    private static final String EXCEPTION_FISCAL_CODE_IDENTIFIER = "professor_fiscal_code";
    private static final String EXCEPTION_NAME_IDENTIFIER = "professor_name";
    // instance variables
    private final ProfessorRepository professorRepository;

    // autowired - dependency injection - constructor
    public ProfessorServiceImplementation(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    /**
     * @return List<ProfessorDto>
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
     * @param String fiscalCode
     * @return ProfessorDto
     * @throws ObjectNotFoundException
     */
    @Override
    public ProfessorDto getProfessorByFiscalCode(String fiscalCode)
        throws ObjectNotFoundException
    {

        Professor professor = professorRepository
            .findByFiscalCode(fiscalCode)
            // throws exception
            .orElseThrow(() -> new ObjectNotFoundException(fiscalCode, EXCEPTION_FISCAL_CODE_IDENTIFIER));

        return ProfessorMapper.mapToProfessorDto(professor);
    }


    /**
     * @param UniqueCode uniqueCode
     * @return ProfessorDto
     * @throws ObjectNotFoundException
     */
    @Override
    public ProfessorDto getProfessorByUniqueCode(UniqueCode uniqueCode)
        throws ObjectNotFoundException
    {

        Professor professor = professorRepository
            .findByUniqueCode(uniqueCode)
            // throws exception
            .orElseThrow(() -> new ObjectNotFoundException(uniqueCode));

        return ProfessorMapper.mapToProfessorDto(professor);
    }

    /**
     * @param String name
     * @return ProfessorDto
     * @throws ObjectNotFoundException
     */
    @Override
    public ProfessorDto getProfessorByName(String name)
        throws ObjectNotFoundException
    {

        Professor professor = professorRepository
            .findByName(name)
            // throws exception
            .orElseThrow(() -> new ObjectNotFoundException(name, EXCEPTION_NAME_IDENTIFIER));

        return ProfessorMapper.mapToProfessorDto(professor);
    }



    /**
     * @param ProfessorDto professorDto
     * @throws ObjectAlreadyExistsException
     */
    @Override
    @Transactional
    public void addNewProfessor(ProfessorDto professorDto)
        throws ObjectAlreadyExistsException
    {

        if(professorRepository.existsByUniqueCode(professorDto.getUniqueCode()))
            throw new ObjectAlreadyExistsException(professorDto.getUniqueCode());

        professorRepository.save(ProfessorMapper.mapToProfessor(professorDto));
    }


    /**
     * @param ProfessorDto newProfessorDto
     * @throws ObjectNotFoundException
     */
    @Override
    @Transactional
    public void updateProfessor(ProfessorDto newProfessorDto) {

        Professor updatableProfessor = professorRepository
            .findByUniqueCode(newProfessorDto.getUniqueCode())
            // throws exception
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
     * @param UniqueCode uniqueCode
     * @throws ObjectNotFoundException
     */
    @Override
    @Transactional
    public void deleteProfessor(@NonNull UniqueCode uniqueCode) {
        professorRepository
            .findByUniqueCode(uniqueCode)
            .ifPresent(
                professor -> professorRepository
                    .deleteByUniqueCode(professor.getUniqueCode())
            );
    }


}
