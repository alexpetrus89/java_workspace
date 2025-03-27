package com.alex.universitymanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UserId;





@Repository
public interface StudentRepository
    extends JpaRepository<Student, UserId>
{
    /**
     * Retrieves a student by register
     * @param register the register of the student
     * @return the student if found, null otherwise
     * @throws IllegalArgumentException if the register is null
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Query(value = "SELECT s FROM Student s WHERE s.register = ?1")
    Student findByRegister(@NonNull Register register);

    /**
     * Retrieves a student by name
     * @param name the name of the student
     * @return List<Student> with the student if found, or an empty
     *         List if no student is found
     * @throws IllegalArgumentException if the name is null
     * @throws UnsupportedOperationException if the name is not unique
     */
    @Query("SELECT s FROM Student s WHERE s.fullname = ?1")
    List<Student> findByFullname(@NonNull String name);

    /**
     * Retrieves a student by email
     * @param email the email of the student
     * @return List<Student> with the student if found, or an empty
     *         List if no student is found
     * @throws IllegalArgumentException if the email is null
     * @throws UnsupportedOperationException if the email is not unique
     */
    @Query("SELECT s FROM Student s WHERE s.username = ?1")
    List<Student> findByUsername(@NonNull String email);

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
