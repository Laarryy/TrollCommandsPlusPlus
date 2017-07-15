package me.egg82.tcpp.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.WhoAmINameRegistry;
import me.egg82.tcpp.services.WhoAmIRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;

public class WhoAmIHelper {
	//vars
	private IRegistry whoAmIRegistry = ServiceLocator.getService(WhoAmIRegistry.class);
	private IRegistry whoAmINameRegistry = ServiceLocator.getService(WhoAmINameRegistry.class);
	
	//constructor
	public WhoAmIHelper() {
		
	}
	
	//public
	public void start(String uuid, Player player) {
		ArrayList<String> names = new ArrayList<String>();
		names.add(player.getDisplayName());
		names.add(player.getPlayerListName());
		names.add(player.getCustomName());
		
		whoAmIRegistry.setRegister(uuid, Player.class, player);
		whoAmINameRegistry.setRegister(uuid, List.class, names);
	}
	@SuppressWarnings("unchecked")
	public void stop(String uuid, Player player) {
		whoAmIRegistry.setRegister(uuid, Player.class, null);
		
		List<String> names = whoAmINameRegistry.getRegister(uuid, List.class);
		player.setDisplayName(names.get(0));
		player.setPlayerListName(names.get(1));
		player.setCustomName(names.get(2));
		
		whoAmINameRegistry.setRegister(uuid, List.class, null);
	}
	
	public void stopAll() {
		String[] names = whoAmIRegistry.getRegistryNames();
		for (int i = 0; i < names.length; i++) {
			stop(names[i], whoAmIRegistry.getRegister(names[i], Player.class));
		}
	}
	
	//private
	
}
