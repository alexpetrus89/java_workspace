package com.alex.universitymanagementsystem.dto;

import java.util.Objects;

import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;



public class ProfessorDto {

    // instance variables
    private UniqueCode uniqueCode;
    private String fiscalCode;
    private String fullname;
    private String email;

    // default constructor
    public ProfessorDto() {}

    // constructor
    public ProfessorDto(
        UniqueCode uniqueCode,
        String fiscalCode,
        String fullname
    ) {
        this.uniqueCode = uniqueCode;
        this.fiscalCode = fiscalCode;
        this.fullname = fullname;
    }


    // getters
    public String getFiscalCode() {
        return fiscalCode;
    }

    public UniqueCode getUniqueCode() {
        return uniqueCode;
    }

    public String getFullname() {
        return fullname;
    }


    // setters
    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setUniqueCode(UniqueCode uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setFullname(String name) {
        this.fullname = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, fiscalCode, fullname, email);
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
            Objects.equals(fullname, other.getFullname());
    }



}
