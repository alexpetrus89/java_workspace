package com.alex.universitymanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.StudentId;





@Repository
public interface StudentRepository
    extends JpaRepository<Student, StudentId>
{
    /**
     * Retrieves a student by register
     * @param register the register of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     * @throws IllegalArgumentException if the register is null
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Query(value = "SELECT s FROM Student s WHERE s.register = ?1")
    Optional<Student> findByRegister(@NonNull Register register);

    /**
     * Retrieves a student by name
     * @param name the name of the student
     * @return Optional<List<Student>> with the student if found, or an empty
     *         Optional if no student is found
     * @throws IllegalArgumentException if the name is null
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Query("SELECT s FROM Student s WHERE s.name = ?1")
    Optional<List<Student>> findByName(@NonNull String name);

    /**
     * Retrieves a student by email
     * @param email the email of the student
     * @return Optional<List<Student>> with the student if found, or an empty
     *         Optional if no student is found
     * @throws IllegalArgumentException if the email is null
     * @throws UnsupportedOperationException if the email is not unique
     */
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<List<Student>> findByEmail(@NonNull String email);

    /**
     * Checks if a student exists by register
     * @param register the register of the student
     * @return true if the student exists, false otherwise
     * @throws IllegalArgumentException if the register is null
     */
    boolean existsByRegister(@NonNull Register register);

    /**
     * Deletes a student by register
     * @param register the register of the student
     * @throws IllegalArgumentException if the register is null
     * @throws UnsupportedOperationException if the register is not unique
     */
    void deleteByRegister(@NonNull Register register);

}
