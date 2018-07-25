package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;

public class RunCommand extends CommandHandler {
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
				return pluginCommand.tabComplete((CommandSender) sender.getHandle(), "", newArgs.toArray(new String[0]));
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_RUN)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		String command = "";
		for (int i = 1; i < args.length; i++) {
			command += args[i] + " ";
		}
		command = command.trim();
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player, command);
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
			
			e(player, command);
		}
	}
	private void e(Player player, String command) {
		CommandUtil.dispatchCommandAtSenderLocation((CommandSender) sender.getHandle(), player, command);
		metricsHelper.commandWasRun(this);
	}
	
	protected void onUndo() {
		
	}
}