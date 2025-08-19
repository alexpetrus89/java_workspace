package com.alex.universitymanagementsystem.domain;

import java.io.Serializable;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "ADDRESSES")
@Access(AccessType.PROPERTY)
public class Address implements Serializable {

    private static final long serialVersionUID = 5160319758849739473L;

    // instance variables
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    // default constructor
    public Address() {}

    // constructor
    public Address(String street, String city, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    // getters
    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "address_id", schema = "public")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "address_id")
    public Long getId() {
        return id;
    }

    @Column(name = "street", nullable = false, unique = false, length = 50, columnDefinition = "TEXT")
    @NotBlank(message = "Street is mandatory")
    public String getStreet() {
        return street;
    }

    @Column(name = "city", nullable = false, unique = false, length = 50)
    @NotBlank(message = "City is mandatory")
    public String getCity() {
        return city;
    }

    @Column(name = "state", nullable = false, unique = false, length = 50)
    @NotBlank(message = "State is mandatory")
    public String getState() {
        return state;
    }

    @Column(name = "zip_code", nullable = false, unique = false, length = 50)
    @NotBlank(message = "ZIP code is mandatory")
    @Pattern(regexp = "\\d{5}", message = "ZIP code must be a 5-digit number")
    public String getZipCode() {
        return zipCode;
    }


    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


}
