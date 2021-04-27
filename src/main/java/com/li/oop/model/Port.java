package com.li.oop.model;

import com.li.oop.constant.enums.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import static com.li.oop.constant.consist.*;
import static com.li.oop.constant.enums.MyTimeUnit.*;

public class Port extends ArrayList<Result> {
    private final LocalDateTime tStart;
    private final Map<CargoType, List<Ship>> shipsMap;

    Port(LocalDateTime tStart, Map<CargoType, List<Ship>> shipsMap) {
        this.tStart = tStart;
        this.shipsMap = shipsMap;
    }

    public ArrayList<Result> getResult() throws InterruptedException, ExecutionException {
        if (isEmpty()) {
            ExecutorCompletionService<Result> service = new ExecutorCompletionService<>(Executors.newFixedThreadPool(CARGO_TYPES.length));
            EnumSet.allOf(CargoType.class).parallelStream().forEach(type -> service.submit(new SingleTypePart(type)));
            for (int i = 0; i < CARGO_TYPES.length; ++i) {
                add(service.take().get());
            }
        }
        return this;
    }

    private final class SingleTypePart implements Callable<Result> {
        private final CargoType type;
        private final Queue<Result> results = new PriorityQueue<>();

        private SingleTypePart(CargoType type) {
            this.type = type;
        }

        @Override
        public Result call() throws InterruptedException, ExecutionException {
            final int sizeStep = 5;
            int nStep = 0;
            ExecutorCompletionService<Result> service = new ExecutorCompletionService<>(Executors.newFixedThreadPool(sizeStep));
            do {
                for (int i = 1; i <= sizeStep; ++i) {
                    service.submit(new UnloadingQueue(nStep * sizeStep + i));
                }
                for (int i = 0; i < sizeStep; ++i) {
                    results.offer(service.take().get());
                }
            } while (results.peek().nCrane == ++nStep * sizeStep);
            return results.poll();
        }

        private final class UnloadingQueue implements Comparable<UnloadingQueue>, Callable<Result> {
            private final Result result;

            private final Queue<ArrayList<Crane>> cranes;
            private Iterator<Ship> shipIter = shipsMap.get(type).iterator();
            private Ship currentShip;
            private ArrayList<Crane> currentCranes;

            private int nArrived = 0;

            private UnloadingQueue(int nCrane) {
                this.result = new Result(type, nCrane);
                this.cranes = new PriorityQueue<>(nCrane, (l, r) -> {
                    if (l.get(0).tCurrent == r.get(0).tCurrent) {
                        return r.size() - l.size();
                    }
                    return l.get(0).tCurrent - r.get(0).tCurrent;
                }) {{
                    for (int i = 0; i < nCrane / MAX_CRANES_FOR_SHIP; ++i) {
                        add(new ArrayList<>(MAX_CRANES_FOR_SHIP) {{
                            for (int i = 0; i < MAX_CRANES_FOR_SHIP; ++i) {
                                add(new Crane());
                            }
                        }});
                    }
                    if (nCrane % MAX_CRANES_FOR_SHIP > 0) {
                        add(new ArrayList<>(nCrane % MAX_CRANES_FOR_SHIP) {{
                            for (int i = 0; i < nCrane % MAX_CRANES_FOR_SHIP; ++i) {
                                add(new Crane());
                            }
                        }});
                    }
                }};
            }

            @Override
            public int compareTo(UnloadingQueue unloadingQueue) {
                return Long.compare(this.result.totalPenalty, unloadingQueue.result.totalPenalty);
            }

