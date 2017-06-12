package me.egg82.tcpp.services;

import org.bukkit.Material;

import ninja.egg82.patterns.Registry;

public class GuiRegistry extends Registry {
	//vars
	
	//constructor
	public GuiRegistry() {
		super();
		
		setRegister("alone", Material.class, Material.IRON_DOOR);
		setRegister("annoy", Material.class, Material.MONSTER_EGG);
		setRegister("annoy_data", Short.class, 120);
	}
	
	//public
	
	//private
	
}
