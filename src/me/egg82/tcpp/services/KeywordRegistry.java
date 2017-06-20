package me.egg82.tcpp.services;

import ninja.egg82.patterns.Registry;

public class KeywordRegistry extends Registry {
	//vars
	
	//constructor
	public KeywordRegistry() {
		super();
		
		setRegister("alone", String[].class, new String[] {
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
		setRegister("amnesia", String[].class, new String[] {
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
		setRegister("annoy", String[].class, new String[] {
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
		setRegister("anvil", String[].class, new String[] {
			"squash",
			"crush",
			"drop",
			"smith",
			"weapon",
			"armor",
			"sword"
		});
		setRegister("banish", String[].class, new String[] {
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
		setRegister("bomb", String[].class, new String[] {
			"tnt",
			"firework",
			"fireball",
			"rain",
			"explode",
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
		setRegister("brittle", String[].class, new String[] {
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
		setRegister("burn", String[].class, new String[] {
			"blaze",
			"powder",
			"fire",
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
		setRegister("cannon", String[].class, new String[] {
			"tnt",
			"dynamite",
			"ac/dc",
			"explode",
			"explosion",
			"mortar",
			"artillary",
			"blast",
			"erupt",
			"detonate"
		});
		setRegister("clumsy", String[].class, new String[] {
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
		setRegister("comet", String[].class, new String[] {
			"fireball",
			"shoot",
			"explode",
			"explosion",
			"meteor",
			"blast",
			"erupt",
			"detonate"
		});
		setRegister("control", String[].class, new String[] {
			"iron",
			"fence",
			"posess",
			"disguise",
			"camno",
			"veil",
			"cloak",
			"guise",
			"costume",
			"illusion",
			"mask",
			"force",
			"dominate",
			"restrict",
			"rope",
			"string"
		});
		setRegister("creep", String[].class, new String[] {
			"egg",
			"creeper",
			"tnt",
			"dynamite",
			"explode",
			"explosion",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
		setRegister("day", String[].class, new String[] {
			"glowstone",
			"sun",
			"light",
			"time",
			"white"
		});
		setRegister("delaykill", String[].class, new String[] {
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
		setRegister("display", String[].class, new String[] {
			"thin",
			"glass",
			"showcase",
			"trap",
			"example",
			"exhibit",
			"present",
			"expose"
		});
		setRegister("electrify", String[].class, new String[] {
			"energy",
			"lightning",
			"electricity",
			"daylight",
			"detector",
			"bolt"
		});
		setRegister("empower", String[].class, new String[] {
			"dragon",
			"egg",
			"ender",
			"allow",
			"entitle",
			"grant",
			"power",
			"health",
			"regen",
			"regeneration",
			"attack",
			"defense",
			"speed",
			"vest",
			"permit",
			"invest",
			"privilege",
			"qualify",
			"sanction"
		});
		setRegister("empty", String[].class, new String[] {
			"ender",
			"pearl",
			"inventory",
			"chest",
			"void",
			"nether"
		});
		setRegister("entomb", String[].class, new String[] {
			"dirt",
			"bury",
			"enshrine"
		});
		setRegister("explodebreak", String[].class, new String[] {
			"cobblestone",
			"tnt",
			"explosion",
			"dynamite",
			"blast",
			"erupt",
			"detonate"
		});
		setRegister("explodebuild", String[].class, new String[] {
			"redstone",
			"torch",
			"tnt",
			"explosion",
			"dynamite",
			"blast",
			"erupt",
			"detonate"
		});
		setRegister("fakeop", String[].class, new String[] {
			"cake",
			"privilege",
			"bogus",
			"phony",
			"artificial"
		});
		setRegister("flip", String[].class, new String[] {
			"wood",
			"stairs",
			"180",
			"rotate",
			"twist"
		});
		setRegister("freeze", String[].class, new String[] {
			"ice",
			"pause",
			"stop",
			"paralyze",
			"motionless",
			"block",
			"halt",
			"stun"
		});
		setRegister("garble", String[].class, new String[] {
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
		setRegister("golem", String[].class, new String[] {
			"red",
			"rose",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
		setRegister("haunt", String[].class, new String[] {
			"skull",
			"zombie",
			"annoy",
			"sound",
			"hound",
			"frighten",
			"terrify",
			"torment",
			"terrorize",
			"worry",
			"agitate"
		});
		setRegister("hottub", String[].class, new String[] {
			"torch",
			"lava",
			"burn",
			"fire",
			"hot",
			"drop",
			"fall",
			"hole"
		});
		setRegister("hound", String[].class, new String[] {
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
		setRegister("hurt", String[].class, new String[] {
			"diamond",
			"sword",
			"pain",
			"kill",
			"death",
			"suffering",
			"wound",
			"harm"
		});
		setRegister("infinity", String[].class, new String[] {
			"water",
			"bucket",
			"fall",
			"forever",
			"endless",
			"eternal",
			"permanent",
			"drop"
		});
		setRegister("inspect", String[].class, new String[] {
			"hopper",
			"inventory",
			"audit",
			"check",
			"investigate",
			"observe",
			"probe",
			"scan",
			"review",
			"survey",
			"watch",
			"case",
			"study",
			"view"
		});
		setRegister("lag", String[].class, new String[] {
			"gold",
			"block",
			"fail",
			"slow",
			"drag",
			"stay"
		});
		setRegister("lavabreak", String[].class, new String[] {
			"lava",
			"bucket",
			"slag",
			"magma",
			"ashes",
			"obsidian",
			"fire",
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
		setRegister("levitate", String[].class, new String[] {
			"feather",
			"chicken",
			"drift",
			"float",
			"fly",
			"hang",
			"climb",
			"disappear",
			"hover",
			"raise",
			"rise",
			"soar",
			"gravity",
			"up",
			"vanish"
		});
		setRegister("lift", String[].class, new String[] {
			"slime",
			"block",
			"ascend",
			"boost",
			"climb",
			"disappear",
			"hoist",
			"raise",
			"rise",
			"soar",
			"arise",
			"up",
			"vanish"
		});
		setRegister("lock", String[].class, new String[] {
			"lever",
			"latch",
			"bar",
			"inventory",
			"link",
			"bond"
		});
		setRegister("lure", String[].class, new String[] {
			"beacon",
			"bait",
			"charm",
			"draw",
			"hook",
			"pull"
		});
		setRegister("nausea", String[].class, new String[] {
			"spider",
			"eye",
			"vomit",
			"revulsion",
			"spinning"
		});
		setRegister("night", String[].class, new String[] {
			"nether",
			"star",
			"moon",
			"bed",
			"black",
			"dark",
			"twilight"
		});
		setRegister("nopickup", String[].class, new String[] {
			"dead",
			"bush",
			"gather",
			"elevate",
			"grasp"
		});
		setRegister("popup", String[].class, new String[] {
			"chest",
			"appear",
			"inventory",
			"open",
			"close",
			"random"
		});
		setRegister("portal", String[].class, new String[] {
			"end",
			"frame",
			"doorway",
			"entry",
			"gate",
			"opening",
			"drop",
			"fall"
		});
		setRegister("potato", String[].class, new String[] {
			"tuber",
			"plant",
			"spud",
			"vegetable",
			"tuber"
		});
		setRegister("public", String[].class, new String[] {
			"dispenser",
			"inventory",
			"civic",
			"communal",
			"mututal",
			"national",
			"popular",
			"social",
			"universal"
		});
		setRegister("rewind", String[].class, new String[] {
			"enchantment",
			"table",
			"back",
			"reverse",
			"undo",
			"invert"
		});
		setRegister("scare", String[].class, new String[] {
			"skull",
			"creeper",
			"posess",
			"disguise",
			"camno",
			"veil",
			"cloak",
			"guise",
			"costume",
			"illusion",
			"mask",
			"alarm",
			"panick",
			"shock",
			"alert",
			"fright",
			"terror"
		});
		setRegister("slap", String[].class, new String[] {
			"piston",
			"punch",
			"whack",
			"bang",
			"poke",
			"slam",
			"strike",
			"sock"
		});
		setRegister("slender", String[].class, new String[] {
			"egg",
			"enderman",
			"dark",
			"blind",
			"slow",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
		setRegister("slowmine", String[].class, new String[] {
			"minecart",
			"delay",
			"restrict",
			"abate",
			"curb",
			"lag",
			"reduce",
			"brake"
		});
		setRegister("slowpoke", String[].class, new String[] {
			"web",
			"spider",
			"delay",
			"restrict",
			"abate",
			"curb",
			"lag",
			"reduce",
			"brake"
		});
		setRegister("slowundo", String[].class, new String[] {
			"grass",
			"block",
			"invalidate",
			"nullify",
			"abolish",
			"annul",
			"destroy",
			"negate",
			"reverse",
			"ruin"
		});
		setRegister("snowballfight", String[].class, new String[] {
			"snow",
			"ball",
			"snowball",
			"bow",
			"arrow",
			"shoot"
		});
		setRegister("spam", String[].class, new String[] {
			"bookshelf",
			"unsolicited",
			"fill",
			"stuff"
		});
		setRegister("sparta", String[].class, new String[] {
			"arrow",
			"poke",
			"jab",
			"blow",
			"sharp",
			"stab"
		});
		setRegister("spin", String[].class, new String[] {
			"boat",
			"twist",
			"spiral",
			"revolution",
			"turn",
			"whirl",
			"rotate"
		});
		setRegister("squid", String[].class, new String[] {
			"ink",
			"sac",
			"octopus",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
		setRegister("stampede", String[].class, new String[] {
			"leather",
			"cow",
			"panic",
			"charge",
			"chase",
			"crash",
			"smash"
		});
		setRegister("starve", String[].class, new String[] {
			"poison",
			"potato",
			"famish",
			"fast",
			"diet"
		});
		setRegister("swap", String[].class, new String[] {
			"diode",
			"redstone",
			"repeater",
			"substitute",
			"switch",
			"trade",
			"change"
		});
		setRegister("trickle", String[].class, new String[] {
			"golden",
			"apple",
			"exp",
			"xp",
			"experience",
			"level"
		});
		setRegister("troll", String[].class, new String[] {
			"ender",
			"chest",
			"user",
			"interface",
			"ui",
			"gui",
			"graphical"
		});
		setRegister("useless", String[].class, new String[] {
			"bedrock",
			"fruitless",
			"futile",
			"hopeless",
			"idle",
			"impractical",
			"ineffective",
			"meaningless",
			"pointless",
			"stupid",
			"unproductive",
			"worthless",
			"waste",
			"weak"
		});
		setRegister("vaporize", String[].class, new String[] {
			"sulphur",
			"gunpowder",
			"creeper",
			"zap",
			"destroy",
			"kill",
			"terminate",
			"tnt",
			"explode",
			"explosion",
			"dynamite",
			"blast",
			"erupt",
			"detonate"
		});
		setRegister("vegetable", String[].class, new String[] {
			"carrot",
			"potato",
			"herb",
			"produce",
			"salad",
			"edible",
			"green"
		});
		setRegister("void", String[].class, new String[] {
			"black",
			"wool",
			"bare",
			"clear",
			"empty",
			"drained",
			"lacking"
		});
		setRegister("vomit", String[].class, new String[] {
			"raw",
			"fish",
			"eject",
			"gag",
			"heave",
			"regurgitate",
			"emit",
			"drop",
			"expel",
			"item",
			"hurt",
			"puke"
		});
		setRegister("weakling", String[].class, new String[] {
			"wood",
			"sword",
			"anemic",
			"feeble",
			"fragile",
			"frail",
			"sickly",
			"sluggish",
			"exhaust",
			"faint",
			"spent",
			"tender",
			"waste"
		});
		setRegister("whoami", String[].class, new String[] {
			"mushroom",
			"careless",
			"distracted",
			"inattentive",
			"sloppy",
			"absent",
			"forgetful",
			"amnesia"
		});
		setRegister("zombify", String[].class, new String[] {
			"rotten",
			"flesh",
			"zombie",
			"spawn",
			"create",
			"generate",
			"hatch",
			"make"
		});
	}
	
	//public
	
	//private
	
}
