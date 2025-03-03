package com.alex.universitymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.domain.immutable.StudyPlanId;



@Repository
public interface StudyPlanRepository
    extends JpaRepository<StudyPlan, StudyPlanId> {


}
