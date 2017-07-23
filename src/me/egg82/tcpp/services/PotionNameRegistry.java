package me.egg82.tcpp.services;

import ninja.egg82.patterns.Registry;

public class PotionNameRegistry extends Registry<String> {
	//vars
	
	//constructor
	public PotionNameRegistry() {
		super(String.class);
		
		setRegister("ABSORPTION", "Absorption");
		setRegister("BLINDNESS", "Blindness");
		setRegister("CONFUSION", "Nausea");
		setRegister("DAMAGE_RESISTANCE", "Resistance");
		setRegister("FAST_DIGGING", "Haste");
		setRegister("FIRE_RESISTANCE", "Fire Resistance");
		setRegister("GLOWING", "Glowing");
		setRegister("HARM", "Instant Damage");
		setRegister("HEAL", "Instant Health");
		setRegister("HEALTH_BOOST", "Health Boost");
		setRegister("HUNGER", "Hunger");
		setRegister("INCREASE_DAMAGE", "Strength");
		setRegister("INVISIBILITY", "Invisibility");
		setRegister("JUMP", "Jump Boost");
		setRegister("LEVITATION", "Levitation");
		setRegister("LUCK", "Luck");
		setRegister("NIGHT_VISION", "Night Vision");
		setRegister("POISON", "Poison");
		setRegister("REGENERATION", "Regeneration");
		setRegister("SATURATION", "Saturation");
		setRegister("SLOW", "Slowness");
		setRegister("SLOW_DIGGING", "Mining Fatigue");
		setRegister("SPEED", "Speed");
		setRegister("UNLUCK", "Bad Luck");
		setRegister("WATER_BREATHING", "Water Breathing");
		setRegister("WEAKNESS", "Weakness");
		setRegister("WITHER", "Wither");
	}
	
	//public
	
	//private
	
}
