package me.egg82.tcpp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.WhoAmIRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.CommandUtil;

public class WhoAmIHelper {
	//vars
	private IRegistry<UUID> whoAmIRegistry = ServiceLocator.getService(WhoAmIRegistry.class);
	
	//constructor
	public WhoAmIHelper() {
		
	}
	
	//public
	public void start(UUID uuid, Player player) {
		ArrayList<String> names = new ArrayList<String>();
		names.add(player.getDisplayName());
		names.add(player.getPlayerListName());
		
		whoAmIRegistry.setRegister(uuid, names);
	}
	@SuppressWarnings("unchecked")
	public void stop(UUID uuid, Player player) {
		List<String> names = whoAmIRegistry.getRegister(uuid, List.class);
		player.setDisplayName(names.get(0));
		player.setPlayerListName(names.get(1));
		
		whoAmIRegistry.removeRegister(uuid);
	}
	public void stop(UUID uuid, OfflinePlayer player) {
		
	}
	
	public void stopAll() {
		for (UUID key : whoAmIRegistry.getKeys()) {
			stop(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	
	//private
	
}
