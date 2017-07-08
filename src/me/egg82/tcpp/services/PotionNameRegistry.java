package me.egg82.tcpp.services;

import ninja.egg82.patterns.Registry;

public class PotionNameRegistry extends Registry {
	//vars
	
	//constructor
	public PotionNameRegistry() {
		super();
		
		setRegister("ABSORPTION", String.class, "Absorption");
		setRegister("BLINDNESS", String.class, "Blindness");
		setRegister("CONFUSION", String.class, "Nausea");
		setRegister("DAMAGE_RESISTANCE", String.class, "Resistance");
		setRegister("FAST_DIGGING", String.class, "Haste");
		setRegister("FIRE_RESISTANCE", String.class, "Fire Resistance");
		setRegister("GLOWING", String.class, "Glowing");
		setRegister("HARM", String.class, "Instant Damage");
		setRegister("HEAL", String.class, "Instant Health");
		setRegister("HEALTH_BOOST", String.class, "Health Boost");
		setRegister("HUNGER", String.class, "Hunger");
		setRegister("INCREASE_DAMAGE", String.class, "Strength");
		setRegister("INVISIBILITY", String.class, "Invisibility");
		setRegister("JUMP", String.class, "Jump Boost");
		setRegister("LEVITATION", String.class, "Levitation");
		setRegister("LUCK", String.class, "Luck");
		setRegister("NIGHT_VISION", String.class, "Night Vision");
		setRegister("POISON", String.class, "Poison");
		setRegister("REGENERATION", String.class, "Regeneration");
		setRegister("SATURATION", String.class, "Saturation");
		setRegister("SLOW", String.class, "Slowness");
		setRegister("SLOW_DIGGING", String.class, "Mining Fatigue");
		setRegister("SPEED", String.class, "Speed");
		setRegister("UNLUCK", String.class, "Bad Luck");
		setRegister("WATER_BREATHING", String.class, "Water Breathing");
		setRegister("WEAKNESS", String.class, "Weakness");
		setRegister("WITHER", String.class, "Wither");
	}
	
	//public
	
	//private
	
}
