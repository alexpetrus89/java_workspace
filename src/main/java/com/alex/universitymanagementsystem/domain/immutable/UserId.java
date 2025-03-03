package com.alex.universitymanagementsystem.domain.immutable;

import java.util.UUID;
import java.io.Serializable;

import org.springframework.util.Assert;



import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(UUID id) implements Serializable {

    public UserId {
        Assert.notNull(id, "id must not be null");
    }

    public UserId() {
        this(UUID.randomUUID());
    }

}
