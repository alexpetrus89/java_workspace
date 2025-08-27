package com.alex.universitymanagementsystem.dto;
import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.annotation.UniqueUsername;
import com.alex.universitymanagementsystem.annotation.ValidBirthDate;
import com.alex.universitymanagementsystem.annotation.ValidFiscalCode;
import com.alex.universitymanagementsystem.annotation.ValidPassword;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.utils.PasswordCarrier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class RegistrationForm implements Serializable, PasswordCarrier {

    // instance variables
    @NotBlank(message = "username is required")
    @Size(min = 4, max = 30, message = "username must be between 4 and 30 characters")
    @UniqueUsername
    private String username;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirm;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @ValidBirthDate
    private LocalDate dob;

    @NotBlank(message = "Fiscal code is required")
    @ValidFiscalCode
    private String fiscalCode;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "\\d{5}", message = "ZIP code must be 5 digits")
    private String zip;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+?\\d{9,15}", message = "Phone number is invalid")
    private String phone;

    @NotNull(message = "Role is required")
    private RoleType role;


    // constructors
    public RegistrationForm() {}

    public RegistrationForm(Builder builder) {
        this.username = builder.getUsername();
        this.password = builder.getPassword();
        this.confirm = builder.getConfirm();
        this.firstName = builder.getFirstName();
        this.lastName = builder.getLastName();
        this.dob = builder.getDob();
        this.fiscalCode = builder.getFiscalCode();
        this.street = builder.getStreet();
        this.city = builder.getCity();
        this.state = builder.getState();
        this.zip = builder.getZip();
        this.phone = builder.getPhone();
        this.role = builder.getRole();
    }

    // getters
    public String getUsername() { return username; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getConfirm() { return confirm; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDob() { return dob; }
    public String getFiscalCode() { return fiscalCode; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }
    public String getPhone() { return phone; }
    public RoleType getRole() { return role; }


    // setters
    public void setUsername(String username) { this.username = username; }
    @Override
    public void setPassword(String password) { this.password = password; }
    @Override
    public void setConfirm(String confirm) { this.confirm = confirm; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public void setFiscalCode(String fiscalCode) { this.fiscalCode = fiscalCode.toUpperCase(); }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setZip(String zip) { this.zip = zip; }
    public void setPhone(String phone) { this.phone = (phone == null || phone.isEmpty()) ? "N/A" : phone; }
    public void setRole(RoleType role) { this.role = role; }


    // methods
    private Builder toBuilder() {
        Builder builder = new Builder();
        builder.withUsername(username);
        builder.withPassword(password);
        builder.withConfirm(confirm);
        builder.withFirstName(firstName);
        builder.withLastName(lastName);
        builder.withDob(dob);
        builder.withFiscalCode(fiscalCode);
        builder.withStreet(street);
        builder.withCity(city);
        builder.withState(state);
        builder.withZip(zip);
        builder.withPhone(phone);
        builder.withRole(role);
        return builder;
    }

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(toBuilder(), passwordEncoder);
    }

    public Student toStudent(PasswordEncoder passwordEncoder) {
        return new Student(toBuilder(), passwordEncoder);
    }

    public Professor toProfessor(PasswordEncoder passwordEncoder) {
        return new Professor(toBuilder(), passwordEncoder);
    }



}
