package me.egg82.tcpp.registries;

import ninja.egg82.patterns.registries.Registry;

import java.util.UUID;

public class MarkovSpeechRegistry extends Registry<UUID, String> {
    //vars

    //constructor
    public MarkovSpeechRegistry() {
        super(UUID.class, String.class);
    }

    //public

    //private

}
