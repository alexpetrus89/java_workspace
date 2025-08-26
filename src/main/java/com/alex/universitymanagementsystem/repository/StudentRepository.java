package com.alex.universitymanagementsystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UserId;






@Repository
public interface StudentRepository
    extends JpaRepository<Student, UserId>
{

    /**
     * Retrieves a student by register
     * @param register the register of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     * @see Register
     */
    @Query(value = "SELECT s FROM Student s WHERE s.register = ?1")
    Optional<Student> findByRegister(Register register);


    /**
     * Retrieves a Set of students by his registers
     * @param registers
     * @return Set<Student>
     * @see Register
     */
    @Query("SELECT s FROM Student s WHERE s.register IN ?1")
    Set<Student> findByRegisterIn(Set<Register> registers);


    /**
     * Retrieves a student by email
     * @param username of the student
     * @return Optional<Student> with the student if found, or an empty
     *         Optional if no student is found
     */
    @Query("SELECT s FROM Student s WHERE s.username = ?1")
    Optional<Student> findByUsername(String username);


    /**
     * Retrieves a student by name
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @return a list if student with same fullname
     */
    @Query(value = "SELECT s FROM Student s WHERE s.firstName = ?1 AND s.lastName = ?2")
    List<Student> findByFullname(String firstName, String lastName);


    /**
     * Checks if a student exists by register
     * @param register the register of the student
     * @return true if the student exists, false otherwise
     * @see Register
     */
    boolean existsByRegister(Register register);


    /**
     * Checks if a student exists by fiscal code
     * @param fiscalCode the fiscal code of the student
     * @return true if the student exists, false otherwise
     * @see FiscalCode
     */
    boolean existsByFiscalCode(FiscalCode fiscalCode);


    /**
     * Deletes a student by register
     * @param register the register of the student
     * @see Register
     */
    @Modifying
    void deleteByRegister(Register register);

}
