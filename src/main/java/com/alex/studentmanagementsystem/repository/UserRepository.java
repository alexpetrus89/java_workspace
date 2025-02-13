package com.alex.studentmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.User;
import com.alex.studentmanagementsystem.domain.immutable.UserId;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository
    extends JpaRepository<User, UserId>
{
    public User findByUsername(String username);

    @Transactional
    @Modifying
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
