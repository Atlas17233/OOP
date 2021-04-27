package com.li.oop.model;

import com.li.oop.constant.enums.*;

import static com.li.oop.constant.consist.*;

class Formatter {
    static String toStringPeriod(long time) { // dd:hh:mm
        return String.format("%02d", time / MINUTES_IN_DAY) + ":"
                + String.format("%02d", (time % MINUTES_IN_DAY) / MyTimeUnit.HOUR.SCALE) + ":"
                + String.format("%02d", time % MyTimeUnit.HOUR.SCALE);
    }
}
