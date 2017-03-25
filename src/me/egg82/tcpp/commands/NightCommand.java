package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class NightCommand extends BasePluginCommand {
	//vars
	IRegistry nightRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.NIGHT_REGISTRY);
	
	//constructor
	public NightCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		nightRegistry.computeIfPresent(uuid, (k,v) -> {
			player.setPlayerTime(18000, false);
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_NIGHT, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (nightRegistry.contains(uuid)) {
			player.resetPlayerTime();
			sender.sendMessage(player.getName() + "'s time is no longer perma-night.");
			nightRegistry.setRegister(uuid, null);
		} else {
			player.setPlayerTime(18000, false);
			sender.sendMessage(player.getName() + "'s time is now perma-night.");
			nightRegistry.setRegister(uuid, player);
		}
	}
}
