package com.alex.studentmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.User;
import com.alex.studentmanagementsystem.domain.immutable.UserId;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository
    extends JpaRepository<User, UserId>
{
    /**
     * Find user by username
     * @param username
     * @return User
     * @throws UsernameNotFoundException if the user is not found
     */
    public User findByUsername(@NonNull String username);

    @Transactional
    @Modifying
    /**
     * Update user
     * @param username
     * @param fullname
     * @param password
     * @param city
     * @param state
     * @param zip
     * @param phone
     * @return int number of rows updated
     * @throws UsernameNotFoundException if the user is not found
     */
    @Query("""
            UPDATE User u SET
            u.fullname = :fullname,
            u.password = :password,
            u.city = :city,
            u.state = :state,
            u.zip = :zip,
            u.phone = :phone
            WHERE u.username = :username
    """)
    public int updateUser(
        @Param("username") String username,
        @Param("fullname") String fullname,
        @Param("password") String password,
        @Param("city") String city,
        @Param("state") String state,
        @Param("zip") String zip,
        @Param("phone") String phone
    );

}
