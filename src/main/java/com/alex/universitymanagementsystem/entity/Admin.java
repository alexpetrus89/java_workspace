package com.alex.universitymanagementsystem.entity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.entity.immutable.AdminCode;
import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "ADMINS")
@Access(AccessType.PROPERTY)
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User {

    // instance variables
    private AdminCode adminCode;
    private static final AtomicInteger adminCounter = new AtomicInteger(000000);

    // constructors
    protected Admin() { super(); }

    public Admin(RegistrationForm form, PasswordEncoder encoder) {
        super(form, encoder);
        this.adminCode = new AdminCode(generateAdminCode());
    }


    public Admin(RegistrationForm form, PasswordEncoder passwordEncoder, AdminCode adminCode) {
        super(form, passwordEncoder);
        this.adminCode = adminCode;
    }

    public Admin(String username, String firstName, String lastName, String fiscalCode, String adminCode) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = new FiscalCode(fiscalCode);
        this.adminCode = new AdminCode(adminCode);
    }


    // getters
    @Embedded
    public AdminCode getAdminCode() { return adminCode; }

    // setters
    public void setAdminCode(AdminCode adminCode) { this.adminCode = adminCode; }


    // --- Object methods ---
    @Override
    public String toString() {
        return "Admin [id=" + id +
        ", adminCode=" + adminCode +
        ", name=" + firstName + " " + lastName +
        ", fiscal code=" + fiscalCode +
        ", email=" + username +
        "]";
    }

    // equals and hashCode
    @Override
    public int hashCode() {
        return Objects.hash(adminCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        Admin other = (Admin) o;
        return Objects.equals(adminCode, other.adminCode);
    }


    // --- Private helper ---
    private String generateAdminCode() {
        int code = adminCounter.getAndIncrement();
        return String.format("%08x", code);
    }

}
