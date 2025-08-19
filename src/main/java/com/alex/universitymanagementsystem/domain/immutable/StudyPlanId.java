package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record StudyPlanId(UUID id) implements Serializable {

    public StudyPlanId {
        Assert.notNull(id, "id must not be null");
    }

    public StudyPlanId() {
        this((UUID) null);
    }

    public static StudyPlanId newId() {
        return new StudyPlanId(UUID.randomUUID());
    }

    public StudyPlanId(String id) {
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
