package me.egg82.tcpp.reflection.disguise;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public interface IDisguiseHelper {
	void disguiseAsPlayer(Player player, Player disguise);
	void disguiseAsPlayer(Player player, Player disguise, boolean canSeeOwnDisguise);
	void disguiseAsEntity(Player player, EntityType disguise);
	void disguiseAsEntity(Player player, EntityType disguise, boolean canSeeOwnDisguise);
	void undisguise(Player player);
	
	EntityType disguiseType(Player player);
	boolean isDisguised(Player player);
	
	boolean isValidLibrary();
}
