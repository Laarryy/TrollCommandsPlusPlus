package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class SlapCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public SlapCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_SLAP, new int[]{1,2}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			
			if (args.length == 1) {
				e(player, 3.0d);
			} else if (args.length == 2) {
				try {
					e(player, Double.parseDouble(args[1]));
				} catch (Exception ex) {
					sender.sendMessage(MessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
					return;
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player, double force) {
		Location loc = player.getLocation();
		player.getWorld().playEffect(loc, Effect.ANVIL_LAND, 0);
		player.setVelocity(loc.getDirection().multiply(-1.0d * force));
		
		sender.sendMessage(player.getName() + " has been slapped.");
	}
}
