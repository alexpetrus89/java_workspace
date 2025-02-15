package com.alex.studentmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.studentmanagementsystem.domain.Examination;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ExaminationDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;


public interface ExaminationService {

    /**
     * Get all examinations
     * @return List<ExaminationDto>
     */
    List<ExaminationDto> getExaminations();

    /**
     * Get all examinations by student register
     * @param Register register
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the student does not exist
     */
    List<ExaminationDto> getExaminationsByStudentRegister(@NonNull Register register)
        throws ObjectNotFoundException;

    /**
     * Get all examinations by professor unique code
     * @param UniqueCode uniqueCode
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the professor does not exist
     */
    List<ExaminationDto> getExaminationsByProfessorUniqueCode(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException;

    /**
     * Get all examinations by course id
     * @param CourseId courseId
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the course does not exist
     */
    List<ExaminationDto> getExaminationsByCourseId(@NonNull CourseId courseId)
        throws ObjectNotFoundException;

    /**
     * Get all examinations by course name
     * @param String name
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the course does not exist
     */
    List<ExaminationDto> getExaminationsByCourseName(@NonNull String name)
        throws ObjectNotFoundException;


    /**
     * Add a new examination for a student.
     * @param registration the student's registration details
     * @param courseName the name of the course
     * @param grade the grade obtained in the examination
     * @param withHonors whether the examination was passed with honors
     * @param date the date of the examination
     * @return ExaminationDto containing the details of the newly added examination
     * @throws ObjectNotFoundException if the student or course does not exist
     */
    Examination addNewExamination(
        Register registration,
        String courseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectNotFoundException;

    /**
     * Update an existing examination for a student.
     * @param registration the student's registration details
     * @param courseName the name of the course
     * @param grade the grade obtained in the examination
     * @param withHonors whether the examination was passed with honors
     * @param date the date of the examination
     * @return Examination containing the details of the updated examination
     * @throws ObjectNotFoundException if the student or course does not exist
     */
    Examination updateExamination(
        Register oldRegistration,
        String oldCourseName,
        Register newRegistration,
        String newCourseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectNotFoundException;


    /**
     * Delete an existing examination for a student.
     * @param ExaminationDto examination
     * @throws ObjectNotFoundException if the student or course does not exist
     */
    void deleteExamination(@NonNull Register register, @NonNull String courseName)
        throws ObjectNotFoundException;
}
