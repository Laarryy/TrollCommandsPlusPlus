package me.egg82.tcpp.reflection.disguise;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;
import de.robingrether.idisguise.disguise.PlayerDisguise;;

public class DisguiseHelper implements IDisguiseHelper {
	//vars
	private DisguiseAPI api = Bukkit.getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
	
	//constructor
	public DisguiseHelper() {
		
	}
	
	//public
	public void disguiseAsPlayer(Player player, Player disguise) {
		api.disguise(player, new PlayerDisguise(disguise.getName(), disguise.getDisplayName()));
	}
	public void disguiseAsEntity(Player player, EntityType disguise) {
		api.disguise(player, new MobDisguise(DisguiseType.valueOf(disguise.toString())));
	}
	public void undisguise(Player player) {
		api.undisguise(player);
	}
	
	public EntityType disguiseType(Player player) {
		OfflinePlayer p = (OfflinePlayer) player;
		return (api.isDisguised(p)) ? EntityType.valueOf(api.getDisguise(p).getType().toString().toUpperCase()) : null;
	}
	public boolean isDisguised(Player player) {
		OfflinePlayer p = (OfflinePlayer) player;
		return api.isDisguised(p);
	}
	
	public boolean isValidLibrary() {
		return true;
	}
	
	//private
	
}
