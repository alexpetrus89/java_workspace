package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;

import jakarta.transaction.Transactional;

public interface ExaminationOutcomeService {


    /**
     * retrive outcome by id
     * @param id
     * @return ExaminationOutcome
     * @throws NullPointerException if the id is null
     * @throws IllegalArgumentException if the id is invalid
     */
    public ExaminationOutcome getOutcomeById(@NonNull Long id)
        throws NullPointerException, IllegalArgumentException;


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
    public ExaminationOutcome getOutcomeByCourseAndStudent(@NonNull String name, @NonNull String register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Get an examination outcomes by student register
     * @param register of the student
     * @return List of examination outcomes
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the register is invalid or the student does not exist
     * @throws UnsupportedOperationException if the register is not unique
     */
    public List<ExaminationOutcome> getStudentOutcomes(@NonNull String register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


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
