package com.alex.studentmanagementsystem.domain.immutable;

import java.util.UUID;
import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProfessorId(UUID id) implements Serializable {

    public ProfessorId {
        Assert.notNull(id, "id must not be null");
    }

    public ProfessorId() {
        this(UUID.randomUUID());
    }

}
