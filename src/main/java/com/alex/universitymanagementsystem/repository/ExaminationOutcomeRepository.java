package com.alex.universitymanagementsystem.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;

import jakarta.persistence.PersistenceException;

public interface ExaminationOutcomeRepository
    extends JpaRepository<ExaminationOutcome, Long>{

    /**
     * Find an examination outcome by student register
     * @param courseId of the course
     * @param register of the student
     * @return ExaminationOutcome
     * @throws NullPointerException if course name is nulla or register is null
     * @throws PersistenceException persistence error
     */
    @Query("SELECT eo FROM ExaminationOutcome eo " +
            "WHERE eo.examinationAppeal.course = :course " +
            "AND eo.register = :register")
    ExaminationOutcome findByCourseAndRegister(@NonNull @Param("course") Course course, @NonNull @Param("register") String register);


    /**
     * Find examination outcomes by student register
     * @param register
     * @return List of examination outcomes
     * @throws NullPointerException if register is null
     * @throws PersistenceException persistence error
     */
    List<ExaminationOutcome> findByRegister(@NonNull String register);


    /**
     * Find examination outcomes with date less of assigned value
     * @param date
     * @return List of examination outcomes
     * @throws NullPointerException if date is null
     * @throws PersistenceException persistence error
     */
    List<ExaminationOutcome> findByDateLessThan(@NonNull LocalDate date);

}
