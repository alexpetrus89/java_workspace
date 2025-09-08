package com.alex.universitymanagementsystem.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.entity.immutable.UserId;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.UpdateForm;
import com.alex.universitymanagementsystem.enum_type.RoleType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS", schema = "public")
@Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED)
public class User implements UserDetails {

    // instance variables
    @EmbeddedId
    protected final UserId id;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    protected RoleType role;

    @OneToOne(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY,
        optional = false
    )
    protected Address address;


    // spring security field
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked;

    @Column(name = "credentials_expiration")
    private LocalDate credentialsExpirationDate;


    // constructors
    public User() {
        this.id = UserId.newId();
    }

    public User(RegistrationForm form, PasswordEncoder encoder) {
        this.id = UserId.newId();
        this.username = form.getUsername();
        this.password = encoder.encode(form.getPassword());
        this.firstName = form.getFirstName();
        this.lastName = form.getLastName();
        this.dob = form.getDob();
        this.fiscalCode = new FiscalCode(form.getFiscalCode());
        this.phone = form.getPhone();
        this.role = form.getRole();
        this.address = new Address(form.getStreet(), form.getCity(), form.getState(), form.getZip());
        this.enabled = true;
        this.accountLocked = false;
        this.credentialsExpirationDate = LocalDate.of(2100, 1, 1);

    }

    public User(UpdateForm form) {
        this.id = UserId.newId();
        this.username = form.getUsername();
        this.firstName = form.getFirstName();
        this.lastName = form.getLastName();
        this.dob = form.getDob();
        this.fiscalCode = new FiscalCode(form.getFiscalCode());
        this.phone = form.getPhone();
        this.role = form.getRole();
        this.address = new Address(form.getStreet(), form.getCity(), form.getState(), form.getZip());
        this.enabled = true;
        this.accountLocked = false;
        this.credentialsExpirationDate = LocalDate.of(2100, 1, 1);
    }


    // getters
    public UserId getId() { return id; }

    @Override
    public String getUsername() { return username; }
    @Override
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDob() { return dob; }
    public FiscalCode getFiscalCode() { return fiscalCode; }
    public String getPhone() { return phone; }
    public RoleType getRole() { return role; }
    public Address getAddress() { return address; }


    // setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public void setFiscalCode(FiscalCode fiscalCode) { this.fiscalCode = fiscalCode; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(RoleType role) { this.role = role; }
    public void setAddress(Address address) { this.address = address; }


    // --- UserDetails methods ---

    // permission state
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isEnabled() { return enabled; }

    @Override
    public boolean isAccountNonLocked() { return !accountLocked; }

    @Override
    public boolean isCredentialsNonExpired() {
        if (credentialsExpirationDate == null) return true; // mai scaduta
        return LocalDate.now().isBefore(credentialsExpirationDate);
    }

    @Override
    public boolean isAccountNonExpired() { return true; }


    // change permission state
    public void lockAccount() { this.accountLocked = true; }
    public void unlockAccount() {  this.accountLocked = false; }
    public void disableUser() { this.enabled = false; }
    public void enableUser() { this.enabled = true; }
    public void setPasswordExpirationDate(LocalDate date) { this.credentialsExpirationDate = date; }





    // --- Object methods ---
    @Override
    public String toString() {
        return "User [id=" + id +
            ", username='" + username + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", dob='" + dob +
            ", fiscalCode='" + fiscalCode.toString() + '\'' +
            ", phoneNumber='" + phone + '\'' +
            ", role=" + role +
            "]";
    }

    // Equals & HashCode based on ID
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
