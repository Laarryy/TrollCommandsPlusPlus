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
	public void onLogin(String uuid, Player player) {
		if (reg.contains(uuid)) {
			reg.setRegister(uuid, player);
		}
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_LAG, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (reg.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer lagging.");
			reg.setRegister(uuid, null);
			reg2.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " is now lagging.");
			reg.setRegister(uuid, player);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("loc", player.getLocation());
			map.put("lastLoc", 0l);
			reg2.setRegister(uuid, map);
		}
	}
}
