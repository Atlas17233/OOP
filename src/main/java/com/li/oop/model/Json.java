package com.li.oop.model;

import com.google.gson.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

public class Json {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, context)
                    -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context)
                    -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            .registerTypeAdapter(Schedule.Ship.class, (JsonSerializer<Schedule.Ship>) (src, type, context) -> {
                JsonObject object = new JsonObject();
                object.add("tArrival", context.serialize(src.tArrival));
                object.add("name", context.serialize(src.name));
                object.add("cargoType", context.serialize(src.cargoType));
                switch (src.cargoType) {
                    case CONTAINER -> object.add("cargoWeight", context.serialize(Math.round(src.cargoWeight)));
                    default -> object.add("cargoWeight", context.serialize(src.cargoWeight));
                }
                object.add("unloadingTerm", context.serialize(src.unloadingTerm));
                return object;
            }).registerTypeAdapter(Port.UnloadedShip.class, (JsonSerializer<Port.UnloadedShip>) (src, type, context) -> {
                JsonObject object = new JsonObject();
                object.add("Ship name", context.serialize(src.name));
                object.add("Arrival time", context.serialize(src.tArrival));
                object.add("Waiting time", context.serialize(Formatter.toStringPeriod(src.tWaiting)));
                object.add("Start time", context.serialize(src.tStartUnloading));
                object.add("Unloading time", context.serialize(Formatter.toStringPeriod(src.tUnloading)));
                return object;
            }).registerTypeAdapter(Result.class, (JsonSerializer<Result>) (src, type, context) -> {
                JsonObject object = new JsonObject();
                object.add("Cargo type", context.serialize(src.type));
                object.add("Unloaded ships", context.serialize(src.nUnloadedShips));
                object.add("Average unloading queue length", context.serialize(src.averageUnloadingQueueLength));
                object.add("Average waiting time in queue", context.serialize(src.averageWaitingTimeInQueue));
                object.add("Max unload delay", context.serialize(src.maxUnloadDelay));
                object.add("Average unload delay", context.serialize(src.averageUnloadDelay));
                object.add("Total penalty", context.serialize(src.totalPenalty));
                object.add("cranes", context.serialize(src.nCrane));
                object.add("Unloaded list", context.serialize(src.unloadedShips));
                return object;
            }).setPrettyPrinting().disableHtmlEscaping().create();

    public static Schedule toSchedule(String string) {
        return gson.fromJson(string, Schedule.class);
    }

    public static String toJson(Schedule schedule) {
        return gson.toJson(schedule);
    }

    public static String toJson(ArrayList<Result> result) {
        return gson.toJson(result);
    }
}
