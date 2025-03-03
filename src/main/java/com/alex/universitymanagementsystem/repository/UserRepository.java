package com.alex.universitymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.UserId;
import com.alex.universitymanagementsystem.utils.Role;

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
     * @param role
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
            u.phone = :phone,
            u.role = :role
            WHERE u.username = :username
    """)
    public int updateUser(
        @Param("username") String username,
        @Param("fullname") String fullname,
        @Param("password") String password,
        @Param("city") String city,
        @Param("state") String state,
        @Param("zip") String zip,
        @Param("phone") String phone,
        @Param("role") Role role
    );

}
