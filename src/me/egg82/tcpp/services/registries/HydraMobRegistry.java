package me.egg82.tcpp.services.registries;

import java.util.UUID;

import ninja.egg82.patterns.ExpiringRegistry;

public class HydraMobRegistry extends ExpiringRegistry<UUID> {
	//vars
	
	//constructor
	public HydraMobRegistry() {
		super(UUID.class, 300000L);
	}
	
	//public
	
	//private
	
}
