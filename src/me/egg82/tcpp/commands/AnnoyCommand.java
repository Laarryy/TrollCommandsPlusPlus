package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class AnnoyCommand extends BasePluginCommand {
	//vars
	IRegistry annoyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ANNOY_REGISTRY);
	
	//constructor
	public AnnoyCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		annoyRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_ANNOY, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (annoyRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer being annoyed by villager sounds.");
			annoyRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " is now being annoyed by villager sounds.");
			annoyRegistry.setRegister(uuid, player);
		}
	}
}
