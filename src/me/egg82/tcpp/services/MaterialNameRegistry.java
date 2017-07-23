package me.egg82.tcpp.services;

import org.bukkit.Material;

import ninja.egg82.patterns.Registry;

public class MaterialNameRegistry extends Registry<String> {
	//vars
	
	//constructor
	public MaterialNameRegistry() {
		super(String.class);
		
		Material[] types = Material.values();
		
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			setRegister(types[i].name(), types[i]);
		}
	}
	
	//public
	
	//private
	
}
