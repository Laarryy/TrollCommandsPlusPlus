package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;

public class SwapCommand extends BasePluginCommand {
	//vars
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public SwapCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_SWAP, new int[]{2}, new int[]{0,1})) {
			if (args.length == 2) {
				Player p1 = Bukkit.getPlayer(args[0]);
				Player p2 = Bukkit.getPlayer(args[1]);
				
				e(p1.getName(), p1, p2.getName(), p2);
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String p1Name, Player p1, String p2Name, Player p2) {
		Location l1 = p1.getLocation();
		Location l2 = p2.getLocation();
		p1.teleport(l2);
		p2.teleport(l1);
		
		sender.sendMessage(p1Name + " and " + p2Name + "have been swapped.");
	}
}
