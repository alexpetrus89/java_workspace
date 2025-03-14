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
@Access(AccessType.PROPERTY)
public class Professor extends User {

    // constants
    private static final String FISCAL_CODE_REGEX = "\\w{16}";
    private static final String FISCAL_CODE_EXCEPTION = "Fiscal Code must be a string of exactly 16 characters and digits";

    //instance variables
    private UniqueCode uniqueCode;
    private String fiscalCode;
    private String email;
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
        String fullname
    ) {
        if(fiscalCode.length() != 16 && !fiscalCode.matches(FISCAL_CODE_REGEX))
            throw new IllegalArgumentException(FISCAL_CODE_EXCEPTION);
        this.fiscalCode = fiscalCode.toUpperCase();
        this.uniqueCode = uniqueCode;
        this.fullname = fullname;
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

    @Column(name = "email")
    public String getEmail() {
        return email;
    }


    // setters
    public void setFiscalCode(String fiscalCode) {
        if(fiscalCode.length() != 16 && !fiscalCode.matches(FISCAL_CODE_REGEX)) {
            throw new IllegalArgumentException(FISCAL_CODE_EXCEPTION);
        }
        this.fiscalCode = fiscalCode.toUpperCase();
    }

    public void setUniqueCode(UniqueCode uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Professor [id=" + id + ", uniqueCode=" + uniqueCode + ", fiscalCode=" + fiscalCode + ", name=" + fullname + ", email=" + email + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode, fiscalCode, fullname, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Professor other = (Professor) obj;
        return Objects.equals(uniqueCode, other.getUniqueCode()) &&
            Objects.equals(fiscalCode, other.getFiscalCode()) &&
            Objects.equals(fullname, other.getFullname()) &&
            Objects.equals(email, other.getEmail());
    }

    // private methods
    private String generateUniqueCode() {
        int code = professorCounter.getAndIncrement();
        return String.format("%08x", code);
    }

}


