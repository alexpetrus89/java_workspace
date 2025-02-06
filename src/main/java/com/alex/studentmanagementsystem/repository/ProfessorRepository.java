package com.alex.studentmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.Professor;
import com.alex.studentmanagementsystem.domain.immutable.ProfessorId;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;



@Repository
public interface ProfessorRepository
    extends JpaRepository<Professor, ProfessorId>
{
    Optional<Professor> findByUniqueCode(UniqueCode uniqueCode);

    Optional<Professor> findByFiscalCode(String fiscalCode);

    Optional<Professor> findByName(String name);

    boolean existsByUniqueCode(UniqueCode uniqueCode);

    void deleteByUniqueCode(UniqueCode uniqueCode);

}
