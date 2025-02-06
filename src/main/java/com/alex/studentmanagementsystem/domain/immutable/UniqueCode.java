package com.alex.studentmanagementsystem.domain.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record UniqueCode(String code) implements Serializable {

    public UniqueCode{
        Assert.notNull(code, "unique code must not be null");
        Assert.isTrue(code.length() == 8, "unique code must be a string of exactly 8 digits");
        Assert.isTrue(code.matches("\\w{8}"), "unique code must be a string of exactly 8 digits");
    }


}
