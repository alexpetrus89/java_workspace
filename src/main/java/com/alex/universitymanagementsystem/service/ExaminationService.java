package com.alex.universitymanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;




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
     * @throws NullPointerException if the register is null
     * @throws IllegalArgumentException if the register is blank
     * @throws ObjectNotFoundException if the student does not exist
     * @throws UnsupportedOperationException if the register is not unique
     */
    List<ExaminationDto> getExaminationsByStudentRegister(@NonNull Register register)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException;


    /**
     * Get all examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return List<ExaminationDto>
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is blank
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    List<ExaminationDto> getExaminationsByProfessorUniqueCode(@NonNull UniqueCode uniqueCode)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException;


    /**
     * Get all examinations by course id
     * @param courseId course id of the course
     * @return List<ExaminationDto>
     * @throws NullPointerException if the courseId is null
     * @throws ObjectNotFoundException if the course does not exist
     * @throws IllegalArgumentException if the courseId is Blank
     * @throws UnsupportedOperationException if the courseId is not unique
     */
    List<ExaminationDto> getExaminationsByCourseId(@NonNull CourseId courseId)
        throws NullPointerException, ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Get all examinations by course name
     * @param name name of the course
     * @return List<ExaminationDto>
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is blank
     * @throws ObjectNotFoundException if the course does not exist
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    List<ExaminationDto> getExaminationsByCourseName(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException;


    /**
     * Add new examination
     * @param register register of the student
     * @param courseName name of the course
     * @param degreeCourseName name of the degree course
     * @param grade grade of the examination
     * @param withHonors whether the examination was passed with honors
     * @param date date of the examination
     * @return Examination
     * @throws NullPointerException if the unique code is null or the course name is null
     * @throws IllegalArgumentException if the unique code is blank or the course name is
     *         blank or the degree course name is blank or if grade is not between 0 and 30
     *         or if the date is in the past or if the student or course does not exist
     * @throws ObjectAlreadyExistsException if the examination already exists.
     * @throws ObjectNotFoundException if the student or course does not exist.
     * @throws UnsupportedOperationException if the unique code is not unique
     *         or if the course name is not unique
     */
	@Transactional
    Examination addNewExamination(
        @NonNull Register register,
        @NonNull String courseName,
        @NonNull String degreeCourseName,
        int grade,
        boolean withHonors,
        @NonNull LocalDate date
    ) throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException, ObjectNotFoundException;


    /**
     * Update existing examination
     * @param oldRegister the old student's register
     * @param oldCourseName the old course name
     * @param oldDegreeCourseName the old degree course name
     * @param newRegister the new student's register
     * @param newCourseName the new course name
     * @param newDegreeCourseName the new degree course name
     * @param grade the new grade
     * @param withHonors whether the examination was passed with honors
     * @param date the new date
     * @return Examination
     * @throws NullPointerException if any of the parameters is null
     * @throws ObjectNotFoundException if the student or course or degree course
     *         or examination to update does not exist.
     * @throws IllegalArgumentException if any of the parameters is blank or
     *         if the grade is not between 0 and 30 or if the date is in the
     *         past
     * @throws UnsupportedOperationException if the unique code is not unique
     */
	@Transactional
    Examination updateExamination(
        @NonNull Register oldRegister,
        @NonNull String oldCourseName,
        @NonNull String oldDegreeCourseName,
        @NonNull Register newRegister,
        @NonNull String newCourseName,
        @NonNull String newDegreeCourseName,
        int grade,
        boolean withHonors,
        @NonNull LocalDate date
    ) throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException;


    /**
     * Delete existing examination
     * @param register register of the student
     * @param name name of the course
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the course name is null or empty
     *         or the register is null or empty
     * @throws ObjectNotFoundException if the examination does not exist
     * @throws UnsupportedOperationException if the register is not unique
     *         or if the course name is not unique
     */
	@Transactional
	void deleteExamination(@NonNull Register register, @NonNull String name)
		throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException;
}
