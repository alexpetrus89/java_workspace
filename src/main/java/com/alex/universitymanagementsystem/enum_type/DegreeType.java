package com.alex.universitymanagementsystem.enum_type;

public enum DegreeType {

    BACHELOR("Bachelor's Degree", 1),
    MASTER("Master's Degree", 2),
    PHD("PhD", 3);

    private final String displayName;
    private final int level;

    DegreeType(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() { return displayName; }

    public int getLevel() { return level; }

}
