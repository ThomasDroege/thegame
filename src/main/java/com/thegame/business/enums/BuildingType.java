package com.thegame.business.enums;

public enum BuildingType {
    CITY_HALL(1, "city hall", -1),
    MILL(2, "mill", ResourceType.FOOD.getValue()),
    LUMBERJACK(3, "lumberjack", ResourceType.WOOD.getValue()),
    MASON(4, "mason", ResourceType.STONE.getValue()),
    IRON_MINE(5, "iron mine", ResourceType.IRON.getValue());

    private final long value;
    private final String fullName;
    private final long producesResource;


    BuildingType(long value, String fullName, long producesResource) {
        this.value = value;
        this.fullName = fullName;
        this.producesResource = producesResource;
    }

    public long getValue() {
        return value;
    }

    public String getFullName() {
        return fullName;
    }

    public long getProducesResource() {
        return producesResource;
    }

    public static BuildingType fromValue(long value) {
        for (BuildingType type : BuildingType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static BuildingType fromFullName(String fullName) {
        for (BuildingType type : BuildingType.values()) {
            if (type.getFullName().equalsIgnoreCase(fullName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown name: " + fullName);
    }

    public static Long getResourceIdFromBuildingTypeId(long buildingTypeId) {
        for (BuildingType type : BuildingType.values()) {
            if (type.getValue() == buildingTypeId) {
                return type.getProducesResource();
            }
        }
        throw new IllegalArgumentException("Unknown value: " + buildingTypeId);
    }

}