package me.egg82.tcpp.services;

import org.bukkit.Material;

import ninja.egg82.patterns.Registry;

public class GuiRegistry extends Registry {
	//vars
	
	//constructor
	public GuiRegistry() {
		super();
		
		setRegister("alone", Material.class, Material.IRON_DOOR);
		//setRegister("amnesia", Material.class, );
		setRegister("annoy", Material.class, Material.MONSTER_EGG);
		setRegister("annoy_data", Short.class, (short) 120);
		setRegister("anvil", Material.class, Material.ANVIL);
		setRegister("banish", Material.class, Material.WOOD_DOOR);
		setRegister("bomb", Material.class, Material.FIREWORK);
		setRegister("brittle", Material.class, Material.GLASS);
		setRegister("burn", Material.class, Material.BLAZE_POWDER);
		setRegister("cannon", Material.class, Material.TNT);
		setRegister("clumsy", Material.class, Material.SAND);
		setRegister("comet", Material.class, Material.FIREBALL);
		setRegister("control", Material.class, Material.IRON_FENCE);
		setRegister("creep", Material.class, Material.MONSTER_EGG);
		setRegister("creep_data", Short.class, (short) 50);
		setRegister("delaykill", Material.class, Material.SKULL_ITEM);
		setRegister("delaykill_data", Short.class, (short) 3);
		setRegister("display", Material.class, Material.THIN_GLASS);
		setRegister("electrify", Material.class, Material.DAYLIGHT_DETECTOR);
		setRegister("entomb", Material.class, Material.DIRT);
		//setRegister("explodebreak", Material.class, );
		//setRegister("explodebuild", Material.class, );
		//setRegister("flip", Material.class, );
		setRegister("freeze", Material.class, Material.ICE);
		//setRegister("garble", Material.class, );
		setRegister("golem", Material.class, Material.RED_ROSE);
		setRegister("haunt", Material.class, Material.SKULL_ITEM);
		setRegister("haunt_data", Short.class, (short) 2);
		setRegister("hound", Material.class, Material.BONE);
		setRegister("hurt", Material.class, Material.DIAMOND_SWORD);
		//setRegister("infinity", Material.class, );
		//setRegister("lag", Material.class, );
		setRegister("lavabreak", Material.class, Material.LAVA_BUCKET);
		//setRegister("lift", Material.class, );
		setRegister("lure", Material.class, Material.BEACON);
		setRegister("nausea", Material.class, Material.SPIDER_EYE);
		setRegister("night", Material.class, Material.NETHER_STAR);
		//setRegister("nopickup", Material.class, );
		//setRegister("popup", Material.class, );
		setRegister("portal", Material.class, Material.ENDER_PORTAL_FRAME);
		setRegister("potato", Material.class, Material.POTATO_ITEM);
		//setRegister("rewind", Material.class, );
		setRegister("scare", Material.class, Material.SKULL_ITEM);
		setRegister("scare_data", Short.class, (short) 4);
		//setRegister("slap", Material.class, );
		setRegister("slender", Material.class, Material.MONSTER_EGG);
		setRegister("slender_data", Short.class, (short) 58);
		setRegister("slowmine", Material.class, Material.MINECART);
		setRegister("slowpoke", Material.class, Material.WEB);
		//setRegister("slowundo", Material.class, );
		//setRegister("spam", Material.class, );
		setRegister("sparta", Material.class, Material.ARROW);
		//setRegister("spin", Material.class, );
		setRegister("squid", Material.class, Material.INK_SACK);
		setRegister("stampede", Material.class, Material.LEATHER);
		setRegister("starve", Material.class, Material.POISONOUS_POTATO);
		//setRegister("swap", Material.class, );
		setRegister("troll", Material.class, Material.CHEST);
		//setRegister("useless", Material.class, );
		setRegister("vaporize", Material.class, Material.SULPHUR);
		setRegister("vegetable", Material.class, Material.CARROT_ITEM);
		setRegister("void", Material.class, Material.WOOL);
		setRegister("void_data", Short.class, (short) 15);
		setRegister("weakling", Material.class, Material.WOOD_SWORD);
		//setRegister("whoami", Material.class, );
		setRegister("zombify", Material.class, Material.ROTTEN_FLESH);
	}
	
	//public
	
	//private
	
}
