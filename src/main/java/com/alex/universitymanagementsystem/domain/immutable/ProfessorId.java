package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;
import java.util.UUID;

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
