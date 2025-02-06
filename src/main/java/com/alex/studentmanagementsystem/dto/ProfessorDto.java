package com.alex.studentmanagementsystem.dto;

import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;

public class ProfessorDto {

    // instance variables
    private UniqueCode uniqueCode;
    private String fiscalCode;
    private String name;
    private String email;

    // default constructor
    public ProfessorDto() {}

    // constructor
    public ProfessorDto(
        UniqueCode uniqueCode,
        String fiscalCode,
        String name,
        String email
    ) {
        this.uniqueCode = uniqueCode;
        this.fiscalCode = fiscalCode;
        this.name = name;
        this.email = email;
    }


    // getters
    public String getFiscalCode() {
        return fiscalCode;
    }

    public UniqueCode getUniqueCode() {
        return uniqueCode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    // setters
    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setUniqueCode(UniqueCode uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
