package me.egg82.tcpp.commands;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;

public class CannonCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public CannonCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(true, PermissionsType.COMMAND_CANNON, new int[]{0,1}, null)) {
			if (args.length == 0) {
				e((Player) sender, 2.0d);
			} else if (args.length == 1) {
				try {
					e((Player) sender, Double.parseDouble(args[0]));
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
	
	private void e(Player player, double speed) {
		Vector direction = player.getLocation().getDirection().multiply(speed);
		TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
		tnt.setVelocity(direction);
	}
}