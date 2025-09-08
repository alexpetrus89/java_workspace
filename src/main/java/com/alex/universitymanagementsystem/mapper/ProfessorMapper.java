package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.entity.Professor;

public class ProfessorMapper {

    private ProfessorMapper() {} // private constructor to prevent instantiation

    public static Professor toEntity(ProfessorDto dto) {
        if(dto == null) return null;
        return new Professor(
            dto.getUsername(),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getFiscalCode(),
            dto.getUniqueCode()

        );
    }

    public static ProfessorDto toDto(Professor professor) {
        if(professor == null) return null;
        return new ProfessorDto(
            professor.getUsername(),
            professor.getFirstName(),
            professor.getLastName(),
            professor.getFiscalCode().toString(),
            professor.getUniqueCode().toString()
        );
    }

}
