package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.annotation.ValidAdminCode;
import com.alex.universitymanagementsystem.annotation.ValidFiscalCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public class AdminDto {

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

    @NotBlank(message = "Fiscal code is mandatory")
    @ValidFiscalCode
    private String fiscalCode;

    @NotBlank(message = "Unique code is mandatory")
    @ValidAdminCode
    private String adminCode;

    // default constructor
    public AdminDto() {}

    // constructor
    public AdminDto(String username, String firstName, String lastName, String fiscalCode, String adminCode) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = fiscalCode;
        this.adminCode = adminCode;
    }


    // getters
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public String getFullName() {
        return firstName + " " + lastName;
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

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

}
