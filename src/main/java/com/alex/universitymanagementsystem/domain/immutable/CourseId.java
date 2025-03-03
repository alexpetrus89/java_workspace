package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record CourseId(UUID id) implements Serializable {

    public CourseId {
        Assert.notNull(id, "id must not be null");
    }

    public CourseId() {
        this(UUID.randomUUID());
    }

}
