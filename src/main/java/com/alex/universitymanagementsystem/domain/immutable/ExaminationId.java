package com.alex.universitymanagementsystem.domain.immutable;

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
        this(UUID.randomUUID());
    }

}
