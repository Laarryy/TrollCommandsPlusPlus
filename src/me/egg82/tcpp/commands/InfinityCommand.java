package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class InfinityCommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.INFINITY_REGISTRY);
	
	//constructor
	public InfinityCommand() {
		super();
	}
	
	//public
	public void onDeath(String name, Player player) {
		reg.computeIfPresent(name, (k,v) -> {
			return null;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_INFINITY, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		String lowerName = name.toLowerCase();
		
		if (reg.contains(lowerName)) {
			sender.sendMessage(name + " is no longer falling forever.");
			reg.setRegister(lowerName, null);
		} else {
			Location l = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().clone();
			l.add(0.0d, 30.0d, 0.0d);
			player.teleport(l);
			
			sender.sendMessage(name + " is now falling forever.");
			reg.setRegister(lowerName, player);
		}
	}
}
