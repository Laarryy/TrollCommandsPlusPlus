package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.utils.MathUtil;

public class EntombCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public EntombCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_ENTOMB, new int[]{1}, new int[]{0})) {
			if (args.length == 1) {
				e(Bukkit.getPlayer(args[0]));
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player) {
		Location loc = player.getLocation();
		Location loc1 = new Location(loc.getWorld(), loc.getX(), Math.floor(MathUtil.random(5.0d, 15.0d)), loc.getZ());
		Location loc2 = new Location(loc.getWorld(), loc.getX(), loc1.getY() + 1.0d, loc.getZ());
		loc1.getBlock().setType(Material.OBSIDIAN);
		loc2.getBlock().setType(Material.OBSIDIAN);
		player.teleport(loc1);
		
		sender.sendMessage(player.getName() + " has been entombed.");
	}
}
