package com.alex.universitymanagementsystem.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.StudentDto;

public final class ExaminationAppealMapper {

    private ExaminationAppealMapper() {}

    public static ExaminationAppealDto toDto(ExaminationAppeal appeal, Set<StudentDto> students) {
        if (appeal == null) return null;

        ExaminationAppealDto dto = new ExaminationAppealDto();
        dto.setId(appeal.getId());
        dto.setCourse(appeal.getCourse().getName());
        dto.setDegreeCourse(appeal.getCourse().getDegreeCourse().getName());
        dto.setCourseCfu(appeal.getCourse().getCfu().toString());
        dto.setProfessorCode(appeal.getProfessor().toString());
        dto.setDescription(appeal.getDescription());
        dto.setDate(appeal.getDate());
        dto.setStudents(students);

        return dto;
    }

    public static ExaminationAppeal toEntity(ExaminationAppealDto dto, Course course) {
        if (dto == null || course == null) return null;
        ExaminationAppeal appeal = ExaminationAppeal.of(course, dto.getDescription(), dto.getDate());

        // Set ID if present (for update scenarios)
        if (dto.getId() != null)
            appeal.setId(dto.getId());

        //  to Register objects
        if (dto.getStudents() != null) {
            Set<Register> registers = dto
                .getStudents()
                .stream()
                .map(StudentDto::getRegister)
                .map(Register::new)
                .collect(Collectors.toSet());
            appeal.setRegisters(registers);
        }

        return appeal;
    }
}

