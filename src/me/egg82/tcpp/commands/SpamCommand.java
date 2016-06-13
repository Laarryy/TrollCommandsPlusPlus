package me.egg82.tcpp.commands;

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

public class SpamCommand extends BasePluginCommand {
	//vars
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.SPAM_REGISTRY);
	
	//constructor
	public SpamCommand(CommandSender sender, Command command, String label, String[] args) {
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
		if (isValid(false, PermissionsType.COMMAND_SPAM, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		if (reg.contains(name.toLowerCase())) {
			sender.sendMessage(name + "'s chat is no longer being spammed with junk.");
			reg.setRegister(name.toLowerCase(), null);
		} else {
			sender.sendMessage(name + "'s chat is now being spammed with junk.");
			reg.setRegister(name.toLowerCase(), player);
		}
	}
}
