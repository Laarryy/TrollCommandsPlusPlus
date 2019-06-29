package me.egg82.tcpp.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AnalyticsHelper {
    private AnalyticsHelper() {}

    private static ConcurrentMap<String, Integer> commands = new ConcurrentHashMap<>();

    public static void incrementCommand(String command) {
        commands.compute(command, (k, v) -> {
           if (v == null) {
               return 1;
           }
           return v + 1;
        });
    }

    public static ConcurrentMap<String, Integer> getAndEraseCommands() {
        ConcurrentMap<String, Integer> retVal = commands;
        commands = new ConcurrentHashMap<>();
        return retVal;
    }
}
