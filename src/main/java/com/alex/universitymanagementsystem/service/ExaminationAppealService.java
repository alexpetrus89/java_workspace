package com.alex.universitymanagementsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;


public interface ExaminationAppealService {


    /**
     * Retrieves all examination appeals
     * @return a list of examination appeals
     */
    List<ExaminationAppeal> getExaminationAppeals();


    /**
     * Retrieves an examination appeal
     * @param id
     * @return examination appeal
     * @throws NullPointerException if id is null
     * @throws NoSuchElementException if the examination appeal does not exist
     */
    ExaminationAppeal getExaminationAppealById(@NonNull Long id);


    /**
     * Retrieves all examination appeals for a student
     * @param register student register
     * @return a list of examination appeals available
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the register is blank
     * @throws UnsupportedOperationException if the register is not unique
     */
    List<ExaminationAppeal> getExaminationAppealsAvailable(@NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Retrieves all examination appeals booked by a student
     * @param register student register
     * @return a list of examination appeals booked
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the register is blank
     * @throws UnsupportedOperationException if the register is not unique
     */
    List<ExaminationAppeal> getExaminationAppealsBooked(@NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Retrieves all examination appeals made by professor
     * @param uniqueCode professor unique code
     * @return a list of examination appeals
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the unique code is blank
     * @throws UnsupportedOperationException if the register is not unique
     */
    List<ExaminationAppeal> getExaminationAppealsByProfessor(@NonNull UniqueCode uniqueCode)
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
    ExaminationAppeal addNewExaminationAppeal(
        @NonNull CourseId courseId,
        @NonNull Professor professor,
        @NonNull String description,
        @NonNull LocalDate date
    ) throws NullPointerException, IllegalArgumentException, ObjectNotFoundException;


    /**
     * deletes an examination appeal
     * @param professor
     * @param id
     * @return boolean
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @throws ObjectNotFoundException
     */
    boolean deleteExaminationAppeal(@NonNull Professor professor, @NonNull Long id)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException;


    /**
     * Adds a student to an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if the register is not unique
     */
    ExaminationAppeal bookExaminationAppeal(@NonNull Long id, @NonNull Register register)
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
    void deleteBookedExaminationAppeal(@NonNull Long id, @NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException, IllegalStateException;


}
