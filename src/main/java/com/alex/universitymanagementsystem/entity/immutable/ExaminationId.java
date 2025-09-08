package com.alex.universitymanagementsystem.entity.immutable;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record ExaminationId(UUID id) implements Serializable {

    public ExaminationId {
        Assert.notNull(id, "id must not be null");
    }

    public ExaminationId() {
        this((UUID) null);
    }

    public static ExaminationId newId() {
        return new ExaminationId(UUID.randomUUID());
    }

    public ExaminationId(String id) {
        this(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public UUID getId() {
        return id;
    }

}
