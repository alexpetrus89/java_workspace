package com.alex.studentmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.utils.CourseType;




@Repository
public interface CourseRepository
    extends JpaRepository<Course, CourseId>
{

    /**
     * Retrieves a course from the repository by its id.
     *
     * @param id the id of the course to retrieve
     * @return an Optional containing the course if found, otherwise an
     *         empty Optional
     */
    @Override
    @Query("SELECT c FROM Course c WHERE c.id = ?1")
    @NonNull
    Optional<Course> findById(@NonNull CourseId id);


    /**
     * Retrieves a course from the repository by its name.
     *
     * @param name the name of the course to retrieve
     * @return an Optional containing the course if found, otherwise an
     *         empty Optional
     */
    @Query("SELECT c FROM Course c WHERE c.name = ?1")
    Optional<Course> findByName(String name);


    /**
     * Retrieves a course from the repository by its type.
     *
     * @param type the type of the course to retrieve
     * @return an Optional containing the course if found, otherwise an
     *         empty Optional
     */
    @Query("SELECT c FROM Course c WHERE c.type = :type")
    Optional<Course> findByType(@Param("type") CourseType type);


    /**
     * Retrieves a list of courses associated with a specific professor.
     *
     * @param uniqueCode the unique code of the professor whose courses are to be retrieved
     * @return an Optional containing a list of courses if any are found, otherwise an empty Optional
     */
    @Query("SELECT c FROM Course c JOIN c.professor p WHERE p.uniqueCode = ?1")
    Optional<List<Course>> findByProfessor(UniqueCode uniqueCode);


    /**
     * Checks if a course exists by its name.
     * @param name
     * @return boolean
     */
    boolean existsByName(String name);

}
