package me.egg82.tcpp.util.nulls;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.egg82.tcpp.util.interfaces.IDisguiseHelper;

public class NullDisguiseHelper implements IDisguiseHelper {
	//vars
	
	//constructor
	public NullDisguiseHelper() {
		
	}
	
	//public
	public void disguiseAsPlayer(Player player, Player disguise) {
		
	}
	public void disguiseAsEntity(Player player, EntityType disguise) {
		
	}
	public void undisguise(Player player) {
		
	}
	public EntityType disguiseType(Player player) {
		return null;
	}
	
	//private
	
}
