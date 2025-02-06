package com.alex.studentmanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;

public interface ProfessorService {

    List<ProfessorDto> getProfessors();

    ProfessorDto getProfessorByUniqueCode(UniqueCode uniqueCode)
        throws ObjectNotFoundException;

    ProfessorDto getProfessorByFiscalCode(String fiscalCode)
        throws ObjectNotFoundException;

    ProfessorDto getProfessorByName(String name)
        throws ObjectNotFoundException;

    @Transactional
    void addNewProfessor(ProfessorDto professorDto)
        throws ObjectAlreadyExistsException;

    @Transactional
    void updateProfessor(ProfessorDto professorDto)
        throws ObjectNotFoundException;

	@Transactional
    void deleteProfessor(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException;

}
