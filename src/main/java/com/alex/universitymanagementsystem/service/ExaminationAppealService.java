package com.alex.universitymanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;


public interface ExaminationAppealService {

    /**
     * Retrieves all examination appeals
     * @return a list of examination appeals
     */
    List<ExaminationAppeal> getExaminationAppeals();

    /**
     * Retrieves all examination appeals for a student
     * @param register
     * @return a list of examination appeals
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the register is blank
     * @throws UnsupportedOperationException if the register is not unique
     */
    public List<ExaminationAppeal> getExaminationAppealByStudent(@NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;

    /**
     * Adds a new examination appeal
     * @param courseName
     * @param degreeCourseName
     * @param professor
     * @param description
     * @param date
     * @return examinationAppeal
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws ObjectNotFoundException if the degree course or professor
     *         does not exist
     * @throws UnsupportedOperationException if the register is not unique
     */
    public ExaminationAppeal addNewExaminationAppeal(
        @NonNull String courseName,
        @NonNull String degreeCourseName,
        @NonNull Professor professor,
        @NonNull String description,
        @NonNull LocalDate date
    ) throws NullPointerException, IllegalArgumentException, ObjectNotFoundException;

    /**
     * Adds a student to an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if the register is not unique
     */
    public ExaminationAppeal bookExaminationAppeal(@NonNull Long id, @NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;

    /**
     * Removes a student from an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws IllegalStateException
     */
    public void deleteBookedExaminationAppeal(@NonNull Long id, @NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException, IllegalStateException;

}
