package com.alex.universitymanagementsystem.utils;

import java.io.Serializable;
import java.time.LocalDate;

import com.alex.universitymanagementsystem.enum_type.RoleType;

public class Builder implements Serializable {

    // instance variables
    private String username;
    private String password;
    private String confirm;
    private String fullname;
    private LocalDate dob;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private RoleType role;

    // getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm() {
        return confirm;
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
    public void withUsername(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void withPassword(String password) {
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void withFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void withDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void withStreet(String street) {
        this.street = street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void withCity(String city) {
        this.city = city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void withState(String state) {
        this.state = state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void withZip(String zip) {
        this.zip = zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void withPhone(String phone) {
        this.phone = phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void withRole(RoleType role) {
        this.role = role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

}
