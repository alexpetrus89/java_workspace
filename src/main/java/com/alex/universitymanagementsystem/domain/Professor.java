package com.alex.universitymanagementsystem.domain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.Builder;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROFESSORS")
@Access(AccessType.PROPERTY)
public class Professor extends User {

    // instance variables
    private UniqueCode uniqueCode;
    private static final AtomicInteger professorCounter = new AtomicInteger(100000);

    // constructors
    protected Professor() { super(); }

    public Professor(Builder builder, PasswordEncoder encoder) {
        super(builder, encoder);
        this.uniqueCode = new UniqueCode(generateUniqueCode());
    }


    public Professor(Builder builder, PasswordEncoder passwordEncoder, UniqueCode uniqueCode) {
        super(builder, passwordEncoder);
        this.uniqueCode = uniqueCode;
    }

    public Professor(String username, String firstName, String lastName, String fiscalCode, String uniqueCode) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = new FiscalCode(fiscalCode);
        this.uniqueCode = new UniqueCode(uniqueCode);
    }


    // getters
    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "unique_code", nullable = false, unique = true)
    )
    public UniqueCode getUniqueCode() {
        return uniqueCode;
    }

    // setters
    public void setUniqueCode(UniqueCode uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    // --- Object methods ---
    @Override
    public String toString() {
        return "Professor [id=" + id +
        ", uniqueCode=" + uniqueCode +
        ", name=" + firstName + " " + lastName +
        ", fiscal code=" + fiscalCode +
        ", email=" + username +
        "]";
    }

    // equals and hashCode
    @Override
    public int hashCode() {
        return Objects.hash(uniqueCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor)) return false;
        Professor other = (Professor) o;
        return Objects.equals(uniqueCode, other.uniqueCode);
    }

    // --- Private helper ---
    private String generateUniqueCode() {
        int code = professorCounter.getAndIncrement();
        return String.format("%08x", code);
    }

}


