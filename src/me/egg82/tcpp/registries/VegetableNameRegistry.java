package me.egg82.tcpp.registries;

import ninja.egg82.patterns.registries.VariableRegistry;

public class VegetableNameRegistry extends VariableRegistry<String> {
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
