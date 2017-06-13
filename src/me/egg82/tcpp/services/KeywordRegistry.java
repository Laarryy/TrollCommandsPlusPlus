package me.egg82.tcpp.services;

import ninja.egg82.patterns.Registry;

public class KeywordRegistry extends Registry {
	//vars
	
	//constructor
	public KeywordRegistry() {
		super();
		
		this.setRegister("alone", String[].class, new String[] {
			"hide",
			"sneak",
			"iron",
			"door",
			"leave",
			"only",
			"solo",
			"abandon",
			"lonely",
			"isolated",
			"solitary"
		});
		this.setRegister("amnesia", String[].class, new String[] {
			"forget",
			"remember",
			"duplicate",
			"copy",
			"remove",
			"diamond",
			"pickaxe",
			"stupor",
			"blackout"
		});
		this.setRegister("annoy", String[].class, new String[] {
			"villager",
			"person",
			"sound",
			"egg",
			"agitate",
			"bother",
			"disturb",
			"exasperate",
			"provoke"
		});
		this.setRegister("anvil", String[].class, new String[] {
			"squash",
			"crush",
			"drop",
			"smith",
			"weapon",
			"armor",
			"sword"
		});
		this.setRegister("banish", String[].class, new String[] {
			"wood",
			"door",
			"exile",
			"leave",
			"alone",
			"dismiss",
			"dispel",
			"eject",
			"eliminate",
			"evict",
			"isolate",
			"ban",
			"deport",
			"transport"
		});
		this.setRegister("bomb", String[].class, new String[] {
			"tnt",
			"firework",
			"fireball",
			"rain",
			"explosion",
			"explosive",
			"missile",
			"projectile",
			"rocket",
			"torpedo",
			"charge",
			"grenade",
			"blast",
			"erupt",
			"detonate"
		});
		this.setRegister("brittle", String[].class, new String[] {
			"fragile",
			"glass",
			"death",
			"kill",
			"break",
			"crisp",
			"frail",
			"crumble",
			"delicate",
			"weak"
		});
		this.setRegister("burn", String[].class, new String[] {
			"blaze",
			"powder",
			"fire",
			"blaze",
			"char",
			"heat",
			"ignite",
			"incinerate",
			"light",
			"melt",
			"scorch",
			"smolder",
			"torch",
			"bake",
			"cook",
			"flame",
			"smoke"
		});
		this.setRegister("cannon", String[].class, new String[] {
			"tnt",
			"dynamite",
			"ac/dc",
			"explosion",
			"mortar",
			"artillary",
			"blast",
			"erupt",
			"detonate"
		});
		this.setRegister("clumsy", String[].class, new String[] {
			"sand",
			"drop",
			"fumble",
			"mistake",
			"inept",
			"blunder",
			"bumbling",
			"crude",
			"unable"
		});
		this.setRegister("comet", String[].class, new String[] {
			"fireball",
			"shoot",
			"explosion",
			"meteor",
			"blast",
			"erupt",
			"detonate"
		});
		this.setRegister("control", String[].class, new String[] {
			"iron",
			"fence",
			"posess",
			"disguise",
			"force",
			"dominate",
			"restrict",
			"rope",
			"string"
		});
		this.setRegister("creep", String[].class, new String[] {
			"egg",
			"creeper",
			"tnt",
			"dynamite",
			"explosion",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
		this.setRegister("delaykill", String[].class, new String[] {
			"skull",
			"head",
			"player",
			"death",
			"assassinate",
			"execute",
			"murder",
			"slaughter",
			"slay",
			"finish"
		});
		this.setRegister("display", String[].class, new String[] {
			"thin",
			"glass",
			"showcase",
			"trap",
			"example",
			"exhibit",
			"present",
			"expose"
		});
		this.setRegister("electrify", String[].class, new String[] {
			"energy",
			"lightning",
			"electricity",
			"daylight",
			"detector",
			"bolt"
		});
		this.setRegister("entomb", String[].class, new String[] {
			"dirt",
			"bury",
			"enshrine"
		});
		this.setRegister("explodebreak", String[].class, new String[] {
			"cobblestone",
			"tnt",
			"explosion",
			"dynamite",
			"blast",
			"erupt",
			"detonate"
		});
		this.setRegister("explodebuild", String[].class, new String[] {
			"redstone",
			"torch",
			"tnt",
			"explosion",
			"dynamite",
			"blast",
			"erupt",
			"detonate"
		});
		this.setRegister("flip", String[].class, new String[] {
			"wood",
			"stairs",
			"180",
			"rotate",
			"twist"
		});
		this.setRegister("freeze", String[].class, new String[] {
			"ice",
			"pause",
			"stop",
			"paralyze",
			"motionless",
			"block",
			"halt",
			"stun"
		});
		this.setRegister("garble", String[].class, new String[] {
			"book",
			"misinterpret",
			"misquote",
			"confuse",
			"twist",
			"mislead",
			"obscure",
			"warp",
			"falsify"
		});
		this.setRegister("golem", String[].class, new String[] {
			"red",
			"rose",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
		this.setRegister("haunt", String[].class, new String[] {
			"skull",
			"zombie",
			"annoy",
			"hound",
			"frighten",
			"terrify",
			"torment",
			"terrorize",
			"worry",
			"agitate"
		});
		this.setRegister("hound", String[].class, new String[] {
			"bone",
			"dog",
			"puppy",
			"pooch",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
		this.setRegister("hurt", String[].class, new String[] {
			"diamond",
			"sword",
			"pain",
			"kill",
			"death",
			"suffering",
			"wound",
			"harm"
		});
		this.setRegister("infinity", String[].class, new String[] { });
		this.setRegister("lag", String[].class, new String[] { });
		this.setRegister("lavabreak", String[].class, new String[] { });
		this.setRegister("lift", String[].class, new String[] { });
		this.setRegister("lure", String[].class, new String[] { });
		this.setRegister("nausea", String[].class, new String[] { });
		this.setRegister("night", String[].class, new String[] { });
		this.setRegister("nopickup", String[].class, new String[] { });
		this.setRegister("popup", String[].class, new String[] { });
		this.setRegister("portal", String[].class, new String[] { });
		this.setRegister("potato", String[].class, new String[] { });
		this.setRegister("rewind", String[].class, new String[] { });
		this.setRegister("scare", String[].class, new String[] { });
		this.setRegister("slap", String[].class, new String[] { });
		this.setRegister("slender", String[].class, new String[] { });
		this.setRegister("slowmine", String[].class, new String[] { });
		this.setRegister("slowpoke", String[].class, new String[] { });
		this.setRegister("slowundo", String[].class, new String[] { });
		this.setRegister("spam", String[].class, new String[] { });
		this.setRegister("sparta", String[].class, new String[] { });
		this.setRegister("spin", String[].class, new String[] { });
		this.setRegister("squid", String[].class, new String[] { });
		this.setRegister("stampede", String[].class, new String[] { });
		this.setRegister("starve", String[].class, new String[] { });
		this.setRegister("swap", String[].class, new String[] { });
		this.setRegister("troll", String[].class, new String[] { });
		this.setRegister("useless", String[].class, new String[] { });
		this.setRegister("vaporize", String[].class, new String[] { });
		this.setRegister("vegetable", String[].class, new String[] { });
		this.setRegister("void", String[].class, new String[] { });
		this.setRegister("weakling", String[].class, new String[] { });
		this.setRegister("whoami", String[].class, new String[] { });
		this.setRegister("zombify", String[].class, new String[] { });
	}
	
	//public
	
	//private
	
}
