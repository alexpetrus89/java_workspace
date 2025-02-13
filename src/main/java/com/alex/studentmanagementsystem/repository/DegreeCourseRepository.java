package com.alex.studentmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.domain.immutable.DegreeCourseId;

@Repository
public interface DegreeCourseRepository
    extends JpaRepository<DegreeCourse, DegreeCourseId>
{
    /**
     * Retrieves a degree course from the repository by its name.
     *
     * @param name the name of the degree course to retrieve
     * @return an Optional containing the degree course if found, otherwise an
     *         empty Optional
     */
    @Query("SELECT s FROM DegreeCourse s WHERE s.name = ?1")
    Optional<DegreeCourse> findByName(String name);

    boolean existsByName(String name);
}
