package me.egg82.tcpp.registries;

import java.util.UUID;

import org.bukkit.Material;

import ninja.egg82.patterns.registries.Registry;
import ninja.egg82.patterns.tuples.pair.Boolean2Pair;

public class MidasTouchRegistry extends Registry<UUID, Boolean2Pair<Material>> {
	//vars
	
	//constructor
	@SuppressWarnings("unchecked")
	public MidasTouchRegistry() {
		super(new UUID[0], new Boolean2Pair[0]);
	}
	
	//public
	
	//private
	
}
