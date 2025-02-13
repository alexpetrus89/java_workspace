package com.alex.studentmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.Professor;
import com.alex.studentmanagementsystem.domain.immutable.ProfessorId;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;



@Repository
public interface ProfessorRepository
    extends JpaRepository<Professor, ProfessorId>
{
    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return Optional containing the professor if found, empty otherwise
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.uniqueCode = ?1")
    Optional<Professor> findByUniqueCode(UniqueCode uniqueCode);

    /**
     * Retrieves a professor by fiscal code
     * @param fiscalCode the fiscal code of the professor to retrieve
     * @return Optional containing the professor if found, empty otherwise
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.fiscalCode = ?1")
    Optional<Professor> findByFiscalCode(String fiscalCode);

    /**
     * Retrieves a professor by name
     * @param name the name of the professor to retrieve
     * @return Optional containing the professor if found, empty otherwise
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.name = ?1")
    Optional<Professor> findByName(String name);

    boolean existsByUniqueCode(UniqueCode uniqueCode);

    boolean existsByFiscalCode(String fiscalCode);

    void deleteByUniqueCode(UniqueCode uniqueCode);

}
