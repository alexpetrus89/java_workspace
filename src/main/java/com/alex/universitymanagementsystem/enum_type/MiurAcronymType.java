package com.alex.universitymanagementsystem.enum_type;


/**
 * Enum representing MIUR acronyms used for identifying courses.
 */
public enum MiurAcronymType {

    INF("Informatica"),
    MAT("Matematica"),
    FIS("Fisica"),
    CHI("Chimica"),
    BIO("Biologia"),
    MED("Medicina"),
    LET("Lettere"),
    ECO("Economia"),
    GIU("Giurisprudenza"),
    PSI("Psicologia"),
    ING("Ingegneria");

    private final String description;

    MiurAcronymType(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.name(); // es. "INF"
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getCode() + " - " + description;
    }
}

