package com.alex.universitymanagementsystem.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.alex.universitymanagementsystem.annotation.ValidBirthDate;
import com.alex.universitymanagementsystem.annotation.ValidFiscalCode;
import com.alex.universitymanagementsystem.annotation.ValidPassword;
import com.alex.universitymanagementsystem.domain.Address;
import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.utils.PasswordCarrier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Builder implements Serializable, PasswordCarrier {

    // instance variables
    @NotBlank(message = "username is required")
    @Size(min = 4, max = 30, message = "username must be between 4 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirm;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @ValidBirthDate
    private LocalDate dob;

    @NotBlank(message = "Fiscal code is required")
    @ValidFiscalCode
    private String fiscalCode;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "\\d{5}", message = "ZIP code must be 5 digits")
    private String zip;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+?\\d{9,15}", message = "Phone number is invalid")
    private String phone;

    @NotNull(message = "Role is required")
    private RoleType role;

    // constructor
    public Builder() { /*no args constructor */ }

    // getters
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getConfirm() {
        return confirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getFiscalCode() {
        return fiscalCode;
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

    public Address getAddress() {
        return new Address(street, city, state, zip);
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

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void withConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void withFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void withLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void withDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void withFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
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
