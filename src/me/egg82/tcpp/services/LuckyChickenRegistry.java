package me.egg82.tcpp.services;

import java.util.UUID;

import ninja.egg82.patterns.ExpiringRegistry;

public class LuckyChickenRegistry extends ExpiringRegistry<UUID> {
	//vars
	
	//constructor
	public LuckyChickenRegistry() {
		super(UUID.class, 300000L);
	}
	
	//public
	
	//private
	
}
