package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.utils.MathUtil;

public class BanishCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public BanishCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_BANISH, new int[]{1,2}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			
			if (args.length == 1) {
				e(player.getName(), player, 20000.0d);
			} else if (args.length == 2) {
				try {
					e(player.getName(), player, Double.parseDouble(args[1]));
				} catch (Exception ex) {
					sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
					return;
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	
	private void e(String name, Player player, double radius) {
		Location banish = BlockUtil.getTopAirBlock(new Location(player.getWorld(), -radius + Math.random() * (2.0d * radius + 1.0d), MathUtil.random(5.0d, 66.0d), -radius + Math.random() * (2.0d * radius + 1.0d)));
		player.teleport(banish);
		
		sender.sendMessage(name + " has been banished.");
	}
}