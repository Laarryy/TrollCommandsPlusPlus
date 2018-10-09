package me.egg82.tcpp.registries;

import java.util.UUID;

import org.bukkit.Location;

import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.registries.Registry;

public class LsdRegistry extends Registry<UUID, IConcurrentSet<Location>> {
    //vars

    //constructor
    @SuppressWarnings("unchecked")
    public LsdRegistry() {
        super(new UUID[0], new IConcurrentSet[0]);
    }

    //public

    //private

}
