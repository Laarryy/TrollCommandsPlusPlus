package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

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
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_TIME)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 2)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
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
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
				return;
			}
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (player.isPlayerTimeRelative()) {
					if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
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
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			if (player.isPlayerTimeRelative()) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
					onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
					return;
				}
				
				e(player, time);
			} else {
				eUndo(player);
			}
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
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
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void eUndo(Player player) {
		player.resetPlayerTime();
		
		sender.sendMessage(player.getName() + "'s time is no longer permanent.");
	}
}
