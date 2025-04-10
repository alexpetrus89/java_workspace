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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.utils.Builder;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "professor")
//@SecondaryTable(name = "\"user\"", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
@Access(AccessType.PROPERTY)
public class Professor extends User {

    // constants
    private static final String FISCAL_CODE_REGEX = "\\w{16}";
    private static final String FISCAL_CODE_EXCEPTION = "Fiscal Code must be a string of exactly 16 characters and digits";

    //instance variables
    private UniqueCode uniqueCode;
    private String fiscalCode;
    private static AtomicInteger professorCounter = new AtomicInteger(100000);

    //default constructor
    public Professor() {}

    // constructor
    public Professor(
        Builder builder,
        PasswordEncoder passwordEncoder
    ) {
        super(builder, passwordEncoder);
        this.uniqueCode = new UniqueCode(generateUniqueCode());
    }


    // constructor
    public Professor(
        Builder builder,
        PasswordEncoder passwordEncoder,
        UniqueCode uniqueCode,
        String fiscalCode
    ) {
        super(builder, passwordEncoder);
        if(fiscalCode.length() != 16 && !fiscalCode.matches(FISCAL_CODE_REGEX))
            throw new IllegalArgumentException(FISCAL_CODE_EXCEPTION);
        this.fiscalCode = fiscalCode.toUpperCase();
        this.uniqueCode = uniqueCode;
    }

    public Professor(
        UniqueCode uniqueCode,
        String fiscalCode,
        String fullname,
        String username
    ) {
        fiscalCode = fiscalCode.toUpperCase();
        if(fiscalCode.length() != 16 && !fiscalCode.matches(FISCAL_CODE_REGEX))
            throw new IllegalArgumentException(FISCAL_CODE_EXCEPTION);
        this.fiscalCode = fiscalCode;
        this.uniqueCode = uniqueCode;
        this.fullname = fullname;
        this.username = username;
    }


    // getters
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

    // setters
    public void setFiscalCode(String fiscalCode) {
        fiscalCode = fiscalCode.toUpperCase();
        if(fiscalCode.length() != 16 && !fiscalCode.matches(FISCAL_CODE_REGEX)) {
            throw new IllegalArgumentException(FISCAL_CODE_EXCEPTION);
        }
        this.fiscalCode = fiscalCode;
    }

    public void setUniqueCode(UniqueCode uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    @Override
    public String toString() {
        return "Professor [id=" + id + ", uniqueCode=" + uniqueCode + ", name=" + fullname + ", fiscalCode=" + fiscalCode + ", email=" + username + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, fiscalCode, fullname, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Professor other = (Professor) obj;
        return Objects.equals(uniqueCode, other.getUniqueCode()) &&
            Objects.equals(fiscalCode, other.getFiscalCode()) &&
            Objects.equals(fullname, other.getFullname()) &&
            Objects.equals(username, other.getUsername());
    }

    // private methods
    private String generateUniqueCode() {
        int code = professorCounter.getAndIncrement();
        return String.format("%08x", code);
    }

}


