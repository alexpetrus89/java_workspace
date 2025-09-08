package com.alex.universitymanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.DegreeCourse;
import com.alex.universitymanagementsystem.entity.immutable.DegreeCourseId;




@Repository
public interface DegreeCourseRepository
    extends JpaRepository<DegreeCourse, DegreeCourseId>
{
    /**
     * Retrieves a degree course from the repository by its name.
     * @param name the name of the degree course to retrieve
     * @return the degree course if found, null otherwise
     */
    @Query("SELECT s FROM DegreeCourse s WHERE s.name = ?1")
    Optional<DegreeCourse> findByName(String name);


    /**
     * Checks if a degree course exists by its name.
     * @param name the name of the degree course
     * @return boolean
     */
    boolean existsByName(String name);
}
