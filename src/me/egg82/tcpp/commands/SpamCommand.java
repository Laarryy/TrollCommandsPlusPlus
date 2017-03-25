package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class SpamCommand extends BasePluginCommand {
	//vars
	IRegistry spamRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SPAM_REGISTRY);
	
	//constructor
	public SpamCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		spamRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_SPAM, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (spamRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + "'s chat is no longer being spammed with junk.");
			spamRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + "'s chat is now being spammed with junk.");
			spamRegistry.setRegister(uuid, player);
		}
	}
}
