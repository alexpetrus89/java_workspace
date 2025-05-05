package com.alex.universitymanagementsystem.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;

import jakarta.persistence.PersistenceException;

public interface ExaminationOutcomeRepository
    extends JpaRepository<ExaminationOutcome, Long>{

    /**
     * Find an examination outcome by student register
     * @param register of the student
     * @param date of the examination appeal
     * @return ExaminationOutcome
     * @throws NullPointerException if course name is nulla or register is null
     * @throws PersistenceException persistence error
     */
    /*
    @Query("SELECT eo FROM ExaminationOutcome eo "
        + "JOIN eo.examinationAppeal ea "
        + "WHERE ea.course.name = :course AND eo.register = :register")
    ExaminationOutcome findByCourseAndRegister(@NonNull @Param("course") String course, @NonNull @Param("register") Register register);
        */
    @Query("SELECT eo FROM ExaminationOutcome eo "
        + "WHERE eo.examinationAppeal.course.name = :course AND eo.register = :register")
    ExaminationOutcome findByCourseAndRegister(@NonNull @Param("course") String course, @NonNull @Param("register") String register);

    /**
     * Find examination outcomes by course and unique code
     * @param course
     * @param uniqueCode
     * @return List<ExaminationOutcome>
     */
    @Query("SELECT eo FROM ExaminationOutcome eo "
        + "WHERE ea.examinationAppeal.course.name = :course AND ea.professor.uniqueCode = :uniqueCode")
    List<ExaminationOutcome> findByCourseAndUniqueCode(@NonNull @Param("course") String course, @NonNull @Param("uniqueCode") UniqueCode uniqueCode);

}
