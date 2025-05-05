package com.alex.universitymanagementsystem.service;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;

public interface ExaminationOutcomeService {

    /**
     * Get an examination outcome by student register and course
     * @param course name of the course
     * @param register of the student
     * @param LocalDate date of the examination
     * @return ExaminationOutcome outcome
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if the register is not unique
     */
    ExaminationOutcome getOutcomeByCourseAndStudent(@NonNull String Course, @NonNull String register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;

    /**
     * Get a list of examination outcomes by professor unique code
     * @param course name of the course
     * @param uniqueCode of the professor owner of the examination appeal
     * @return List<ExaminationOutcome> outcomes
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    /*
    List<ExaminationOutcome> getOutcomesByCourseAndProfessor(@NonNull String course, @NonNull UniqueCode uniqueCode)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;
    */


    /**
     * Save an examination outcome
     * @param outcome examination outcome of the student
     * @return ExaminationOutcome outcome
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws ObjectAlreadyExistsException if the outcome already exists
     */
    @Transactional(rollbackOn = {NullPointerException.class, IllegalArgumentException.class, ObjectAlreadyExistsException.class})
    ExaminationOutcome addNewExaminationOutcome(@NonNull ExaminationOutcome outcome)
        throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException;


    /**
     * Delete an examination outcome
     * @param outcome examination outcome of the student
     * @return ExaminationOutcome outcome
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws UnsupportedOperationException if the outcome is not unique
     */
    @Transactional
    ExaminationOutcome deleteExaminationOutcome(@NonNull ExaminationOutcome outcome)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException;

}
