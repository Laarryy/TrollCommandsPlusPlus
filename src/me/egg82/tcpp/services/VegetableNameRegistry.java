package me.egg82.tcpp.services;

import ninja.egg82.patterns.Registry;

public class VegetableNameRegistry extends Registry {
	//vars
	
	//constructor
	public VegetableNameRegistry() {
		super();
		
		setRegister("BEETROOT", String.class, "Beetroot");
		setRegister("BROWN_MUSHROOM", String.class, "Brown Mushroom");
		setRegister("CARROT_ITEM", String.class, "Carrot");
		setRegister("POTATO_ITEM", String.class, "Potato");
		setRegister("RED_MUSHROOM", String.class, "Red Mushroom");
	}
	
	//public
	
	//private
	
}
