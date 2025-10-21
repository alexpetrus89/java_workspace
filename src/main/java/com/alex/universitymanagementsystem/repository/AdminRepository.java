package com.alex.universitymanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.alex.universitymanagementsystem.entity.Admin;
import com.alex.universitymanagementsystem.entity.immutable.AdminCode;
import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;
import com.alex.universitymanagementsystem.entity.immutable.UserId;

public interface AdminRepository
    extends JpaRepository<Admin, UserId> {


    /**
     * Retrieves an admin by  admin code
     * @param adminCode the unique code of the professor to retrieve
     * @return Optional<Admin> with the admin if found, or an empty
     *        Optional if no admin is found
     * @see UniqueCode
     */
    @Query(value = "SELECT s FROM Admin s WHERE s.adminCode = ?1")
    Optional<Admin> findByAdminCode(AdminCode adminCode);



    /**
     * Retrieves an admin by fiscal code
     * @param fiscalCode the fiscal code of the admin to retrieve
     * @return Optional<admin> with the admin if found, or an empty
     *        Optional if no admin is found
     * @see FiscalCode
     */
    @Query(value = "SELECT s FROM Admin s WHERE s.fiscalCode = ?1")
    Optional<Admin> findByFiscalCode(FiscalCode fiscalCode);


    /**
     * Retrieves an admin by name
     * @param name the name of the admin to retrieve
     * @return a list of admin with same fullname
     */
    @Query(value = "SELECT s FROM Admin s WHERE s.firstName = ?1 AND s.lastName = ?2")
    List<Admin> findByFullname(String firstName, String lastName);



    /**
     * Checks if an admin exists by unique code
     * @param adminCode the admin code of the admin
     * @return true if the admin exists, false otherwise
     * @see AdminCode
     */
    boolean existsByAdminCode(AdminCode adminCode);


    /**
     * Checks if an admin exists by fiscal code
     * @param fiscalCode the fiscal code of the admin
     * @return true if the admin exists, false otherwise
     * @see FiscalCode
     */
    boolean existsByFiscalCode(FiscalCode fiscalCode);


    /**
     * Deletes an admin by admin code
     * @param uniqueCode the admin code of the admin to delete
     * @see AdminCode
     */
    @Modifying
    void deleteByAdminCode(AdminCode adminCode);

}
