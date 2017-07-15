package me.egg82.tcpp.services;

import org.bukkit.Material;

import ninja.egg82.patterns.Registry;

public class MaterialNameRegistry extends Registry {
	//vars
	
	//constructor
	public MaterialNameRegistry() {
		super();
		
		Material[] types = Material.values();
		
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			setRegister(types[i].name(), Material.class, types[i]);
		}
	}
	
	//public
	
	//private
	
}
