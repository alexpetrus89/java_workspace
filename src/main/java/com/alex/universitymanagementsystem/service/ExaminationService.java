package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.UpdateExaminationDto;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;




public interface ExaminationService {

    /**
     * Get all examinations
     * @return List<ExaminationDto>
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    List<ExaminationDto> getExaminations() throws DataAccessServiceException;


    /**
     * Get all examinations by course
     * @param courseName course name of the course
     * @param degreeCourseName degree course name of the course
     * @return List<ExaminationDto>
     * @throws IllegalArgumentException if the course name or degree course name is Blank
     * @throws ObjectNotFoundException if the course does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    List<ExaminationDto> getExaminationsByCourseNameAndDegreeCourseName(String courseName, String degreeCourseName)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * Get all examinations by student register
     * @param register of the student
     * @return List<ExaminationDto>
     * @throws IllegalArgumentException if the register is blank
     * @throws ObjectNotFoundException if the student does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    List<ExaminationDto> getExaminationsByStudentRegister(Register register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * Get all examinations by professor unique code
     * @param uniqueCode of the professor
     * @return List<ExaminationDto>
     * @throws IllegalArgumentException if the unique code is blank
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws DataAccessServiceException if there is an error accessing the database.
     */
    List<ExaminationDto> getExaminationsByProfessorUniqueCode(UniqueCode uniqueCode)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;


    /**
     * Add new examination
     *
     * @param request the examination data transfer object containing the details of the examination to be added
     * @return Examination data transfer object
     * @throws IllegalArgumentException for many kind of errors
     * @throws ObjectNotFoundException if any referenced entity is not found
     * @throws ObjectAlreadyExistsException if the examination already exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class, ObjectAlreadyExistsException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ExaminationDto addNewExamination(@Valid ExaminationDto request)
        throws IllegalArgumentException, ObjectNotFoundException, ObjectAlreadyExistsException, DataAccessServiceException;


    /**
     * Update existing examination
     *
     * @param request the examination data transfer object containing the details of the examination to be updated
     * @return Examination data transfer object
     * @throws IllegalArgumentException for many kind of errors
     * @throws ObjectNotFoundException if any referenced entity is not found
     * @throws IllegalStateException if the student is not part of the degree course
     * @throws DataAccessServiceException if there is an error accessing the database
    */
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class, IllegalStateException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    ExaminationDto updateExamination(@Valid UpdateExaminationDto request)
        throws IllegalArgumentException, ObjectNotFoundException, IllegalStateException, DataAccessServiceException;


    /**
     * Delete existing examination
     * @param register register of the student
     * @param courseName name of the course
     * @param degreeCourseName name of the degree course
     * @return Examination data transfer object
     * @throws IllegalArgumentException if the course name is null or empty
     *         or the register is null or empty
     * @throws ObjectNotFoundException if the examination does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
	@Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	ExaminationDto deleteExamination(String register, String courseName, String degreeCourseName)
		throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException;

}
