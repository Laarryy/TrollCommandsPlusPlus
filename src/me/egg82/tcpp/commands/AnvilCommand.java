package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class AnvilCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public AnvilCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_ANVIL, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		Location loc = player.getLocation().clone();
		for (int i = 0; i < 4; i++) {
			loc.add(0.0d, 1.0d, 0.0d);
			loc.getBlock().setType(Material.AIR);
		}
		loc.add(0.0d, 1.0d, 0.0d);
		loc.getBlock().setType(Material.ANVIL);
		
		sender.sendMessage("The " + ChatColor.STRIKETHROUGH + ChatColor.ITALIC + "base" + ChatColor.RESET + " anvil has been dropped on " + name + ".");
	}
}
