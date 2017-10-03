package me.egg82.tcpp.services.registries;

import java.util.UUID;

import ninja.egg82.patterns.ExpiringRegistry;

public class TripRegistry extends ExpiringRegistry<UUID> {
	//vars
	
	//constructor
	public TripRegistry() {
		super(UUID.class, 3500L);
	}
	
	//public
	
	//private
	
}
