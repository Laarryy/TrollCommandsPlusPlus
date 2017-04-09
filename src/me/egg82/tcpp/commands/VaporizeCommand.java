package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;

public class VaporizeCommand extends BasePluginCommand {
	//vars
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public VaporizeCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_VAPORIZE, new int[]{1,2}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			
			if (args.length == 1) {
				e(player.getName(), player, 4.0f);
			} else if (args.length == 2) {
				try {
					e(player.getName(), player, Float.parseFloat(args[1]));
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
	private void e(String name, Player player, float power) {
		Location loc = player.getLocation();
		player.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), Math.abs(power), (power > 0) ? true : false, (power > 0) ? true : false);
		
		sender.sendMessage(name + " has been vaporized.");
	}
}
