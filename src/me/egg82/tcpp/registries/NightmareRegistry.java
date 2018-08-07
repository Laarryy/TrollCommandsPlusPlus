package me.egg82.tcpp.registries;

import java.util.UUID;

import me.egg82.tcpp.reflection.entity.IFakeLivingEntity;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.registries.Registry;

public class NightmareRegistry extends Registry<UUID, IConcurrentSet<IFakeLivingEntity>> {
	//vars
	
	//constructor
	@SuppressWarnings("unchecked")
	public NightmareRegistry() {
		super(new UUID[0], new IConcurrentSet[0]);
	}
	
	//public
	
	//private
	
}
