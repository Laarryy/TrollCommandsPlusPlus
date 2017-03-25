package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class NopickupCommand extends BasePluginCommand {
	//vars
	private IRegistry noPickupRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.NO_PICKUP_REGISTRY);
	
	//constructor
	public NopickupCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		noPickupRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_NOPICKUP, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (noPickupRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " can now pick items up again.");
			noPickupRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " can no longer pick items up.");
			noPickupRegistry.setRegister(uuid, player);
		}
	}
}
