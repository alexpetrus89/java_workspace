package com.alex.universitymanagementsystem.entity.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Register(
    @Column(name = "register", nullable = false, length = 6)
    String register) implements Serializable {

    public Register {
        Assert.notNull(register, "register must not be null");
        Assert.isTrue(register.matches("\\d{6}"), "register must be a string of exactly 6 digits");
    }

    @Override
    public String toString() {
        return String.format(register);
    }


}
