package me.egg82.tcpp.registries;

import ninja.egg82.patterns.registries.VariableRegistry;

public class EnchantNameRegistry extends VariableRegistry<String> {
    //vars

    //constructor
    public EnchantNameRegistry() {
        super(String.class);

        setRegister("ARROW_DAMAGE", "Power");
        setRegister("ARROW_FIRE", "Flame");
        setRegister("ARROW_INFINITE", "Infinity");
        setRegister("ARROW_KNOCKBACK", "Punch");
        setRegister("BINDING_CURSE", "Curse of Binding");
        setRegister("DAMAGE_ALL", "Sharpness");
        setRegister("DAMAGE_ARTHROPODS", "Bane of Arthropods");
        setRegister("DAMAGE_UNDEAD", "Smite");
        setRegister("DEPTH_STRIDER", "Depth Strider");
        setRegister("DIG_SPEED", "Efficiency");
        setRegister("DURABILITY", "Unbreaking");
        setRegister("FIRE_ASPECT", "Fire Aspect");
        setRegister("FROST_WALKER", "Frost Walker");
        setRegister("KNOCKBACK", "Knockback");
        setRegister("LOOT_BONUS_BLOCKS", "Fortune");
        setRegister("LOOT_BONUS_MOBS", "Looting");
        setRegister("LUCK", "Luck of the Sea");
        setRegister("LURE", "Lure");
        setRegister("MENDING", "Mending");
        setRegister("OXYGEN", "Respiration");
        setRegister("PROTECTION_ENVIRONMENTAL", "Protection");
        setRegister("PROTECTION_EXPLOSIONS", "Blast Protection");
        setRegister("PROTECTION_FALL", "Feather Falling");
        setRegister("PROTECTION_FIRE", "Fire Protection");
        setRegister("PROTECTION_PROJECTILE", "Projectile Protection");
        setRegister("SILK_TOUCH", "Silk Touch");
        setRegister("SWEEPING_EDGE", "Sweeping Edge");
        setRegister("THORNS", "Thorns");
        setRegister("VANISHING_CURSE", "Curse of Vanishing");
        setRegister("WATER_WORKER", "Aqua Affinity");
    }

    //public

    //private

}
