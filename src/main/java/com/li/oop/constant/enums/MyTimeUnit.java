package com.li.oop.constant.enums;

public enum MyTimeUnit {
    MINUTE(1),
    HOUR(60),
    DAY(24);

    public final int SCALE;

    MyTimeUnit(int scale) {
        this.SCALE = scale;
    }
}