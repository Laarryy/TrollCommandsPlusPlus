package me.egg82.tcpp.reflection.disguise;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class NullDisguiseHelper implements IDisguiseHelper {
	//vars
	
	//constructor
	public NullDisguiseHelper() {
		
	}
	
	//public
	public void disguiseAsPlayer(Player player, Player disguise) {
		
	}
	public void disguiseAsPlayer(Player player, Player disguise, boolean canSeeOwnDisguise) {
		
	}
	public void disguiseAsEntity(Player player, EntityType disguise) {
		
	}
	public void disguiseAsEntity(Player player, EntityType disguise, boolean canSeeOwnDisguise) {
		
	}
	public void undisguise(Player player) {
		
	}
	
	public EntityType disguiseType(Player player) {
		return null;
	}
	public boolean isDisguised(Player player) {
		return false;
	}
	
	public boolean isValidLibrary() {
		return false;
	}
	
	//private
	
}
