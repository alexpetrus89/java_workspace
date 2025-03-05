package com.alex.universitymanagementsystem.domain;

/**
 * Voglio creare un collegamento tra la classe Professor
 * e la classe Course senza però inserire il field Course
 * dentro la classe Professor, e senza inserire il field
 * Professor dentro la classe Course, visto che voglio
 * creare un Professor senza aver ancora deciso quale
 * Course affidargli.
 * Un Professor può tenere più Course, mentre ad un
 * Course può essere associato uno ed un solo Professor.
 */

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.alex.universitymanagementsystem.domain.immutable.ProfessorId;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "professor")
@Access(AccessType.PROPERTY)
public class Professor implements Serializable {

    //instance variables
    private ProfessorId id;
    private UniqueCode uniqueCode;
    private String fiscalCode;
    private String name;
    private String email;

    //default constructor
    public Professor() {}

    // constructor
    public Professor(
        UniqueCode uniqueCode,
        String fiscalCode,
        String name,
        String email

    ) {
        this.id = new ProfessorId(UUID.randomUUID());
        if(fiscalCode.length() != 16 && !fiscalCode.matches("\\w{16}"))
            throw new IllegalArgumentException("Fiscal Code must be a string of exactly 16 characters and digits");
        this.fiscalCode = fiscalCode.toUpperCase();
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.email = email;
    }


    // getters
    @EmbeddedId
    @Column(name = "professor_id")
    public ProfessorId getId() {
        return id;
    }

    @Embedded
    @AttributeOverride(
        name = "unique_code",
        column = @Column(name = "unique_code")
    )
    @Pattern(regexp = "^[a-zA-Z0-9]{1,8}$")
    public UniqueCode getUniqueCode() {
        return uniqueCode;
    }

    @AttributeOverride(
        name = "fiscal_code",
        column = @Column(name = "fiscal_code")
    )
    @Pattern(regexp = "^[a-zA-Z0-9]{1,16}$")
    public String getFiscalCode() {
        return fiscalCode;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }


    // setters
    public void setId(ProfessorId id) {
        this.id = id;
    }

    public void setFiscalCode(String fiscalCode) {
        if(fiscalCode.length() != 16 && !fiscalCode.matches("\\w{16}")) {
            throw new IllegalArgumentException("Fiscal Code must be a string of exactly 16 characters and digits");
        }
        this.fiscalCode = fiscalCode.toUpperCase();
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
    public String toString() {
        return "Professor [id=" + id + ", uniqueCode=" + uniqueCode + ", fiscalCode=" + fiscalCode + ", name=" + name + ", email=" + email + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, fiscalCode, name, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Professor other = (Professor) obj;
        return Objects.equals(uniqueCode, other.getUniqueCode()) &&
            Objects.equals(fiscalCode, other.getFiscalCode()) &&
            Objects.equals(name, other.getName()) &&
            Objects.equals(email, other.getEmail());
    }

}


