package me.egg82.tcpp.commands.base;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.commands.PluginCommand;

public class BasePluginCommand extends PluginCommand {
	//vars
	
	//constructor
	public BasePluginCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected boolean isValid(boolean needsPlayer, String permissions, int[] argsLengths, int[] playerArgs) {
		if (needsPlayer && !(sender instanceof Player)) {
			sender.sendMessage(MessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, CommandErrorType.CONSOLE_NOT_ALLOWED);
			return false;
		}
		if (sender instanceof Player && !permissionsManager.playerHasPermission((Player) sender, permissions)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return false;
		}
		
		if (!ArrayUtils.contains(argsLengths, args.length)) {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
			return false;
		}
		
		if (playerArgs != null) {
			for (int i = 0; i < playerArgs.length; i++) {
				if (i < args.length) {
					if (!tryPlayer(Bukkit.getPlayer(args[i]))) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	private boolean tryPlayer(Player player) {
		if (player == null) {
			sender.sendMessage(MessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_NOT_FOUND);
			return false;
		}
		if (permissionsManager.playerHasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return false;
		}
		
		return true;
	}
}
