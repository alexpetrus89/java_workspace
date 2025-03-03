package com.alex.universitymanagementsystem.dto;

import java.util.Objects;

import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;



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

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, fiscalCode, name, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        ProfessorDto other = (ProfessorDto) obj;
        return Objects.equals(uniqueCode, other.getUniqueCode()) &&
            Objects.equals(fiscalCode, other.getFiscalCode()) &&
            Objects.equals(name, other.getName()) &&
            Objects.equals(email, other.getEmail());
    }



}
