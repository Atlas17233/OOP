package com.li.oop.constant.enums;

public enum CargoType {
    BULK((float) 11.6, 100000),
    LIQUID((float) 35.4, 300000),
    CONTAINER((float) 1.7, 15000);

    public final float CRANE_MINUTE_PRODUCTIVITY;
    public final int MAX_WEIGHT;

    CargoType(float craneMinuteProductivity, int maxWeight) {
        this.CRANE_MINUTE_PRODUCTIVITY = craneMinuteProductivity;
        this.MAX_WEIGHT = maxWeight;
    }
}