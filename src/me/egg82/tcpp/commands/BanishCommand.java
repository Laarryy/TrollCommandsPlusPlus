package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.plugin.commands.PluginCommand;
import com.egg82.utils.MathUtil;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;

public class BanishCommand extends PluginCommand {
	//vars
	
	//constructor
	public BanishCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (sender instanceof Player && !permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_BANISH)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 1) {
			try {
				banish(Bukkit.getPlayer(args[0]), MathUtil.random(-20000.0d, 20000.0d));
			} catch (Exception ex) {
				sender.sendMessage(MessageType.INCORRECT_USAGE);
				dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
			}
		} else if (args.length == 2) {
			try {
				banish(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]));
			} catch (Exception ex) {
				sender.sendMessage(MessageType.INCORRECT_USAGE);
				dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
			}
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	
	private void banish(Player player, double radius) {
		if (player == null) {
			sender.sendMessage(MessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (permissionsManager.playerHasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		Location banish = getTopAirBlock(new Location(player.getWorld(), -radius + Math.random() * (2.0d * radius + 1.0d), 5.0d + Math.random() * 66.0d, -radius + Math.random() * (2.0d * radius + 1.0d)));
		player.teleport(banish);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private Location getTopAirBlock(Location l) {
		do {
			while (l.getBlock().getType() != Material.AIR) {
				l = l.subtract(0, 1, 0);
			}
			while (l.subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
				l = l.subtract(0, 1, 0);
			}
		} while (l.getBlock().getType() != Material.AIR || l.subtract(0, 1, 0).getBlock().getType() != Material.AIR);
		
		return l;
	}
}