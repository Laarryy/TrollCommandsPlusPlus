package me.egg82.tcpp.commands.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.commands.PluginCommand;

public class BasePluginCommand extends PluginCommand {
	//vars
	
	//constructor
	public BasePluginCommand() {
		super();
	}
	
	//public
	
	//private
	protected boolean isValid(boolean needsPlayer, String permissions, int[] argsLengths, int[] playerArgs) {
		if (!super.isValid(needsPlayer, permissions, argsLengths, playerArgs)) {
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
		if (permissionsManager.playerHasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return false;
		}
		
		return true;
	}
}
