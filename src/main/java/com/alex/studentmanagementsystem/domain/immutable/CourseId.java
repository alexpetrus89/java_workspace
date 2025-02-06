package com.alex.studentmanagementsystem.domain.immutable;

import java.util.UUID;
import java.io.Serializable;

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
