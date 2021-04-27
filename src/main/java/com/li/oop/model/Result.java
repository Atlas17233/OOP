package com.li.oop.model;

import com.li.oop.constant.enums.*;

import java.util.*;

public final class Result implements Comparable<Result> {
    public final CargoType type;
    public int nUnloadedShips;
    public long averageUnloadingQueueLength;
    public long averageWaitingTimeInQueue;
    public long averageUnloadDelay;
    public int maxUnloadDelay;
    public long totalPenalty;
    public final int nCrane;
    public final List<Port.UnloadedShip> unloadedShips = new LinkedList<>();

    public Result(CargoType type, int nCrane) {
        this.type = type;
        this.nCrane = nCrane;
    }

    @Override
    public int compareTo(Result result) {
        return Long.compare(this.totalPenalty, result.totalPenalty);
    }
}
