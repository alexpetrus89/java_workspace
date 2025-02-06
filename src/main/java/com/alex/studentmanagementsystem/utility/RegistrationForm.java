package com.alex.studentmanagementsystem.utility;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.studentmanagementsystem.domain.User;

public class RegistrationForm {

    // instance variables
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    // default constructor
    public RegistrationForm() { /* default constructor */ }

    // constructor
    public RegistrationForm(
        String username,
        String password,
        String fullname,
        String street,
        String city,
        String state,
        String zip,
        String phone
    ) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }


    // methods
    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
            username,
            passwordEncoder.encode(password),
            fullname,
            street,
            city,
            state,
            zip,
            phone
        );
    }


}
