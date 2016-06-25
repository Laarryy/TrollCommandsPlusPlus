package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;

public class SlapCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public SlapCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_SLAP, new int[]{1,2}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			
			if (args.length == 1) {
				e(player.getName(), player, 3.0d);
			} else if (args.length == 2) {
				try {
					e(player.getName(), player, Double.parseDouble(args[1]));
				} catch (Exception ex) {
					sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
					return;
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player, double force) {
		Location loc = player.getLocation();
		player.getWorld().playEffect(loc, Effect.ANVIL_LAND, 0);
		player.setVelocity(loc.getDirection().multiply(-1.0d * force));
		
		sender.sendMessage(name + " has been slapped.");
	}
}
