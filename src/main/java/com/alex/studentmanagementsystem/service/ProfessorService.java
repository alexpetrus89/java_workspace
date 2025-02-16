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
     * @param uniqueCode the unique code of the professor to retrieve
     * @return ProfessorDto
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                 code exists
     * @throws IllegalArgumentException if the unique code is empty or null
     */
    ProfessorDto getProfessorByUniqueCode(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException;


    /**
     * get professor by fiscal code
     * @param fiscalCode the fiscal code of the professor
     * @return ProfessorDto
     * @throws ObjectNotFoundException if no professor with the given fiscal
     *                                 code exists
     * @throws IllegalArgumentException if the fiscal code is empty or null
     */
    ProfessorDto getProfessorByFiscalCode(@NonNull String fiscalCode)
        throws ObjectNotFoundException;


    /**
     * get professor by name
     * @param name the name of the professor
     * @return ProfessorDto
     * @throws ObjectNotFoundException if no professor with the given name exists
     * @throws IllegalArgumentException if the name is empty or null
     */
    ProfessorDto getProfessorByName(@NonNull String name)
        throws ObjectNotFoundException;


    /**
     * add new professor
     * @param professorDto the data transfer object containing the details
     *                     of the professor to be added
     * @throws ObjectAlreadyExistsException if a professor with the same unique
     *                                      code already exists
     * @throws IllegalArgumentException if the unique code is empty or null
     */
    @Transactional
    void addNewProfessor(@NonNull ProfessorDto professorDto)
        throws ObjectAlreadyExistsException;


    /**
     * update professor
     * @param professorDto the data transfer object containing the new
     *                     details of the professor to be updated
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                 code exists
     * @throws IllegalArgumentException if the unique code is empty or null
     */
    @Transactional
    void updateProfessor(@NonNull ProfessorDto professorDto)
        throws ObjectNotFoundException;


    /**
     * delete professor
     * @param uniqueCode the unique code of the professor to delete
     * @throws ObjectNotFoundException if no professor with the given unique
     *                                 code exists
     * @throws IllegalArgumentException if the unique code is empty or null
     */
	@Transactional
    void deleteProfessor(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException;

}
