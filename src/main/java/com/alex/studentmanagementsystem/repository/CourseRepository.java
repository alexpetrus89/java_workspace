package com.alex.studentmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;




@Repository
public interface CourseRepository
    extends JpaRepository<Course, CourseId>
{

    @Override
    @Query("SELECT c FROM Course c WHERE c.id = ?1")
    @NonNull
    Optional<Course> findById(@NonNull CourseId id);

    @Query("SELECT c FROM Course c WHERE c.name = ?1")
    Optional<Course> findByName(String name);

    @Query("SELECT c FROM Course c WHERE c.category = ?1")
    Optional<Course> findByCategory(String category);

    @Query("SELECT c FROM Course c JOIN c.professor p WHERE p.uniqueCode = ?1")
    Optional<List<Course>> findByProfessor(UniqueCode uniqueCode);

    boolean existsByName(String name);

}
