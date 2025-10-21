package com.alex.universitymanagementsystem.entity.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record AdminCode(
    @Column(name = "admin_code", nullable = false)
    String code
) implements Serializable {

    public AdminCode {
        Assert.notNull(code, "admin code must not be null");
        Assert.isTrue(code.length() == 8, "admin code must be a string of exactly 8 characters");
        Assert.isTrue(code.matches("\\w{8}"), "admin code must be a string of exactly 8 characters");
    }

    @Override
    public String toString() {
        return code;
    }

}
