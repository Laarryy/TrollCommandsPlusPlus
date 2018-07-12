package me.egg82.tcpp.commands.internal;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class CannonCommand extends CommandHandler {
	//vars
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public CannonCommand() {
		super();
	}
	
	//public
	public List<String> tabComplete() {
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_CANNON)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (sender.isConsole()) {
			sender.sendMessage(ChatColor.RED + "Console cannot run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1, 2)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		Player player = (Player) sender.getHandle();
		double speed = 2.0d;
		double power = 1.0d;
		
		if (args.length == 1) {
			try {
				speed = Double.parseDouble(args[0]);
			} catch (Exception ex) {
				sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
				return;
			}
		}
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
		
		e(player.getEyeLocation(), speed, power);
	}
	private void e(Location location, double speed, double power) {
		TNTPrimed tnt = location.getWorld().spawn(location, TNTPrimed.class);
		tnt.setYield((float) power);
		tnt.setIsIncendiary((power > 0.0d) ? true : false);
		Vector direction = location.getDirection().multiply(speed);
		tnt.setVelocity(direction);
		
		metricsHelper.commandWasRun(this);
	}
	
	protected void onUndo() {
		
	}
}