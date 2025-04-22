package com.alex.universitymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alex.universitymanagementsystem.domain.ExaminationOutcome;

public interface ExaminationOutcomeRepository
    extends JpaRepository<ExaminationOutcome, Long>{

}
