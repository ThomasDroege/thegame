package com.thegame.business.enums;

public enum ResourceType {
    WOOD(2, "wood"),
    STONE(3, "stone");

    private final long value;
    private final String name;

    ResourceType(long value, String name) {
        this.value = value;
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ResourceType fromValue(long value) {
        for (ResourceType type : ResourceType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static ResourceType fromName(String name) {
        for (ResourceType type : ResourceType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown name: " + name);
    }
}