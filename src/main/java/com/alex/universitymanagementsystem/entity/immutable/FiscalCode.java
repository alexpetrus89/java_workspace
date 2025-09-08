package com.alex.universitymanagementsystem.entity.immutable;

import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record FiscalCode(String fiscalCode) implements Serializable {

    public FiscalCode{
        Assert.notNull(fiscalCode, "fiscal code must not be null");
        Assert.isTrue(fiscalCode.length() == 16, "fiscal code must be a string of exactly 16 characters and digits");
        Assert.isTrue(fiscalCode.matches("\\w{16}"), "fiscal code must be a string of exactly 16 characters and digits");
    }

    @Override
    public String toString() {
        return fiscalCode;
    }

}
