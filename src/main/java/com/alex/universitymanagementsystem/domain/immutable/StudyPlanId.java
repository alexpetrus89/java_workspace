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
        this(UUID.randomUUID());
    }

}
