package com.alex.studentmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.studentmanagementsystem.domain.Examination;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ExaminationDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;


public interface ExaminationService {

    /**
     * Get all examinations
     * @return List<ExaminationDto>
     */
    List<ExaminationDto> getExaminations();


    /**
     * Get all examinations by student register
     * @param register register of the student
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the student does not exist
     * @throws IllegalArgumentException if the register is null or empty
     */
    List<ExaminationDto> getExaminationsByStudentRegister(@NonNull Register register)
        throws ObjectNotFoundException;


    /**
     * Get all examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws IllegalArgumentException if the unique code is null or empty
     */
    List<ExaminationDto> getExaminationsByProfessorUniqueCode(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException;


    /**
     * Get all examinations by course id
     * @param CourseId courseId of the course
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the course does not exist
     * @throws IllegalArgumentException if the courseId is null
     */
    List<ExaminationDto> getExaminationsByCourseId(@NonNull CourseId courseId)
        throws ObjectNotFoundException;


    /**
     * Get all examinations by course name
     * @param name name of the course
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the course does not exist
     * @throws IllegalArgumentException if the name is null or empty
     */
    List<ExaminationDto> getExaminationsByCourseName(@NonNull String name)
        throws ObjectNotFoundException;


    /**
     * Add a new examination for a student.
     * @param register the student's registration details
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @param grade the grade obtained in the examination
     * @param withHonors whether the examination was passed with honors
     * @param date the date of the examination
     * @return ExaminationDto containing the details of the newly added examination
     * @throws ObjectNotFoundException if the student or course does not exist
     * @throws ObjectAlreadyExistsException if the examination already exists
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match
     */
    @NonNull
    Examination addNewExamination(
        Register register,
        String courseName,
        String degreeCourseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectAlreadyExistsException, ObjectNotFoundException;


    /**
     * Update an existing examination
     * @param oldRegister the old student's registration number
     * @param oldCourseName the old course name
     * @param oldDegreeCourseName the old degree course name
     * @param newRegister the new student's registration number
     * @param newCourseName the new course name
     * @param newDegreeCourseName the new degree course name
     * @param grade the new grade obtained in the examination
     * @param withHonors whether the examination was passed with honors
     * @param date the new date of the examination
     * @return Examination containing the details of the updated examination
     * @throws ObjectNotFoundException if the student or course does not exist
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match
     */
    @NonNull
    Examination updateExamination(
        Register oldRegister,
        String oldCourseName,
        String oldDegreeCourseName,
        Register newRegister,
        String newCourseName,
        String newDegreeCourseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectNotFoundException;


    /**
     * Delete an existing examination
     * @param register the student's registration number
     * @param courseName the course name
     * @throws ObjectNotFoundException if the examination does not exist
     * @throws IllegalArgumentException if the course name is null or empty
     *                                  or the register is null or empty
     */
    void deleteExamination(@NonNull Register register, @NonNull String courseName)
        throws ObjectNotFoundException;
}
