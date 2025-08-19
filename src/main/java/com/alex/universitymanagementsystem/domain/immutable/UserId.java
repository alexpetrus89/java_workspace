package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(UUID id) implements Serializable {

    public UserId {
        Assert.notNull(id, "id must not be null");
    }

    public UserId() {
        this((UUID) null);
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public UserId(String id) {
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
