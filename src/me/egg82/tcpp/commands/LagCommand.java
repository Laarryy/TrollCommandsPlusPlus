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

public class LagCommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_INTERN_REGISTRY);
	
	//constructor
	public LagCommand() {
		super();
	}
	
	//public
	public void onLogin(String name, Player player) {
		if (reg.contains(name)) {
			reg.setRegister(name, player);
		}
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_LAG, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		String lowerName = name.toLowerCase();
		
		if (reg.contains(lowerName)) {
			sender.sendMessage(name + " is no longer lagging.");
			reg.setRegister(lowerName, null);
			reg2.setRegister(lowerName, null);
		} else {
			sender.sendMessage(name + " is now lagging.");
			reg.setRegister(lowerName, player);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("loc", player.getLocation());
			map.put("lastLoc", 0l);
			reg2.setRegister(lowerName, map);
		}
	}
}
