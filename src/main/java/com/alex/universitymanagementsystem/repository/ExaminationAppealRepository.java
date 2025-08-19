package com.alex.universitymanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;



@Repository
public interface ExaminationAppealRepository
    extends JpaRepository<ExaminationAppeal, Long>
{

    /**
     * Find an examination appeal by course id
     * @param courseId
     * @param date
     * @return an examination appeal
     */
    @Query("SELECT ea FROM ExaminationAppeal ea WHERE ea.course.id = :courseId AND ea.date = :date")
    Optional<ExaminationAppeal> findByCourseIdAndDate(@Param("courseId") CourseId courseId, @Param("date") LocalDate date);


    /**
     * Find all examination appeals by course ids
     * @param ids
     * @return a list of examination appeals
     */
    //@Query(value = "SELECT * FROM examination_appeal ea WHERE ea.course_id IN (:ids)", nativeQuery = true)
    List<ExaminationAppeal> findByCourse_Id_IdIn(List<UUID> ids);


    /**
     * Find all examination appeals by course ids and date
     * @param ids
     * @param date
     * @return a list of examination appeals
     */
    // @Query(value = "SELECT * FROM examination_appeal ea WHERE ea.course_id IN (:ids) AND ea.date = :date", nativeQuery = true)
    List<ExaminationAppeal> findByDateBefore(LocalDate expirationDateOneMonth);


}
