package com.alex.studentmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.StudentId;

import io.micrometer.common.lang.NonNull;


@Repository
public interface StudentRepository
    extends JpaRepository<Student, StudentId>
{
    /**
     * Retrieves a student by register
     * @param Register register the register of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     * @throws IllegalArgumentException if the register is null
     */
    @Query(value = "SELECT s FROM Student s WHERE s.register = ?1")
    Optional<Student> findByRegister(@NonNull Register register);

    /**
     * Retrieves a student by name
     * @param String name the name of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     * @throws IllegalArgumentException if the name is null
     */
    @Query("SELECT s FROM Student s WHERE s.name = ?1")
    Optional<Student> findByName(@NonNull String name);

    /**
     * Retrieves a student by email
     * @param String email the email of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     * @throws IllegalArgumentException if the email is null
     */
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findByEmail(@NonNull String email);

    /**
     * Checks if a student exists by register
     * @param Register register
     * @return true if the student exists, false otherwise
     * @throws IllegalArgumentException if the register is null
     */
    boolean existsByRegister(@NonNull Register register);

    /**
     * Deletes a student by register
     * @param Register register
     * @throws IllegalArgumentException if the register is null
     */
    void deleteByRegister(@NonNull Register register);

}
