package com.alex.studentmanagementsystem.utility;

public class Builder {

    // instance variables
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    // getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    // setters
    public void withUsername(String username) {
        this.username = username;
    }

    public void withPassword(String password) {
        this.password = password;
    }

    public void withFullname(String fullname) {
        this.fullname = fullname;
    }

    public void withStreet(String street) {
        this.street = street;
    }

    public void withCity(String city) {
        this.city = city;
    }

    public void withState(String state) {
        this.state = state;
    }

    public void withZip(String zip) {
        this.zip = zip;
    }

    public void withPhone(String phone) {
        this.phone = phone;
    }

}
