package me.egg82.tcpp.services.registries;

import ninja.egg82.patterns.Registry;

public class VegetableNameRegistry extends Registry<String> {
	//vars
	
	//constructor
	public VegetableNameRegistry() {
		super(String.class);
		
		setRegister("BEETROOT", "Beetroot");
		setRegister("BROWN_MUSHROOM", "Brown Mushroom");
		setRegister("CARROT_ITEM", "Carrot");
		setRegister("POTATO_ITEM", "Potato");
		setRegister("RED_MUSHROOM", "Red Mushroom");
	}
	
	//public
	
	//private
	
}
