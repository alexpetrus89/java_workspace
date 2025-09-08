package com.alex.universitymanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.Professor;
import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;
import com.alex.universitymanagementsystem.entity.immutable.UserId;




@Repository
public interface ProfessorRepository
    extends JpaRepository<Professor, UserId>
{

    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return Optional<Professor> with the professor if found, or an empty
     *        Optional if no professor is found
     * @see UniqueCode
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.uniqueCode = ?1")
    Optional<Professor> findByUniqueCode(UniqueCode uniqueCode);



    /**
     * Retrieves a professor by fiscal code
     * @param fiscalCode the fiscal code of the professor to retrieve
     * @return Optional<Professor> with the professor if found, or an empty
     *        Optional if no professor is found
     * @see FiscalCode
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.fiscalCode = ?1")
    Optional<Professor> findByFiscalCode(FiscalCode fiscalCode);


    /**
     * Retrieves a professor by name
     * @param name the name of the professor to retrieve
     * @return a list if professor with same fullname
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.firstName = ?1 AND s.lastName = ?2")
    List<Professor> findByFullname(String firstName, String lastName);



    /**
     * Checks if a professor exists by unique code
     * @param uniqueCode the unique code of the professor
     * @return true if the professor exists, false otherwise
     * @see UniqueCode
     */
    boolean existsByUniqueCode(UniqueCode uniqueCode);


    /**
     * Checks if a professor exists by fiscal code
     * @param fiscalCode the fiscal code of the professor
     * @return true if the professor exists, false otherwise
     * @see FiscalCode
     */
    boolean existsByFiscalCode(FiscalCode fiscalCode);


    /**
     * Deletes a professor by unique code
     * @param uniqueCode the unique code of the professor to delete
     * @see UniqueCode
     */
    @Modifying
    void deleteByUniqueCode(UniqueCode uniqueCode);

}
