package com.alex.universitymanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.UserId;
import com.alex.universitymanagementsystem.enum_type.RoleType;

@Repository
public interface UserRepository
    extends JpaRepository<User, UserId>
{
    /**
     * Find user by username
     * @param username
     * @return User
     * @throws NullPointerException if the username is null
     * @throws IllegalArgumentException if the username is null
     */
    User findByUsername(@NonNull String username)
        throws NullPointerException, IllegalArgumentException;

    /**
     * Find user by fullname
     * @param fullname
     * @return List<User>
     * @throws NullPointerException if the fullname is null
     * @throws IllegalArgumentException if the fullname is blank
     */
    List<User> findByFullname(@NonNull String fullname)
        throws NullPointerException, IllegalArgumentException;

    /**
     * Find user by dob
     * @param dob
     * @return List<User>
     * @throws NullPointerException if the dob is null
     * @throws IllegalArgumentException if the dob is null
     */
    List<User> findByDob(@NonNull LocalDate dob);


    /**
     * Update user
     * @param username
     * @param fullname
     * @param password
     * @param dob
     * @param city
     * @param state
     * @param zip
     * @param phone
     * @param role
     * @return int number of rows updated
     * @throws NullPointerException if the parameters are null
     * @throws IllegalArgumentException if the parameters are blanks
     * @throws UsernameNotFoundException if the user is not found
     */
    @Modifying
    @Query("""
            UPDATE User u SET
            u.fullname = :fullname,
            u.password = :password,
            u.dob = :dob,
            u.city = :city,
            u.state = :state,
            u.zip = :zip,
            u.phone = :phone,
            u.role = :role
            WHERE u.username = :username
    """)
    public int updateUser(
        @NonNull @Param("username") String username,
        @NonNull @Param("fullname") String fullname,
        @NonNull @Param("password") String password,
        @Param("dob") LocalDate dob,
        @Param("city") String city,
        @Param("state") String state,
        @Param("zip") String zip,
        @Param("phone") String phone,
        @NonNull @Param("role") RoleType role
    );

}
