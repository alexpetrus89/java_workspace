package com.alex.studentmanagementsystem.service;

import java.util.List;

import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ExaminationDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;


public interface ExaminationService {

    List<ExaminationDto> getExaminations();

    List<ExaminationDto> getExaminationsByStudentRegister(Register register)
        throws ObjectNotFoundException;

    List<ExaminationDto> getExaminationsByProfessorUniqueCode(UniqueCode uniqueCode)
        throws ObjectNotFoundException;

    List<ExaminationDto> getExaminationsByCourseId(CourseId courseId)
        throws ObjectNotFoundException;

    List<ExaminationDto> getExaminationsByCourseName(String courseName)
        throws ObjectNotFoundException;

}
