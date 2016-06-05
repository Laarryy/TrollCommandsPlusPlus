package me.egg82.tcpp.commands;

import org.bukkit.command.CommandSender;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class TrollCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public TrollCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_TROLL, new int[]{0,1}, new int[]{0})) {
			if (args.length == 0) {
				sender.getServer().dispatchCommand(sender, "help trollcommandsplusplus");
			} else if (args.length == 1) {
				sender.getServer().dispatchCommand(sender, "help trollcommandsplusplus" + args[0]);
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
}