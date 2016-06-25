package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class NightCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public NightCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_NIGHT, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		if (!player.isPlayerTimeRelative()) {
			player.resetPlayerTime();
			sender.sendMessage(name + "'s time is no longer perma-night.");
		} else {
			player.setPlayerTime(18000, false);
			sender.sendMessage(name + "'s time is now perma-night.");
		}
	}
}
