package com.alex.universitymanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.immutable.DegreeCourseId;



@Repository
public interface DegreeCourseRepository
    extends JpaRepository<DegreeCourse, DegreeCourseId>
{
    /**
     * Retrieves a degree course from the repository by its name.
     * @param name the name of the degree course to retrieve
     * @return an Optional containing the degree course if found, otherwise an
     *         empty Optional
     * @throws IllegalArgumentException if the name is null
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    @Query("SELECT s FROM DegreeCourse s WHERE s.name = ?1")
    Optional<DegreeCourse> findByName(@NonNull String name);


    /**
     * Checks if a degree course exists by its name.
     * @param name the name of the degree course
     * @return boolean
     * @throws IllegalArgumentException if the name is null
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    boolean existsByName(@NonNull String name);
}
