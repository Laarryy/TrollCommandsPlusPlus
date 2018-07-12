package me.egg82.tcpp.registries;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import ninja.egg82.patterns.registries.VariableExpiringRegistry;

public class LuckyVillagerRegistry extends VariableExpiringRegistry<UUID> {
	//vars
	
	//constructor
	public LuckyVillagerRegistry() {
		super(UUID.class, 30L * 1000L, TimeUnit.MILLISECONDS);
	}
	
	//public
	
	//private
	
}
