package me.egg82.tcpp.services;

import ninja.egg82.patterns.Registry;

public class EnchantNameRegistry extends Registry {
	//vars
	
	//constructor
	public EnchantNameRegistry() {
		super();
		
		setRegister("ARROW_DAMAGE", String.class, "Power");
		setRegister("ARROW_FIRE", String.class, "Flame");
		setRegister("ARROW_INFINITE", String.class, "Infinity");
		setRegister("ARROW_KNOCKBACK", String.class, "Punch");
		setRegister("BINDING_CURSE", String.class, "Curse of Binding");
		setRegister("DAMAGE_ALL", String.class, "Sharpness");
		setRegister("DAMAGE_ARTHROPODS", String.class, "Bane of Arthropods");
		setRegister("DAMAGE_UNDEAD", String.class, "Smite");
		setRegister("DEPTH_STRIDER", String.class, "Depth Strider");
		setRegister("DIG_SPEED", String.class, "Efficiency");
		setRegister("DURABILITY", String.class, "Unbreaking");
		setRegister("FIRE_ASPECT", String.class, "Fire Aspect");
		setRegister("FROST_WALKER", String.class, "Frost Walker");
		setRegister("KNOCKBACK", String.class, "Knockback");
		setRegister("LOOT_BONUS_BLOCKS", String.class, "Fortune");
		setRegister("LOOT_BONUS_MOBS", String.class, "Looting");
		setRegister("LUCK", String.class, "Luck of the Sea");
		setRegister("LURE", String.class, "Lure");
		setRegister("MENDING", String.class, "Mending");
		setRegister("OXYGEN", String.class, "Respiration");
		setRegister("PROTECTION_ENVIRONMENTAL", String.class, "Protection");
		setRegister("PROTECTION_EXPLOSIONS", String.class, "Blast Protection");
		setRegister("PROTECTION_FALL", String.class, "Feather Falling");
		setRegister("PROTECTION_FIRE", String.class, "Fire Protection");
		setRegister("PROTECTION_PROJECTILE", String.class, "Projectile Protection");
		setRegister("SILK_TOUCH", String.class, "Silk Touch");
		setRegister("SWEEPING_EDGE", String.class, "Sweeping Edge");
		setRegister("THORNS", String.class, "Thorns");
		setRegister("VANISHING_CURSE", String.class, "Curse of Vanishing");
		setRegister("WATER_WORKER", String.class, "Aqua Affinity");
	}
	
	//public
	
	//private
	
}
