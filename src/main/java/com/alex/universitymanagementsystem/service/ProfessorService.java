package com.alex.universitymanagementsystem.service;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;

public interface ProfessorService {

    /**
     * get all professors
     * @return List<ProfessorDto>
     */
    List<ProfessorDto> getProfessors();


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return ProfessorDto object containing the professor's data
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is blank
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    ProfessorDto getProfessorByUniqueCode(@NonNull UniqueCode uniqueCode)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Retrieves a professor by fiscal code.
     * @param fiscalCode the fiscal code of the professor to retrieve.
     * @return ProfessorDto object containing the professor's data.
     * @throws NullPointerException if the fiscal code is null
     * @throws IllegalArgumentException if the fiscal code is blank
     * @throws UnsupportedOperationException if the fiscal code is not unique
     */
    ProfessorDto getProfessorByFiscalCode(@NonNull String fiscalCode)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Retrieves a professor by name.
     * @param name the name of the professor.
     * @return ProfessorDto object containing the professor's data.
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is blank
     * @throws UnsupportedOperationException if the name is not unique
     */
    ProfessorDto getProfessorByName(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Adds a new professor to the repository.
     *
     * @param professor the professor to add
     * @throws NullPointerException if the professor is null
     * @throws IllegalArgumentException if the unique code or fiscal
     *         code is empty
     * @throws ObjectAlreadyExistsException if a professor with the
     *         same unique code already exists in the repository.
     * @throws UnsupportedOperationException if the unique code or
     *         fiscal code is not unique
     */
    @Transactional
    void addNewProfessor(@NonNull Professor professor)
        throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException, UnsupportedOperationException;


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
    @Transactional
    void updateProfessor(@NonNull ProfessorDto newProfessorDto)
        throws NullPointerException, ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException, PatternSyntaxException;


    /**
     * Deletes a professor by unique code.
     * If the professor with the given unique code exists, it will be
     * removed from the repository.
     *
     * @param uniqueCode the unique code of the professor to delete
     * @throws ObjectNotFoundException if no professor with the given
     *         unique code is found
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is blank
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Transactional
    void deleteProfessor(@NonNull UniqueCode uniqueCode)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException;

}
