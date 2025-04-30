package com.alex.universitymanagementsystem.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.ExaminationId;
import com.alex.universitymanagementsystem.domain.immutable.Register;

import jakarta.persistence.PersistenceException;


@Repository
public interface ExaminationRepository
    extends JpaRepository<Examination, ExaminationId>
{

    /**
     * Retrieves a list of examinations for a given student based
     * on the student's register.
     * @param register the register of the student
     * @return a list of Examination entities associated with the specified student
     * @throws NullPointerException if the register is null
     * @throws PersistenceException persistence error
     */
    @Query("SELECT e FROM Examination e WHERE e.student.register = :register")
    List<Examination> findExaminationsByStudent(@NonNull @Param("register") Register register)
        throws NullPointerException, PersistenceException;


    /**
     * Retrieves a list of examinations for a given course based
     * on the course's ID.
     * @param courseId the ID of the course
     * @return a list of Examination entities associated with the specified course
     * @throws NullPointerException if the courseId is null
     * @throws PersistenceException persistence error
     */
    @Query("SELECT e FROM Examination e WHERE e.course.id = :courseId")
    List<Examination> findExaminationsByCourseId(@NonNull @Param("courseId") CourseId courseId)
        throws NullPointerException, PersistenceException;


    /**
     * Retrieves a list of examinations for a given course based
     * on the course's name.
     * @param name the name of the course
     * @return a list of Examination entities associated with the specified course
     * @throws NullPointerException if the name is null
     * @throws PersistenceException persistence error
     */
    @Query("SELECT e FROM Examination e WHERE e.course.name = :name")
    List<Examination> findExaminationsByCourseName(@NonNull @Param("name") String name)
        throws NullPointerException, PersistenceException;
}
