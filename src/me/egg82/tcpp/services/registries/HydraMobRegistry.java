package me.egg82.tcpp.services.registries;

import java.util.UUID;

import ninja.egg82.patterns.ExpiringRegistry;

public class HydraMobRegistry extends ExpiringRegistry<UUID> {
	//vars
	
	//constructor
	public HydraMobRegistry() {
		super(UUID.class, 5L * 60L * 1000L);
	}
	
	//public
	
	//private
	
}
