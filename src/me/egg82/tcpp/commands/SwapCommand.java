package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class SwapCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public SwapCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_SWAP, new int[]{2}, new int[]{0,1})) {
			if (args.length == 2) {
				try {
					e(Bukkit.getPlayer(args[0]), Bukkit.getPlayer(args[1]));
				} catch (Exception ex) {
					sender.sendMessage(MessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player p1, Player p2) {
		Location l1 = p1.getLocation();
		Location l2 = p2.getLocation();
		p1.teleport(l2);
		p2.teleport(l1);
		
		sender.sendMessage(p1.getName() + " and " + p2.getName() + "have been swapped.");
	}
}
