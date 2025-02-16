package com.alex.studentmanagementsystem.domain.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record Register(String register) implements Serializable {

    public Register {
        Assert.notNull(register, "register must not be null");
        Assert.isTrue(register.length() == 6, "register must be a string of exactly 6 digits");
        Assert.isTrue(register.matches("\\d{6}"), "register must be a string of exactly 6 digits");
    }

    @Override
    public String toString() {
        return String.format(register);
    }

}
