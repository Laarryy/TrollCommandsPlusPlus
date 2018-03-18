package me.egg82.tcpp.services.registries;

import java.util.UUID;

import ninja.egg82.patterns.ExpiringRegistry;

public class EmpowerRegistry extends ExpiringRegistry<UUID> {
	//vars
	
	//constructor
	public EmpowerRegistry() {
		super(UUID.class, 300000L);
	}
	
	//public
	
	//private
	
}
