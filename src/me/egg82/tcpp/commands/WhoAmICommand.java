package me.egg82.tcpp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class WhoAmICommand extends BasePluginCommand {
	//vars
	private IRegistry whoAmIRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.WHO_AM_I_REGISTRY);
	private IRegistry whoAmIInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.WHO_AM_I_INTERN_REGISTRY);
	
	//constructor
	public WhoAmICommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		whoAmIRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_WHO_AM_I, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String uuid, Player player) {
		if (whoAmIRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer having an identity crisis.");
			
			whoAmIInternRegistry.computeIfPresent(uuid, (k,v) -> {
				HashMap<String, Object> map = (HashMap<String, Object>) v;
				String displayName = (String) map.get("displayName");
				String listName = (String) map.get("listName");
				
				player.setDisplayName(displayName);
				try {
					player.setPlayerListName(listName);
				} catch (Exception ex) {
					
				}
				
				return null;
			});
			
			whoAmIRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " is now having an identity crisis.");
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("displayName", player.getDisplayName());
			map.put("listName", player.getPlayerListName());
			
			whoAmIRegistry.setRegister(uuid, player);
			whoAmIInternRegistry.setRegister(uuid, map);
		}
	}
}
