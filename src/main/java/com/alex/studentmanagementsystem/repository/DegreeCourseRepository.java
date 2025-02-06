package com.alex.studentmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.domain.immutable.DegreeCourseId;

@Repository
public interface DegreeCourseRepository
    extends JpaRepository<DegreeCourse, DegreeCourseId>
{
    Optional<DegreeCourse> findByName(String name);
}
