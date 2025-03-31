package com.alex.universitymanagementsystem.utils;
import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.enum_type.RoleType;

public class RegistrationForm {

    // instance variables
    private String username;
    private String password;
    private String fullname;
    private LocalDate dob;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private RoleType role;

    // constructor
    public RegistrationForm(Builder formBuilder) {
        this.username = formBuilder.getUsername();
        this.password = formBuilder.getPassword();
        this.fullname = formBuilder.getFullname();
        this.dob = formBuilder.getDob();
        this.street = formBuilder.getStreet();
        this.city = formBuilder.getCity();
        this.state = formBuilder.getState();
        this.zip = formBuilder.getZip();
        this.phone = formBuilder.getPhone();
        this.role = formBuilder.getRole();
    }

    // getters
    public String getUsername() {
        return username;
    }

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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }


    // methods
    public User toUser(PasswordEncoder passwordEncoder) {
        // create a new user builder
        Builder userBuilder = new Builder();
        // set the values
        userBuilder.withUsername(username);
        userBuilder.withPassword(password);
        userBuilder.withFullname(fullname);
        userBuilder.withDob(dob);
        userBuilder.withStreet(street);
        userBuilder.withCity(city);
        userBuilder.withState(state);
        userBuilder.withZip(zip);
        userBuilder.withPhone(phone);
        userBuilder.withRole(role);
        // create the user
        return new User(userBuilder, passwordEncoder);
    }


}
