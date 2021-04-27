package com.li.oop.model;

import com.li.oop.constant.enums.*;

import java.time.*;
import java.util.*;

public class Schedule {
    public final LocalDateTime tStart;
    public final List<Ship> list;

    public Schedule(LocalDateTime tStart, int initialCapacity) {
        this.tStart = tStart;
        list = new ArrayList<>(initialCapacity);
    }

    public void sort() {
        list.sort(Ship::compareTo);
    }

    public static class Ship implements Comparable<Ship> {
        public final int tArrival;
        public final String name;
        public final CargoType cargoType;
        public final float cargoWeight;
        public final int unloadingTerm;

        public Ship(int tArrival, String name, CargoType cargoType, float cargoWeight) {
            this.tArrival = tArrival;
            this.name = name;
            this.cargoType = cargoType;
            this.cargoWeight = cargoWeight;
            this.unloadingTerm = (int) (this.cargoWeight / cargoType.CRANE_MINUTE_PRODUCTIVITY);
        }

        @Override
        public int compareTo(Ship ship) {
            return this.tArrival - ship.tArrival;
        }
    }
}
