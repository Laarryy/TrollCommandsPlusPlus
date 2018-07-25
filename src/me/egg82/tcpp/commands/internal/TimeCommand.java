package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class TimeCommand extends CommandHandler {
	//vars
	private ArrayList<String> timeNames = new ArrayList<String>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public TimeCommand() {
		super();
		
		timeNames.add("sunrise");
		timeNames.add("day");
		timeNames.add("morning");
		timeNames.add("noon");
		timeNames.add("afternoon");
		timeNames.add("sunset");
		timeNames.add("night");
		timeNames.add("midnight");
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
		} else if (args.length == 2) {
			if (args[1].isEmpty()) {
				return timeNames;
			}
			
			ArrayList<String> retVal = new ArrayList<String>();
			
			for (String name : timeNames) {
				if (name.startsWith(args[1].toLowerCase())) {
					retVal.add(name);
				}
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_TIME)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 2)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		long time = -1L;
		if (args[1].equalsIgnoreCase("sunrise")) {
			time = 23000L;
		} else if (args[1].equalsIgnoreCase("day")) {
			time = 0L;
		} else if (args[1].equalsIgnoreCase("morning")) {
			time = 1000L;
		} else if (args[1].equalsIgnoreCase("noon")) {
			time = 6000L;
		} else if (args[1].equalsIgnoreCase("afternoon")) {
			time = 9000L;
		} else if (args[1].equalsIgnoreCase("sunset")) {
			time = 12000L;
		} else if (args[1].equalsIgnoreCase("night")) {
			time = 14000L;
		} else if (args[1].equalsIgnoreCase("midnight")) {
			time = 18000L;
		} else {
			try {
				time = Long.parseLong(args[1]);
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
				if (player.isPlayerTimeRelative()) {
					if (player.hasPermission(PermissionsType.IMMUNE)) {
						continue;
					}
					
					e(player, time);
				} else {
					eUndo(player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Player could not be found.");
				return;
			}
			
			if (player.isPlayerTimeRelative()) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					sender.sendMessage(ChatColor.RED + "Player is immune.");
					return;
				}
				
				e(player, time);
			} else {
				eUndo(player);
			}
		}
	}
	private void e(Player player, long time) {
		player.setPlayerTime(time, false);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + "'s time is now permanent.");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		if (player != null) {
			if (!player.isPlayerTimeRelative()) {
				eUndo(player);
			}
		}
	}
	private void eUndo(Player player) {
		player.resetPlayerTime();
		
		sender.sendMessage(player.getName() + "'s time is no longer permanent.");
	}
}
