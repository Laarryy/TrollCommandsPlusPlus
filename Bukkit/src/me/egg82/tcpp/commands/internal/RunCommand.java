package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;

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

public class RunCommand extends PluginCommand {
	//vars
	private ArrayList<String> commandNames = new ArrayList<String>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public RunCommand() {
		super();
		
		for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
			String name = topic.getName();
			if (name.charAt(0) != '/') {
				continue;
			}
			
			commandNames.add(name.substring(1, name.length()));
		}
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
				return commandNames;
			}
			
			ArrayList<String> retVal = new ArrayList<String>();
			
			for (int i = 0; i < commandNames.size(); i++) {
				if (commandNames.get(i).toLowerCase().startsWith(args[1].toLowerCase())) {
					retVal.add(commandNames.get(i));
				}
			}
			
			return retVal;
		} else if (args.length > 2) {
			org.bukkit.command.PluginCommand pluginCommand = Bukkit.getPluginCommand(args[1]);
			
			if (pluginCommand != null) {
				ArrayList<String> newArgs = new ArrayList<String>(Arrays.asList(args));
				newArgs.remove(0);
				newArgs.remove(0);
				return pluginCommand.tabComplete(sender, label, newArgs.toArray(new String[0]));
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_RUN)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_RUN)));
			return;
		}
		if (args.length < 2) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		String command = "";
		for (int i = 1; i < args.length; i++) {
			command += args[i] + " ";
		}
		command = command.trim();
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player, command);
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
				onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
				return;
			}
			
			e(player, command);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(Player player, String command) {
		CommandUtil.dispatchCommandAtSenderLocation(sender, player, command);
		metricsHelper.commandWasRun(this);
	}
	
	protected void onUndo() {
		
	}
}