package com.alex.universitymanagementsystem.domain.immutable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.alex.universitymanagementsystem.enum_type.MiurAcronymType;

import jakarta.persistence.Embeddable;

/**
 * Immutable value object representing the unique code of a Course.
 * Format: <MIUR_ACRONYM>-<UUID>
 */
@Embeddable
public record MiurCourseCode(String value) implements Serializable {

    public MiurCourseCode {
        Objects.requireNonNull(value, "Course code cannot be null");
        if (value.isBlank())
            throw new IllegalArgumentException("Course code cannot be blank");
        if (!value.matches("^[A-Z]{3}-[0-9a-fA-F\\-]{36}$"))
            throw new IllegalArgumentException("Invalid course code format: " + value);
    }

    /**
     * Factory method to generate a new course code from a MIUR acronym.
     *
     * @param acronym the MIUR acronym (e.g., INF, MAT, FIS)
     * @return a new CourseCode
     */
    public static MiurCourseCode generate(MiurAcronymType acronym) {
        Objects.requireNonNull(acronym, "MIUR acronym cannot be null");
        return new MiurCourseCode(acronym.getCode() + "-" + UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value;
    }
}

