package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record DegreeCourseId(UUID id) implements Serializable {

    public DegreeCourseId {
        Assert.notNull(id, "id must not be null");
    }

    public DegreeCourseId() {
        this((UUID) null);
    }

    public static DegreeCourseId newId() {
        return new DegreeCourseId(UUID.randomUUID());
    }

    public DegreeCourseId(String id) {
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
