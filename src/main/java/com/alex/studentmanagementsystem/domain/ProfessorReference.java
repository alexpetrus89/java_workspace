package com.alex.studentmanagementsystem.domain;

import org.springframework.util.Assert;

import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;

public record ProfessorReference(String name, UniqueCode uniqueCode) {

    public ProfessorReference {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(uniqueCode, "unique code must not be null");
    }

}
