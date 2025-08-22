package com.alex.universitymanagementsystem.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
import com.alex.universitymanagementsystem.domain.immutable.UserId;
import com.alex.universitymanagementsystem.dto.Builder;
import com.alex.universitymanagementsystem.enum_type.RoleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"users\"", schema = "public")
@Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED)
public class User implements UserDetails {

    // instance variables
    @EmbeddedId
    protected UserId id;
    @Column(name = "username", nullable = false, unique = true, length = 50)
    protected String username;
    @Column(name = "password", nullable = false)
    protected String password;
    @Column(name = "first_name", nullable = false, unique = false, length = 50)
    protected String firstName;
    @Column(name = "last_name", nullable = false, unique = false, length = 50)
    protected String lastName;
    @Column(name = "dob", nullable = false)
    protected LocalDate dob;
    @Embedded
    @Column(name = "fiscal_code", nullable = false, unique = true, length = 16)
    protected FiscalCode fiscalCode;
    @Column(name = "phone", nullable = false, length = 15)
    protected String phone;
    protected RoleType role;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, optional = false)
    protected Address address;

    // default constructor
    public User() {}

    // constructors
    public User(Builder builder, PasswordEncoder encoder) {
        this.id = new UserId(UUID.randomUUID());
        this.username = builder.getUsername();
        this.password = encoder.encode(builder.getPassword());
        this.firstName = builder.getFirstName();
        this.lastName = builder.getLastName();
        this.dob = builder.getDob();
        this.fiscalCode = new FiscalCode(builder.getFiscalCode().toUpperCase());
        this.phone = (builder.getPhone() == null || builder.getPhone().isEmpty()) ? "N/A" : builder.getPhone();
        this.role = builder.getRole();
        this.address = new Address(builder.getStreet(), builder.getCity(), builder.getState(), builder.getZip());
    }

    public User(Builder builder) {
        this.id = new UserId(UUID.randomUUID());
        this.username = builder.getUsername();
        this.firstName = builder.getFirstName();
        this.lastName = builder.getLastName();
        this.dob = builder.getDob();
        this.fiscalCode = new FiscalCode(builder.getFiscalCode());
        this.phone = (builder.getPhone() == null || builder.getPhone().isEmpty()) ? "N/A" : builder.getPhone();
        this.role = builder.getRole();
        this.address = new Address(builder.getStreet(), builder.getCity(), builder.getState(), builder.getZip());
    }


    // getters
    public UserId getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public FiscalCode getFiscalCode() {
        return fiscalCode;
    }

    public String getPhone() {
        return phone;
    }

    public RoleType getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }


    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setFiscalCode(FiscalCode fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public void setPhoneNumber(String phone) {
        this.phone = phone;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // toString
    @Override
    public String toString() {
        return "User {" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", dob='" + dob + '\'' +
            ", fiscalCode='" + fiscalCode.toString() + '\'' +
            ", street='" + address.getStreet() + '\'' +
            ", city='" + address.getCity() + '\'' +
            ", state='" + address.getState() + '\'' +
            ", zip='" + address.getZipCode() + '\'' +
            ", phoneNumber='" + phone + '\'' + '}';
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    // equals and hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return Objects.equals(id, other.id);
    }


}
