package com.alex.universitymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.StudyPlan;
import com.alex.universitymanagementsystem.entity.immutable.StudyPlanId;



@Repository
public interface StudyPlanRepository
    extends JpaRepository<StudyPlan, StudyPlanId> {


}
