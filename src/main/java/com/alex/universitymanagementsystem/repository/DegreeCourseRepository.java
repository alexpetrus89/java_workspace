package com.alex.universitymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.immutable.DegreeCourseId;

import jakarta.persistence.PersistenceException;



@Repository
public interface DegreeCourseRepository
    extends JpaRepository<DegreeCourse, DegreeCourseId>
{
    /**
     * Retrieves a degree course from the repository by its name.
     * @param name the name of the degree course to retrieve
     * @return the degree course if found, null otherwise
     * @throws NullPointerException if the name is null
     * @throws PersistenceException persistence error
     */
    @Query("SELECT s FROM DegreeCourse s WHERE s.name = ?1")
    DegreeCourse findByName(@NonNull String name)
        throws NullPointerException, PersistenceException;


    /**
     * Checks if a degree course exists by its name.
     * @param name the name of the degree course
     * @return boolean
     * @throws NullPointerException if the name is null
     * @throws PersistenceException persistence error
     */
    boolean existsByName(@NonNull String name)
        throws NullPointerException, PersistenceException;
}
