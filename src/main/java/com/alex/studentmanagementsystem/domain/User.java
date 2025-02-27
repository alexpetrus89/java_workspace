package com.alex.studentmanagementsystem.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alex.studentmanagementsystem.domain.immutable.UserId;
import com.alex.studentmanagementsystem.utils.Builder;
import com.alex.studentmanagementsystem.utils.Role;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {

    // instance variables
    @EmbeddedId
    private UserId id;
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private Role role;

    // default constructor
    public User() {}

    // constructor
    public User(Builder userBuilder) {
        this.id = new UserId(UUID.randomUUID());
        this.username = userBuilder.getUsername();
        this.password = userBuilder.getPassword();
        this.fullname = userBuilder.getFullname();
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

    public Role getRole() {
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

    public void setRole(Role role) {
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
            ", street='" + street + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", zip='" + zip + '\'' +
            ", phoneNumber='" + phone + '\'' + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, fullname, street, city, state, zip, phone, role);
    }

    // equals and hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(username, other.username) &&
            Objects.equals(password, other.password) &&
            Objects.equals(fullname, other.fullname) &&
            Objects.equals(street, other.street) &&
            Objects.equals(city, other.city) &&
            Objects.equals(state, other.state) &&
            Objects.equals(zip, other.zip) &&
            Objects.equals(phone, other.phone) &&
            Objects.equals(role, other.role );
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


}
