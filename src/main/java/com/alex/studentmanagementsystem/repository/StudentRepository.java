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
    /**
     * Retrieves a student by register
     * @param studentRegister the register of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     */
    @Query(value = "SELECT s FROM Student s WHERE s.register = ?1")
    Optional<Student> findByRegister(Register studentRegister);

    /**
     * Retrieves a student by name
     * @param name the name of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     */
    @Query("SELECT s FROM Student s WHERE s.name = ?1")
    Optional<Student> findByName(String name);

    /**
     * Retrieves a student by email
     * @param email the email of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     */
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findByEmail(String email);

    boolean existsByRegister(Register studentRegister);

    void deleteByRegister(Register studentRegister);

}
