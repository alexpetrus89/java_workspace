package com.alex.universitymanagementsystem.dto;


import java.time.LocalDate;

import com.alex.universitymanagementsystem.annotation.ValidBirthDate;
import com.alex.universitymanagementsystem.annotation.ValidFiscalCode;
import com.alex.universitymanagementsystem.entity.Address;
import com.alex.universitymanagementsystem.enum_type.RoleType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

    // instance variables
    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    private String username;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @ValidBirthDate
    private LocalDate dob;

    @NotBlank(message = "Fiscal code is mandatory")
    @ValidFiscalCode
    private String fiscalCode;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Role is mandatory")
    private RoleType role;

    @NotNull(message = "Address is mandatory")
    @Valid
    private Address address;

    // constructors
    public UserDto() { /* no-args constructor */ }

    // Getters
    public String getUsername() {
        return username;
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

    public String getPhone() {
        return phone;
    }

    public RoleType getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }


    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


}

