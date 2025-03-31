package com.alex.universitymanagementsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;


@Repository
public interface ExaminationAppealRepository
    extends JpaRepository<ExaminationAppeal, Long>
{
    /**
     * Find all examination appeals by course ids
     * @param ids
     * @return a list of examination appeals
     */
    @Query(value = "SELECT * FROM examination_appeal ea WHERE ea.course_id IN (:ids)", nativeQuery = true)
    List<ExaminationAppeal> findByIdIn(@Param("ids") List<UUID> ids);
}
