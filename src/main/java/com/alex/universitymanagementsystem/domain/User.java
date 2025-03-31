package com.alex.universitymanagementsystem.domain;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.immutable.UserId;
import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.utils.Builder;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"user\"")
@Inheritance(strategy = jakarta.persistence.InheritanceType.TABLE_PER_CLASS)
public class User implements UserDetails {

    // instance variables
    @EmbeddedId
    protected UserId id;
    protected String username;
    protected String password;
    protected String fullname;
    protected LocalDate dob;
    protected String street;
    protected String city;
    protected String state;
    protected String zip;
    protected String phone;
    protected RoleType role;

    // default constructor
    public User() {}

    // constructor
    public User(Builder userBuilder, PasswordEncoder passwordEncoder) {
        this.id = new UserId(UUID.randomUUID());
        this.username = userBuilder.getUsername();
        this.password = passwordEncoder.encode(userBuilder.getPassword());
        this.fullname = userBuilder.getFullname();
        this.dob = userBuilder.getDob();
        this.street = userBuilder.getStreet();
        this.city = userBuilder.getCity();
        this.state = userBuilder.getState();
        this.zip = userBuilder.getZip();
        this.phone = userBuilder.getPhone();
        this.role = userBuilder.getRole();
    }


    // getters
    public UserId getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public RoleType getRole() {
        return role;
    }


    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setPhoneNumber(String phone) {
        this.phone = phone;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    // toString
    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", fullname='" + fullname + '\'' +
            ", dob='" + dob + '\'' +
            ", street='" + street + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", zip='" + zip + '\'' +
            ", phoneNumber='" + phone + '\'' + '}';
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (role) {
            case STUDENT -> Arrays.asList(new SimpleGrantedAuthority("ROLE_STUDENT"));
            case PROFESSOR -> Arrays.asList(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
            default -> Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        };
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    // equals and hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, fullname, dob, street, city, state, zip, phone, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(username, other.username) &&
            Objects.equals(password, other.password) &&
            Objects.equals(fullname, other.fullname) &&
            Objects.equals(dob, other.dob) &&
            Objects.equals(street, other.street) &&
            Objects.equals(city, other.city) &&
            Objects.equals(state, other.state) &&
            Objects.equals(zip, other.zip) &&
            Objects.equals(phone, other.phone) &&
            Objects.equals(role, other.role );
    }


}
