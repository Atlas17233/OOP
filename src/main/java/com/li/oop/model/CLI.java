package com.li.oop.model;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import static com.li.oop.constant.consist.*;

import static java.lang.System.*;

public class CLI {
    private static final Scanner input = new Scanner(in);
    private static final Map<String, Method> Command;
    static {
        Map<String, Method> map = null;
        try {
            map = Map.of(
                    "service1", CLI.class.getDeclaredMethod("service1"),
                    "service2", CLI.class.getDeclaredMethod("service2"),
                    "service3", CLI.class.getDeclaredMethod("service3")
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Command = map;
    }

    private static final Map<String, String> pathMap = Map.of(
            "service1", SCHEDULE_PATH,
            "service3", RESULT_PATH
    );

    public static void run() throws InvocationTargetException, IllegalAccessException {
        while (input.hasNext()) {
            out.println(Command.get(input.next().toLowerCase()).invoke(null));
        }
    }

    private static String service1() {
        int n = input.nextInt();
        Schedule schedule = Generator.generateSchedule(n);
        return Json.toJson(schedule);
    }

    private static String service2() throws InvocationTargetException, IllegalAccessException {
        String command = input.next().toLowerCase();
        String s = (String) Command.get(command).invoke(null);
        FileIO.write(pathMap.get(command), s);
        return pathMap.get(command) + " generated";
    }

    private static String service3() throws ExecutionException, InterruptedException {
        return Json.toJson(Generator.generatePort(Json.toSchedule(FileIO.read(SCHEDULE_PATH))).getResult());
    }
}
