package com.alex.universitymanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.domain.immutable.UserId;

import jakarta.persistence.PersistenceException;



@Repository
public interface ProfessorRepository
    extends JpaRepository<Professor, UserId>
{
    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return the professor if found, null otherwise
     * @throws NullPointerException if the unique code is null
     * @throws PersistenceException persistence error
     * @see UniqueCode
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.uniqueCode = ?1")
    Professor findByUniqueCode(@NonNull UniqueCode uniqueCode)
        throws NullPointerException, PersistenceException;


    /**
     * Retrieves a professor by fiscal code
     * @param fiscalCode the fiscal code of the professor to retrieve
     * @return the professor if found, null otherwise
     * @throws NullPointerException if the fiscal code is null
     * @throws PersistenceException persistence error
     * @see FiscalCode
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.fiscalCode = ?1")
    Professor findByFiscalCode(@NonNull FiscalCode fiscalCode)
        throws NullPointerException, PersistenceException;


    /**
     * Retrieves a professor by name
     * @param name the name of the professor to retrieve
     * @return a list if professor with same fullname
     * @throws NullPointerException if the name is null
     * @throws PersistenceException persistence error
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.fullname = ?1")
    List<Professor> findByFullname(@NonNull String name)
        throws NullPointerException, PersistenceException;


    /**
     * Checks if a professor exists by unique code
     * @param uniqueCode the unique code of the professor
     * @return true if the professor exists, false otherwise
     * @throws NullPointerException if the unique code is null
     * @throws PersistenceException persistence error
     */
    boolean existsByUniqueCode(@NonNull UniqueCode uniqueCode)
        throws NullPointerException, PersistenceException;


    /**
     * Checks if a professor exists by fiscal code
     * @param fiscalCode the fiscal code of the professor
     * @return true if the professor exists, false otherwise
     * @throws NullPointerException if the fiscal code is null
     * @throws PersistenceException persistence error
     */
    boolean existsByFiscalCode(@NonNull FiscalCode fiscalCode)
        throws NullPointerException, PersistenceException;


    /**
     * Deletes a professor by unique code
     * @param uniqueCode the unique code of the professor to delete
     * @throws NullPointerException if the unique code is null
     * @throws PersistenceException persistence error
     */
    @Modifying
    void deleteByUniqueCode(@NonNull UniqueCode uniqueCode)
        throws NullPointerException, PersistenceException;

}
