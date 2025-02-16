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
     * @param id the id of the course to retrieve
     * @return an Optional containing the course if found, otherwise an
     *         empty Optional
     * @throws IllegalArgumentException if the id is null or empty or invalid UUID
     * @throws UnsupportedOperationException if the id is not unique
     * @throws NullPointerException if the id is null
     */
    @Override
    @NonNull
    @Query("SELECT c FROM Course c WHERE c.id = ?1")
    Optional<Course> findById(@NonNull CourseId id);


    /**
     * Retrieves a course from the repository by its name.
     * @param name the name of the course to retrieve
     * @return an Optional containing the course if found, otherwise an
     *         empty Optional
     * @throws IllegalArgumentException if the name is empty or null or not unique
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    @Query("SELECT c FROM Course c WHERE c.name = ?1")
    Optional<Course> findByName(String name);


    /**
     * Retrieves a course from the repository by its type.
     * @param type the type of the course to retrieve
     * @return an Optional containing the course if found, otherwise an
     *         empty Optional
     * @throws IllegalArgumentException if the type is null
     * @throws UnsupportedOperationException if the type is not unique
     * @throws NullPointerException if the type is null
     * @see CourseType
     */
    @Query("SELECT c FROM Course c WHERE c.type = :type")
    Optional<Course> findByType(@Param("type") CourseType type);


    /**
     * Retrieves a list of courses associated with a specific professor.
     * @param uniqueCode the unique code of the professor whose courses
     *                   are to be retrieved
     * @return an Optional containing a list of courses if any are found,
     *         otherwise an empty Optional
     * @throws IllegalArgumentException if the unique code is null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     * @see UniqueCode
     */
    @Query("SELECT c FROM Course c JOIN c.professor p WHERE p.uniqueCode = ?1")
    Optional<List<Course>> findByProfessor(@NonNull UniqueCode uniqueCode);


    /**
     * Checks if a course exists by its name.
     * @param name the name of the course
     * @return boolean
     * @throws IllegalArgumentException if the name is null
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    boolean existsByName(@NonNull String name);

}
