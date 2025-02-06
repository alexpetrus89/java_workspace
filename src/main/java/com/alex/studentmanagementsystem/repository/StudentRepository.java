package com.alex.studentmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.StudentId;


@Repository
public interface StudentRepository
    extends JpaRepository<Student, StudentId>
{
    @Query(value = "SELECT s FROM Student s WHERE s.register = ?1")
    Optional<Student> findByRegister(Register studentRegister);

    @Query("SELECT s FROM Student s WHERE s.name = ?1")
    Optional<Student> findByName(String name);

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findByEmail(String email);

    boolean existsByRegister(Register studentRegister);

    void deleteByRegister(Register studentRegister);

}
