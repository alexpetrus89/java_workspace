package com.alex.universitymanagementsystem.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.Examination;
import com.alex.universitymanagementsystem.entity.immutable.ExaminationId;


@Repository
public interface ExaminationRepository
    extends JpaRepository<Examination, ExaminationId>
{

    /**
     * Retrieves all examinations for the given student register.
     *
     * @param register the register of the student
     * @return list of examinations associated with the student
     */
    List<Examination> findByRegister(String register);


    /**
     * Retrieves all examinations for the given course ID.
     *
     * @param courseId the ID of the course
     * @return list of examinations associated with the specified course
     */
    List<Examination> findByCourse_Id_Id(UUID courseId);


}