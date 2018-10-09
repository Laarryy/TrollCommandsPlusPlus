package me.egg82.tcpp.registries;

import ninja.egg82.patterns.registries.Registry;

import java.util.UUID;

public class MarkovRegistry extends Registry<UUID, UUID> {
    //vars

    //constructor
    public MarkovRegistry() {
        super(UUID.class, UUID.class);
    }

    //public

    //private

}
