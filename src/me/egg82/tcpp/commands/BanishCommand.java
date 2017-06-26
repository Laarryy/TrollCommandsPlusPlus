package me.egg82.tcpp.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class BanishCommand extends PluginCommand {
	//vars
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public BanishCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_BANISH)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		double banishMax = 20000.0d;
		if (args.length == 2) {
			try {
				banishMax = Double.parseDouble(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				sender.getServer().dispatchCommand(sender, "help " + command.getName());
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
		}
		
		if (e(player.getUniqueId().toString(), player, banishMax)) {
			dispatch(CommandEvent.COMPLETE, null);
		} else {
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_SPACE);
		}
	}
	private boolean e(String uuid, Player player, double maxRadius) {
		double minRadius = maxRadius - (maxRadius / 3.0d);
		Location currentLocation = player.getLocation();
		double currentX = currentLocation.getX();
		double newX = currentX;
		double currentZ = currentLocation.getZ();
		double newZ = currentZ;
		
		while (newX >= currentX - minRadius && newX <= currentX + minRadius) {
			newX = MathUtil.random(currentX - maxRadius, currentX + maxRadius);
		}
		while (newZ >= currentZ - minRadius && newZ <= currentZ + minRadius) {
			newZ = MathUtil.random(currentZ - maxRadius, currentZ + maxRadius);
		}
		
		Location newLocation = null;
		Material headBlock = null;
		int retryCount = 0;
		
		do {
			newLocation = BlockUtil.getTopAirBlock(new Location(currentLocation.getWorld(), newX, MathUtil.random(5.0d, currentLocation.getWorld().getMaxHeight()), newZ));
			headBlock = newLocation.clone().add(0.0d, 1.0d, 0.0d).getBlock().getType();
			retryCount++;
		} while (headBlock != Material.AIR && retryCount < 100);
		
		if (headBlock != Material.AIR) {
			sender.sendMessage(player.getName() + " could not be banished because there was not enough space around them!");
			return false;
		}
		
		player.teleport(newLocation);
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage(player.getName() + " has been banished.");
		return true;
	}
	
	protected void onUndo() {
		
	}
}