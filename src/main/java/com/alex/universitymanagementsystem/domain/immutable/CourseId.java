package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

@Embeddable
public record CourseId(UUID id) implements Serializable {

    // costruttore canonico: JPA richiede che i campi non siano null quando usati
    public CourseId {
        Assert.notNull(id, "id must not be null");
    }

    // costruttore di default usato da JPA (deve delegare al canonico)
    public CourseId() {
        this((UUID) null); // delega al costruttore canonico
    }

    // factory method per generare un nuovo ID
    public static CourseId newId() {
        return new CourseId(UUID.randomUUID());
    }

    // costruttore per stringhe
    public CourseId(String id) {
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
