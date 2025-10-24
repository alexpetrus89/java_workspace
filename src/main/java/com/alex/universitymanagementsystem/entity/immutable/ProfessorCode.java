package com.alex.universitymanagementsystem.entity.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ProfessorCode(
    @Column(name = "professor_code", nullable = false)
    String code
) implements Serializable {

    public ProfessorCode {
        Assert.notNull(code, "professor code must not be null");
        Assert.isTrue(code.length() == 8, "professor code must be a string of exactly 8 characters");
        Assert.isTrue(code.matches("\\w{8}"), "professor code must be a string of exactly 8 characters");
    }

    @Override
    public String toString() {
        return code;
    }

}
