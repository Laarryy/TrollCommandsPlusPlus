package me.egg82.tcpp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class AmnesiaCommand extends BasePluginCommand {
	//vars
	private IRegistry amnesiaRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_REGISTRY);
	private IRegistry amnesiaInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_INTERN_REGISTRY);
	
	//constructor
	public AmnesiaCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		amnesiaRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_AMNESIA, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (amnesiaRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer an amnesiac.");
			amnesiaRegistry.setRegister(uuid, null);
			amnesiaInternRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " is now an amnesiac.");
			amnesiaRegistry.setRegister(uuid, player);
			amnesiaInternRegistry.setRegister(uuid, new ArrayList<ImmutableMap<String, Object>>());
		}
	}
}
