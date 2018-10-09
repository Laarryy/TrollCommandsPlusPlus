package me.egg82.tcpp.registries;

import org.bukkit.Material;

import ninja.egg82.bukkit.reflection.material.IMaterialHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.VariableRegistry;

public class GuiRegistry extends VariableRegistry<String> {
    //vars

    //constructor
    public GuiRegistry() {
        super(String.class);

        IMaterialHelper materialHelper = ServiceLocator.getService(IMaterialHelper.class);

        setRegister("alone", Material.IRON_DOOR);
        setRegister("amnesia", Material.DIAMOND_PICKAXE);
        setRegister("annoy", materialHelper.getByName("MONSTER_EGG"));
        setRegister("annoy_data", (short) 120);
        setRegister("anvil", Material.ANVIL);
        setRegister("attach", materialHelper.getByName("EXP_BOTTLE"));
        setRegister("banish", materialHelper.getByName("WOOD_DOOR"));
        setRegister("bludger", Material.MAGMA_CREAM);
        setRegister("bomb", materialHelper.getByName("FIREWORK"));
        setRegister("brittle", Material.GLASS);
        setRegister("burn", Material.BLAZE_POWDER);
        setRegister("cannon", Material.TNT);
        setRegister("chat", Material.LAPIS_BLOCK);
        setRegister("clumsy", Material.SAND);
        setRegister("comet", materialHelper.getByName("FIREBALL"));
        setRegister("control", materialHelper.getByName("IRON_FENCE"));
        setRegister("convert", materialHelper.getByName("POTATO_ITEM"));
        setRegister("creep", materialHelper.getByName("MONSTER_EGG"));
        setRegister("creep_data", (short) 50);
        setRegister("deathtag", Material.NAME_TAG);
        setRegister("display", materialHelper.getByName("THIN_GLASS"));
        setRegister("duck", Material.EGG);
        setRegister("effect", materialHelper.getByName("BOOK_AND_QUILL"));
        setRegister("electrify", Material.DAYLIGHT_DETECTOR);
        setRegister("empower", Material.DRAGON_EGG);
        setRegister("enchant", Material.ENCHANTED_BOOK);
        setRegister("entomb", Material.DIRT);
        setRegister("explodebreak", Material.COBBLESTONE);
        setRegister("explodebuild", materialHelper.getByName("REDSTONE_TORCH_ON"));
        setRegister("fakecrash", materialHelper.getByName("COMMAND"));
        setRegister("fakeop", Material.CAKE);
        setRegister("fill", materialHelper.getByName("LEAVES"));
        setRegister("flip", materialHelper.getByName("WOD_STAIRS"));
        setRegister("foolsgold", Material.GOLD_ORE);
        setRegister("freeze", Material.ICE);
        setRegister("garble", Material.BOOK);
        setRegister("grantwishes", Material.DROPPER);
        setRegister("haunt", materialHelper.getByName("SKULL_ITEM"));
        setRegister("haunt_data", (short) 2);
        setRegister("hottub", Material.TORCH);
        setRegister("hurt", Material.DIAMOND_SWORD);
        setRegister("hydra", materialHelper.getByName("SAPLING"));
        setRegister("hydra_data", (short) 1);
        setRegister("infinity", Material.WATER_BUCKET);
        setRegister("inspect", Material.HOPPER);
        setRegister("invert", materialHelper.getByName("REDSTONE_COMPARATOR"));
        setRegister("kill", materialHelper.getByName("SKULL_ITEM"));
        setRegister("kill_data", (short) 3);
        setRegister("lag", Material.EMERALD_BLOCK);
        setRegister("lavabreak", Material.LAVA_BUCKET);
        setRegister("lavabuild", Material.BLAZE_ROD);
        setRegister("levitate", Material.FEATHER);
        setRegister("lift", Material.SLIME_BLOCK);
        setRegister("lock", Material.LEVER);
        setRegister("lsd", materialHelper.getByName("WOOL"));
        setRegister("lsd_data", (short) 2);
        setRegister("luckyblock", materialHelper.getByName("GOLD_PICKAXE"));
        setRegister("lure", Material.BEACON);
        setRegister("midastouch", Material.GOLD_BLOCK);
        setRegister("moist", materialHelper.getByName("STAINED_GLASS"));
        setRegister("moist_data", (short) 3);
        setRegister("necro", materialHelper.getByName("SKULL_ITEM"));
        setRegister("necro_data", (short) 0);
        setRegister("nightmare", Material.OBSIDIAN);
        setRegister("nopickup", Material.DEAD_BUSH);
        setRegister("popup", Material.CHEST);
        setRegister("portal", materialHelper.getByName("ENDER_PORTAL_FRAME"));
        setRegister("public", Material.DISPENSER);
        setRegister("radiate", Material.DIRT);
        setRegister("radiate_data", (short) 2);
        setRegister("random", materialHelper.getByName("COMMAND_MINECART"));
        setRegister("randombreak", Material.IRON_PICKAXE);
        setRegister("randombuild", Material.MAP);
        setRegister("randomdrop", Material.COOKIE);
        setRegister("randommenu", materialHelper.getByName("BREWING_STAND_ITEM"));
        setRegister("randompotion", Material.POTION);
        setRegister("randomspeed", Material.SOUL_SAND);
        setRegister("rewind", materialHelper.getByName("ENCHANTMENT_TABLE"));
        setRegister("run", Material.LAPIS_ORE);
        setRegister("slap", materialHelper.getByName("PISTON_BASE"));
        setRegister("slender", materialHelper.getByName("MONSTER_EGG"));
        setRegister("slender_data", (short) 58);
        setRegister("slowundo", Material.GRASS);
        setRegister("snowballfight", materialHelper.getByName("SNOW_BALL"));
        setRegister("spam", Material.BOOKSHELF);
        setRegister("sparta", Material.ARROW);
        setRegister("spawnbreak", materialHelper.getByName("MOB_SPAWNER"));
        setRegister("spawnbreak_data", (short) 50);
        setRegister("spin", materialHelper.getByName("BOAT"));
        setRegister("spoil", materialHelper.getByName("RAW_FISH"));
        setRegister("spoil_data", (short) 3);
        setRegister("squid", materialHelper.getByName("INK_SACK"));
        setRegister("stampede", Material.LEATHER);
        setRegister("starve", Material.POISONOUS_POTATO);
        setRegister("stop", Material.BARRIER);
        setRegister("surround", materialHelper.getByName("SKULL_ITEM"));
        setRegister("surround_data", (short) 4);
        setRegister("swap", materialHelper.getByName("DIODE"));
        setRegister("time", Material.NETHER_STAR);
        setRegister("trickle", Material.GOLDEN_APPLE);
        setRegister("trip", Material.BRICK_STAIRS);
        setRegister("troll", Material.ENDER_CHEST);
        setRegister("useless", Material.BEDROCK);
        setRegister("vaporize", materialHelper.getByName("SULPHUR"));
        setRegister("vegetable", materialHelper.getByName("CARROT_ITEM"));
        setRegister("void", materialHelper.getByName("WOOL"));
        setRegister("void_data", (short) 15);
        setRegister("vomit", materialHelper.getByName("RAW_FISH"));
        setRegister("whoami", Material.RED_MUSHROOM);

        for (String key : getKeys()) {
            if (getRegister(key) == null) {
                removeRegister(key);
            }
        }
    }

    //public

    //private

}
