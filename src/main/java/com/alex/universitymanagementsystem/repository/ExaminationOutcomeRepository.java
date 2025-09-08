package com.alex.universitymanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alex.universitymanagementsystem.entity.Course;
import com.alex.universitymanagementsystem.entity.ExaminationOutcome;

import jakarta.persistence.PersistenceException;

public interface ExaminationOutcomeRepository
    extends JpaRepository<ExaminationOutcome, Long>
{


    /**
     * Find an examination outcome by course and student register
     * @param course course related to the examination
     * @param register of the student
     * @return ExaminationOutcome
     * @throws PersistenceException persistence error
     */
    Optional<ExaminationOutcome> findByAppeal_CourseAndRegister(Course course, String register);


    /**
     * Find examination outcomes by student register
     * @param register
     * @return List of examination outcomes
     * @throws PersistenceException persistence error
     */
    List<ExaminationOutcome> findByRegister(String register);


    /**
     * Find examination outcomes with date less than the assigned value
     * @param date
     * @return List of examination outcomes
     * @throws PersistenceException persistence error
     */
    List<ExaminationOutcome> findByAppeal_DateBefore(LocalDate date);


    /**
     * Check if an examination outcome exists by there appeal id and student register
     * @param id of the appeal
     * @param register of the student
     * @return boolean
     * @throws PersistenceException persistence error
     */
    boolean existsByIdAndRegister(Long id, String register);

}

