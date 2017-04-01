package me.egg82.tcpp.util;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public interface IDisguiseHelper {
	void disguiseAsPlayer(Player player, Player disguise);
	void disguiseAsEntity(Player player, EntityType disguise);
	void undisguise(Player player);
	EntityType disguiseType(Player player);
	boolean isValidLibrary();
}
