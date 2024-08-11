package com.thegame.business.enums;

public enum ResourceType {
    WOOD(2, "wood"),
    STONE(3, "stone");

    private final long value;
    private final String fullName;

    ResourceType(long value, String fullName) {
        this.value = value;
        this.fullName = fullName;
    }

    public long getValue() {
        return value;
    }

    public String getFullName() {
        return fullName;
    }

    public static ResourceType fromValue(long value) {
        for (ResourceType type : ResourceType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static ResourceType fromName(String fullName) {
        for (ResourceType type : ResourceType.values()) {
            if (type.getFullName().equalsIgnoreCase(fullName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown name: " + fullName);
    }
}