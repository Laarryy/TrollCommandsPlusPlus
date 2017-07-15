package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class TimeCommand extends PluginCommand {
	//vars
	private ArrayList<String> timeNames = new ArrayList<String>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public TimeCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
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
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
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
			} else {
				ArrayList<String> retVal = new ArrayList<String>();
				
				for (String name : timeNames) {
					if (name.startsWith(args[1].toLowerCase())) {
						retVal.add(name);
					}
				}
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_TIME)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 2)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
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
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				String uuid = player.getUniqueId().toString();
				
				if (player.isPlayerTimeRelative()) {
					e(uuid, player, time);
				} else {
					eUndo(uuid, player);
				}
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
				return;
			}
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(MessageType.PLAYER_IMMUNE);
				dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
				return;
			}
			
			String uuid = player.getUniqueId().toString();
			
			if (player.isPlayerTimeRelative()) {
				e(uuid, player, time);
			} else {
				eUndo(uuid, player);
			}
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, long time) {
		player.setPlayerTime(time, false);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + "'s time is now permanent.");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		String uuid = player.getUniqueId().toString();
		
		if (!player.isPlayerTimeRelative()) {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void eUndo(String uuid, Player player) {
		player.resetPlayerTime();
		
		sender.sendMessage(player.getName() + "'s time is no longer permanent.");
	}
}
