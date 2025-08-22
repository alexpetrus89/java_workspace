package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record UniqueCode(String code) implements Serializable {

    public UniqueCode{
        Assert.notNull(code, "unique code must not be null");
        Assert.isTrue(code.length() == 8, "unique code must be a string of exactly 8 characters");
        Assert.isTrue(code.matches("\\w{8}"), "unique code must be a string of exactly 8 characters");
    }

    @Override
    public String toString() {
        return code;
    }

}
