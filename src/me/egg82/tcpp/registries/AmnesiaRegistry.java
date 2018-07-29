package me.egg82.tcpp.registries;

import java.util.UUID;

import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.patterns.registries.Registry;

public class AmnesiaRegistry extends Registry<UUID, IConcurrentDeque<String>> {
	//vars
	
	//constructor
	@SuppressWarnings("unchecked")
	public AmnesiaRegistry() {
		super(new UUID[0], new IConcurrentDeque[0]);
	}
	
	//public
	
	//private
	
}
