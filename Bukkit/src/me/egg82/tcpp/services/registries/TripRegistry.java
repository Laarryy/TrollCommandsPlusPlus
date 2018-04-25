package me.egg82.tcpp.services.registries;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import ninja.egg82.patterns.registries.VariableExpiringRegistry;

public class TripRegistry extends VariableExpiringRegistry<UUID> {
	//vars
	
	//constructor
	public TripRegistry() {
		super(UUID.class, 3500L, TimeUnit.MILLISECONDS);
	}
	
	//public
	
	//private
	
}
