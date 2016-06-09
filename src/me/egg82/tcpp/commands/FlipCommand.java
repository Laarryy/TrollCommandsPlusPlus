package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class FlipCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public FlipCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_FLIP, new int[]{1}, new int[]{0})) {
			e(Bukkit.getPlayer(args[0]));
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player) {
		Location loc = player.getLocation();
		loc.setYaw(loc.getYaw() - 180.0f);
		player.teleport(loc);
		
		sender.sendMessage(player.getName() + " has been flipped.");
	}
}
