package me.egg82.tcpp.reflection.disguise;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class LibsDisguisesHelper implements IDisguiseHelper {
	//vars
	
	//constructor
	public LibsDisguisesHelper() {
		
	}
	
	//public
	public void disguiseAsPlayer(Player player, Player disguise) {
		DisguiseAPI.disguiseToAll(player, new PlayerDisguise(disguise).setViewSelfDisguise(false));
	}
	public void disguiseAsPlayer(Player player, Player disguise, boolean canSeeOwnDisguise) {
		DisguiseAPI.disguiseToAll(player, new PlayerDisguise(disguise).setViewSelfDisguise(canSeeOwnDisguise));
	}
	public void disguiseAsEntity(Player player, EntityType disguise) {
		DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.getType(disguise)).setViewSelfDisguise(false));
	}
	public void disguiseAsEntity(Player player, EntityType disguise, boolean canSeeOwnDisguise) {
		DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.getType(disguise)).setViewSelfDisguise(canSeeOwnDisguise));
	}
	public void undisguise(Player player) {
		DisguiseAPI.undisguiseToAll(player);
	}
	
	public EntityType disguiseType(Player player) {
		return (DisguiseAPI.isDisguised(player)) ? EntityType.valueOf(DisguiseAPI.getDisguise(player).getType().toString()) : null;
	}
	public boolean isDisguised(Player player) {
		return DisguiseAPI.isDisguised(player);
	}
	
	public boolean isValidLibrary() {
		return true;
	}
	
	//private
	
}
