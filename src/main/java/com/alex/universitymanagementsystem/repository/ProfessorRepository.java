package com.alex.universitymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.domain.immutable.UserId;



@Repository
public interface ProfessorRepository
    extends JpaRepository<Professor, UserId>
{
    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor to retrieve
     * @return the professor if found, null otherwise
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is empty or null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @see UniqueCode
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.uniqueCode = ?1")
    Professor findByUniqueCode(@NonNull UniqueCode uniqueCode);


    /**
     * Retrieves a professor by fiscal code
     * @param fiscalCode the fiscal code of the professor to retrieve
     * @return the professor if found, null otherwise
     * @throws NullPointerException if the fiscal code is null
     * @throws IllegalArgumentException if the fiscal code is empty or null
     * @throws UnsupportedOperationException if the fiscal code is not unique
     * @see FiscalCode
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.fiscalCode = ?1")
    Professor findByFiscalCode(@NonNull String fiscalCode);


    /**
     * Retrieves a professor by name
     * @param name the name of the professor to retrieve
     * @return the professor if found, null otherwise
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty or null
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Query(value = "SELECT s FROM Professor s WHERE s.fullname = ?1")
    Professor findByFullname(@NonNull String name);


    /**
     * Checks if a professor exists by unique code
     * @param uniqueCode the unique code of the professor
     * @return true if the professor exists, false otherwise
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is null
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    boolean existsByUniqueCode(@NonNull UniqueCode uniqueCode);


    /**
     * Checks if a professor exists by fiscal code
     * @param fiscalCode the fiscal code of the professor
     * @return true if the professor exists, false otherwise
     * @throws NullPointerException if the fiscal code is null
     * @throws IllegalArgumentException if the fiscal code is null
     * @throws UnsupportedOperationException if the fiscal code is not unique
     */
    boolean existsByFiscalCode(@NonNull String fiscalCode);


    /**
     * Deletes a professor by unique code
     * @param uniqueCode the unique code of the professor to delete
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is null
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Modifying
    void deleteByUniqueCode(@NonNull UniqueCode uniqueCode);

}
