package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.dto.ProfessorDto;

public class ProfessorMapper {

    private ProfessorMapper() {} // private constructor to prevent instantiation

    public static Professor mapToProfessor(ProfessorDto professorDto) {
        return new Professor(
            professorDto.getUniqueCode(),
            professorDto.getFiscalCode(),
            professorDto.getName(),
            professorDto.getEmail()
        );
    }

    public static ProfessorDto mapToProfessorDto(Professor professor) {
        return new ProfessorDto(
            professor.getUniqueCode(),
            professor.getFiscalCode(),
            professor.getName(),
            professor.getEmail()
        );
    }

}
