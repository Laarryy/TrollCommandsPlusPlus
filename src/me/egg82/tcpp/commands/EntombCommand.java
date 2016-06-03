package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.plugin.commands.PluginCommand;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;

public class EntombCommand extends PluginCommand {
	//vars
	
	//constructor
	public EntombCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (sender instanceof Player && !permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_ENTOMB)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 1) {
			entomb(Bukkit.getPlayer(args[0]));
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	private void entomb(Player player) {
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
		
		Location loc = player.getLocation();
		Location loc1 = new Location(loc.getWorld(), loc.getX(), 5.0d, loc.getZ());
		Location loc2 = new Location(loc.getWorld(), loc.getX(), 6.0d, loc.getZ());
		loc1.getBlock().setType(Material.OBSIDIAN);
		loc2.getBlock().setType(Material.OBSIDIAN);
		player.teleport(loc1);
		
		sender.sendMessage(player.getName() + " has been entombed.");
		
		dispatch(CommandEvent.COMPLETE, null);
	}
}
