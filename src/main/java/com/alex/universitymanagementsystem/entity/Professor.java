package com.alex.universitymanagementsystem.entity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.entity.immutable.ProfessorCode;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROFESSORS")
@Access(AccessType.PROPERTY)
@PrimaryKeyJoinColumn(name = "id")
public class Professor extends User {

    // instance variables
    private ProfessorCode professorCode;
    private static final AtomicInteger professorCounter = new AtomicInteger(100000);

    // constructors
    protected Professor() { super(); }

    public Professor(RegistrationForm form, PasswordEncoder encoder) {
        super(form, encoder);
        this.professorCode = new ProfessorCode(generateProfessorCode());
    }


    public Professor(RegistrationForm form, PasswordEncoder passwordEncoder, ProfessorCode professorCode) {
        super(form, passwordEncoder);
        this.professorCode = professorCode;
    }

    public Professor(String username, String firstName, String lastName, String fiscalCode, String professorCode) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = new FiscalCode(fiscalCode);
        this.professorCode = new ProfessorCode(professorCode);
    }


    // getters
    @Embedded
    public ProfessorCode getProfessorCode() { return professorCode; }

    // setters
    public void setProfessorCode(ProfessorCode professorCode) { this.professorCode = professorCode; }

    // --- Object methods ---
    @Override
    public String toString() {
        return "Professor [id=" + id +
        ", professorCode=" + professorCode +
        ", name=" + firstName + " " + lastName +
        ", fiscal code=" + fiscalCode +
        ", email=" + username +
        "]";
    }

    // equals and hashCode
    @Override
    public int hashCode() {
        return Objects.hash(professorCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor)) return false;
        Professor other = (Professor) o;
        return Objects.equals(professorCode, other.professorCode);
    }


    // --- Private helper ---
    private String generateProfessorCode() {
        int code = professorCounter.getAndIncrement();
        return String.format("%08x", code);
    }

}


