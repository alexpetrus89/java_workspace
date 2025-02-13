package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;

public interface ProfessorService {

    /**
     * get all professors
     * @return List<ProfessorDto>
     */
    List<ProfessorDto> getProfessors();

    /**
     * get professor by unique code
     * @param UniqueCode uniqueCode
     * @return ProfessorDto object
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                 code exists
     */
    ProfessorDto getProfessorByUniqueCode(UniqueCode uniqueCode)
        throws ObjectNotFoundException;

    /**
     * get professor by fiscal code
     * @param fiscalCode
     * @return ProfessorDto object
     * @throws ObjectNotFoundException if no professor with the given fiscal
     *                                 code exists
     */
    ProfessorDto getProfessorByFiscalCode(String fiscalCode)
        throws ObjectNotFoundException;

    /**
     * get professor by name
     * @param name
     * @return ProfessorDto object
     * @throws ObjectNotFoundException if no professor with the given name exists
     */
    ProfessorDto getProfessorByName(String name)
        throws ObjectNotFoundException;

    /**
     * add new professor
     * @param ProfessorDto professorDto
     * @throws ObjectAlreadyExistsException if a professor with the same unique
     *                                      code already exists
     */
    @Transactional
    void addNewProfessor(ProfessorDto professorDto)
        throws ObjectAlreadyExistsException;

    /**
     * update professor
     * @param ProfessorDto professorDto
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                 code exists
     */
    @Transactional
    void updateProfessor(ProfessorDto professorDto)
        throws ObjectNotFoundException;

    /**
     * delete professor
     * @param UniqueCode uniqueCode
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                 code exists
     */
	@Transactional
    void deleteProfessor(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException;

}
