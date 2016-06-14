package me.egg82.tcpp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class WhoAmICommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.WHO_AM_I_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.WHO_AM_I_INTERN_REGISTRY);
	
	//constructor
	public WhoAmICommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	public void onLogin(String name, Player player) {
		if (reg.contains(name)) {
			reg.setRegister(name, player);
		}
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_WHO_AM_I, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String name, Player player) {
		HashMap<String, Object> map = null;
		
		if (reg.contains(name.toLowerCase())) {
			sender.sendMessage(name + " is no longer having an identity crisis.");
			
			map = (HashMap<String, Object>) reg2.getRegister(name.toLowerCase());
			String displayName = (String) map.get("displayName");
			String listName = (String) map.get("listName");
			
			player.setDisplayName(displayName);
			try {
				player.setPlayerListName(listName);
			} catch (Exception ex) {
				
			}
			
			reg.setRegister(name.toLowerCase(), null);
			reg2.setRegister(name.toLowerCase(), null);
		} else {
			sender.sendMessage(name + " is now having an identity crisis.");
			
			map = new HashMap<String, Object>();
			map.put("displayName", player.getDisplayName());
			map.put("listName", player.getPlayerListName());
			
			reg.setRegister(name.toLowerCase(), player);
			reg2.setRegister(name.toLowerCase(), map);
		}
	}
}
