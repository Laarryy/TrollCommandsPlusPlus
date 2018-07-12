package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class VaporizeCommand extends CommandHandler {
	//vars
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public VaporizeCommand() {
		super();
	}
	
	//public
	public List<String> tabComplete() {
		if (args.length == 1) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if (args[0].isEmpty()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					retVal.add(player.getName());
				}
			} else {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						retVal.add(player.getName());
					}
				}
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_VAPORIZE)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		double power = 4.0d;
		if (args.length == 2) {
			try {
				power = Double.parseDouble(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player, power);
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			if (player.hasPermission(PermissionsType.IMMUNE)) {
				sender.sendMessage(ChatColor.RED + "Player is immune.");
				return;
			}
			
			e(player, power);
		}
	}
	private void e(Player player, double power) {
		Location location = player.getLocation().clone();
		location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), (float) Math.abs(power), (power > 0.0d) ? true : false, (power > 0.0d) ? true : false);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " has been vaporized.");
	}
	
	protected void onUndo() {
		
	}
}
