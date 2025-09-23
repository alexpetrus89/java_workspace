package com.alex.universitymanagementsystem.entity.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UniqueCode(
    @Column(name = "unique_code", nullable = false)
    String code
) implements Serializable {

    public UniqueCode {
        Assert.notNull(code, "unique code must not be null");
        Assert.isTrue(code.length() == 8, "unique code must be a string of exactly 8 characters");
        Assert.isTrue(code.matches("\\w{8}"), "unique code must be a string of exactly 8 characters");
    }

    @Override
    public String toString() {
        return code;
    }

}
