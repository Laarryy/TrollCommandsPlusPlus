package me.egg82.tcpp.registries;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.registries.Registry;
import ninja.egg82.patterns.tuples.pair.Pair;

public class MidasTouchRegistry extends Registry<UUID, Pair<Material, IConcurrentSet<Location>>> {
	//vars
	
	//constructor
	@SuppressWarnings("unchecked")
	public MidasTouchRegistry() {
		super(new UUID[0], new Pair[0]);
	}
	
	//public
	
	//private
	
}
