package com.li.oop.constant;

import com.li.oop.constant.enums.*;

import static com.li.oop.constant.enums.MyTimeUnit.*;

public final class consist {
    public static final int MINUTES_IN_DAY = DAY.SCALE * HOUR.SCALE;

    public static final int PRE_ARRIVAL_DAYS_DEVIATION = 7;
    public static final int POST_ARRIVAL_DAYS_DEVIATION = 7;
    public static final int SIMULATION_DAYS_PERIOD = 30;
    public static final int SIMULATION_HOURS_PERIOD = SIMULATION_DAYS_PERIOD * DAY.SCALE;
    public static final int SIMULATION_MINUTES_PERIOD = SIMULATION_HOURS_PERIOD * HOUR.SCALE;

    public static final int MAX_CRANES_FOR_SHIP = 2;
    public static final int END_DELAY_DAYS_DEVIATION = MINUTES_IN_DAY;

    public static final int ONE_HOUR_PENALTY = 100;
    public static final int CRANE_COST = 30000;

    public static final CargoType[] CARGO_TYPES = CargoType.values();

    public static final String SCHEDULE_PATH = "data/Schedule.json";
    public static final String RESULT_PATH = "data/Result.json";

    public static final String URL1 = "http://localhost:8080/service1/";
    public static final String URL2 = "http://localhost:8080/service2/";
    public static final String URL3 = "http://localhost:8080/service3/";
}
