package com.alex.universitymanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.DegreeCourseId;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.enum_type.CourseType;


@Repository
public interface CourseRepository
    extends JpaRepository<Course, CourseId>
{

    /**
     * Retrieves a course from the repository by its name and degree course.
     * @param courseName the name of the course
     * @param degreeCourseId the id of the degree course
     * @return the course if found, null otherwise
     * @throws NullPointerException if the course name or degree course id is null
     * @throws IllegalArgumentException if the course name or degree course id is null
     * @throws UnsupportedOperationException if the course name or degree course id is not unique
     */
    @Query("SELECT c FROM Course c WHERE c.name = :courseName AND c.degreeCourse.id = :degreeCourseId")
    Course findByNameAndDegreeCourse(
        @Param("courseName") @NonNull String courseName,
        @Param("degreeCourseId") @NonNull DegreeCourseId degreeCourseId
    );


    /**
     * Retrieves a course from the repository by its type.
     * @param type the type of the course to retrieve
     * @return a list of courses if found, null otherwise
     * @throws NullPointerException if the type is null
     * @throws IllegalArgumentException if the type is null
     * @throws UnsupportedOperationException if the type is not unique
     * @see CourseType
     */
    @Query("SELECT c FROM Course c WHERE c.type = :type")
    List<Course> findByType(@NonNull @Param("type") CourseType type);


    /**
     * Retrieves a list of courses associated with a specific professor.
     * @param uniqueCode the unique code of the professor whose courses
     *                   are to be retrieved
     * @return a list of courses if found, null otherwise
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @see UniqueCode
     */
    @Query("SELECT c FROM Course c JOIN c.professor p WHERE p.uniqueCode = ?1")
    List<Course> findByProfessor(@NonNull UniqueCode uniqueCode);


    /**
     * Checks if a course exists by its name.
     * @param name the name of the course
     * @return boolean
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is null
     * @throws UnsupportedOperationException if the name is not unique
     */
    boolean existsByName(@NonNull String name);

    /**
     * Checks if a course exists by its name and degree course
     * @param courseName
     * @param degreeCourseName
     * @return boolean
     * @throws NullPointerException if the course name or degree course name is null
     * @throws IllegalArgumentException if the course name or degree course name is null
     * @throws UnsupportedOperationException if the course name or degree course name is not unique
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END "
    + "FROM Course c JOIN c.degreeCourse dc "
    + "WHERE c.name = :courseName AND dc.name = :degreeCourseName")
    boolean existsByNameAndDegreeCourse(@NonNull @Param("courseName") String courseName, @NonNull @Param("degreeCourseName") String degreeCourseName);
}