            @Override
            public Result call() throws InterruptedException {
                while (cranes.peek().get(0).tCurrent < SIMULATION_MINUTES_PERIOD && shipIter.hasNext()) {
                    currentShip = shipIter.next();
                    currentCranes = cranes.poll();
                    ++nArrived;
                    AtomicBoolean isExecuted = new AtomicBoolean(false);
                    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CRANES_FOR_SHIP);
                    currentCranes.parallelStream().forEach(crane -> {
                        crane.isExecuted = isExecuted;
                        executor.execute(crane);
                    });
                    executor.shutdown();
                    while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                        Thread.yield();
                    }
                    cranes.offer(currentCranes);
                }
                while (shipIter.hasNext()) {
                    currentShip = shipIter.next();
                    if (currentShip.tArrival < SIMULATION_MINUTES_PERIOD) {
                        ++nArrived;
                        int tWaiting = SIMULATION_MINUTES_PERIOD - currentShip.tArrival;
                        result.averageWaitingTimeInQueue += tWaiting;
                        result.totalPenalty += tWaiting;
                    }
                }
                result.nUnloadedShips = result.unloadedShips.size();
                result.averageUnloadingQueueLength /= SIMULATION_MINUTES_PERIOD;
                result.averageWaitingTimeInQueue /= (nArrived > 0) ? nArrived : 1;
                result.averageUnloadDelay /= (result.nUnloadedShips > 0) ? result.nUnloadedShips : 1;
                result.totalPenalty /= HOUR.SCALE;
                result.totalPenalty *= ONE_HOUR_PENALTY;
                result.totalPenalty += (long) result.nCrane * CRANE_COST;
                return result;
            }

            private final class Crane implements Runnable {
                private int tCurrent;
                private AtomicBoolean isExecuted;

                @Override
                public void run() {
                    boolean isExecute = false;
                    if (isExecuted.compareAndSet(false, true)) {
                        isExecute = true;
                    }
                    int tWaiting = 0;
                    if (tCurrent <= currentShip.tArrival) {
                        tCurrent = currentShip.tArrival;
                    } else {
                        tWaiting = tCurrent - currentShip.tArrival;
                        if (isExecute) {
                            result.averageWaitingTimeInQueue += tWaiting;
                            result.totalPenalty += tWaiting;
                        }
                        tCurrent += tWaiting;
                    }
                    int tUnloading = currentShip.unloadingTerm / currentCranes.size();
                    int tLeft = SIMULATION_MINUTES_PERIOD - tCurrent;
                    tCurrent += tUnloading;
                    if (tUnloading <= tLeft) {
                        if (isExecute) {
                            result.averageUnloadingQueueLength += tUnloading;
                            result.unloadedShips.add(new UnloadedShip(currentShip, tWaiting, tUnloading));
                            int tDelay = Math.min(currentShip.tDelay, SIMULATION_MINUTES_PERIOD - tCurrent);
                            result.totalPenalty += tDelay;
                            result.averageUnloadDelay += tDelay;
                            result.maxUnloadDelay = Math.max(result.maxUnloadDelay, tDelay);
                        }
                        tCurrent += currentShip.tDelay;
                    } else if (isExecute) {
                        result.averageUnloadingQueueLength += tLeft;
                    }
                }
            }
        }
    }

    static class Ship extends Schedule.Ship {
        private final int tDelay;

        Ship(Schedule.Ship ship, int tArrivalDeviation, int tDelay) {
            super(ship.tArrival + tArrivalDeviation, ship.name, ship.cargoType, ship.cargoWeight);
            this.tDelay = tDelay;
        }
    }

    final class UnloadedShip implements Comparable<UnloadedShip> {
        final String name;
        final LocalDateTime tArrival;
        final int tWaiting;
        final LocalDateTime tStartUnloading;
        final int tUnloading;

        private UnloadedShip(Ship ship, int tWaiting, int tUnloading) {
            this.name = ship.name;
            this.tArrival = tStart.plusMinutes(ship.tArrival);
            this.tWaiting = tWaiting;
            this.tStartUnloading = this.tArrival.plusMinutes(this.tWaiting);
            this.tUnloading = tUnloading;
        }

        @Override
        public int compareTo(UnloadedShip ship) {
            return this.tArrival.compareTo(ship.tArrival);
        }
    }
}
