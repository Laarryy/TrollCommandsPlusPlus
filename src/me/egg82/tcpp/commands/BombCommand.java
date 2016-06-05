package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.egg82.patterns.ServiceLocator;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.commands.base.RegistryCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;

public class BombCommand extends RegistryCommand {
	//vars
	private IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
	
	//constructor
	public BombCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (tryPlaceRegistry(false, PermissionsType.COMMAND_BOMB, bombRegistry)) {
			String name = Bukkit.getPlayer(args[0]).getName();
			if (!bombRegistry.contains(name.toLowerCase())) {
				sender.sendMessage(name + " is no longer being bombed.");
			} else {
				sender.sendMessage(name + " is now being bombed.");
			}
		}
	}
}