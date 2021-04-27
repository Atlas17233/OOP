package com.li.oop.model;

import com.github.javafaker.Faker;
import com.li.oop.constant.enums.*;

import java.security.*;
import java.time.*;
import java.util.*;

import static com.li.oop.constant.consist.*;

public class Generator {
    private static final Faker faker = new Faker();
    private static final SecureRandom random = new SecureRandom();

    private static Schedule.Ship generateShip() {
        int tPlannedArrival = random.nextInt(SIMULATION_MINUTES_PERIOD);
        CargoType cargoType = CARGO_TYPES[random.nextInt(CARGO_TYPES.length)];
        return new Schedule.Ship(tPlannedArrival, faker.name().lastName(), cargoType, generateWeight(cargoType));
    }

    public static Schedule generateSchedule(int n) {
        Schedule schedule = new Schedule(LocalDateTime.now(), n);
        for (int i = 0; i < n; ++i) {
            schedule.list.add(generateShip());
        }
        schedule.sort();
        return schedule;
    }

    private static int generateDelay() {
        return random.nextInt(END_DELAY_DAYS_DEVIATION);
    }

    private static Port.Ship generateArrivalShip(Schedule.Ship ship) {
        int tArrivalDeviation = random.nextInt(PRE_ARRIVAL_DAYS_DEVIATION + POST_ARRIVAL_DAYS_DEVIATION)
                - PRE_ARRIVAL_DAYS_DEVIATION;
        return new Port.Ship(ship, tArrivalDeviation, generateDelay());
    }

    public static Port generatePort(Schedule schedule) {
        Map<CargoType, List<Port.Ship>> shipsMap = new HashMap<>() {{
            EnumSet.allOf(CargoType.class).parallelStream().forEach(type -> put(type, new LinkedList<>()));
        }};
        schedule.list.parallelStream().forEachOrdered(ship -> shipsMap.get(ship.cargoType).add(generateArrivalShip(ship)));
        EnumSet.allOf(CargoType.class).parallelStream().forEach(type -> shipsMap.get(type).sort(Port.Ship::compareTo));
        return new Port(schedule.tStart, shipsMap);
    }

    private static float generateWeight(CargoType type) {
        return switch (type) {
            case CONTAINER -> random.nextInt(type.MAX_WEIGHT);
            default -> random.nextFloat() * type.MAX_WEIGHT;
        };
    }
}
