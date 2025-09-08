package com.alex.universitymanagementsystem.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.Course;
import com.alex.universitymanagementsystem.entity.immutable.CourseId;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;
import com.alex.universitymanagementsystem.enum_type.CourseType;



@Repository
public interface CourseRepository
    extends JpaRepository<Course, CourseId>
{

    /**
     * Retrieves a course from the repository by its name and degree course.
     * @param courseName the name of the course
     * @param degreeCourseName the name of degree course
     * @return an Optional containing the course if found, empty otherwise
     */
    @Query("SELECT c FROM Course c WHERE c.name = ?1 AND c.degreeCourse.name = ?2")
    Optional<Course> findByNameAndDegreeCourseName(
        @Param("courseName") String courseName,
        @Param("degreeCourseName") String degreeCourseName
    );


    /**
     * Retrieves a course from the repository by its type.
     * @param type the type of the course to retrieve
     * @return a set of courses if found, null otherwise
     * @see CourseType
     */
    @Query("SELECT c FROM Course c WHERE c.type = :type")
    Set<Course> findByType(@Param("type") CourseType type);


    /**
     * Retrieves a list of courses associated with a specific professor.
     * @param uniqueCode the unique code of the professor whose courses
     *                   are to be retrieved
     * @return a set of courses if found, null otherwise
     * @see UniqueCode
     */
    @Query("SELECT c FROM Course c JOIN c.professor p WHERE p.uniqueCode = ?1")
    Set<Course> findByProfessor(UniqueCode uniqueCode);


    /**
     * Checks if a course exists by its name.
     * @param name the name of the course
     * @return boolean
     */
    boolean existsByName(String name);


    /**
     * Checks if a course exists by its name and degree course
     * @param courseName
     * @param degreeCourseName
     * @return boolean
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END "
    + "FROM Course c JOIN c.degreeCourse dc "
    + "WHERE c.name = :courseName AND dc.name = :degreeCourseName")
    boolean existsByNameAndDegreeCourseName(@Param("courseName") String courseName, @Param("degreeCourseName") String degreeCourseName);


}