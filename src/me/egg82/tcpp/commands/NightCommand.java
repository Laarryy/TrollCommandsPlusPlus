package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class NightCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public NightCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_NIGHT, new int[]{1}, new int[]{0})) {
			e(Bukkit.getPlayer(args[0]));
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player) {
		if (!player.isPlayerTimeRelative()) {
			player.resetPlayerTime();
			sender.sendMessage(player.getName() + "'s time is no longer perma-night.");
		} else {
			player.setPlayerTime(18000, false);
			sender.sendMessage(player.getName() + "'s time is now perma-night.");
		}
	}
}
