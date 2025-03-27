package com.alex.universitymanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.Register;


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
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     */
    List<ExaminationAppeal> getExaminationAppealByStudent(@NonNull Register register);

    /**
     * Adds a new examination appeal
     * @param courseName
     * @param degreeCourseName
     * @param professor
     * @param description
     * @param date
     * @return examinationAppeal
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     * @throws IllegalStateException
     */
    ExaminationAppeal addNewExaminationAppeal(String courseName, String degreeCourseName, Professor professor, String description, LocalDate date);

    /**
     * Adds a student to an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     */
    ExaminationAppeal bookExaminationAppeal(@NonNull Long id, @NonNull Register register);

    /**
     * Removes a student from an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     * @throws IllegalStateException
     */
    void deleteBookedExaminationAppeal(@NonNull Long id, @NonNull Register register);

}
