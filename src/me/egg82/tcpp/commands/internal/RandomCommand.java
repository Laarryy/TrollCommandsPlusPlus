package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.analytics.exceptions.IExceptionHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.plugin.utils.PluginReflectUtil;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class RandomCommand extends CommandHandler {
	//vars
	private ArrayList<String> commandNames = new ArrayList<String>();
	private HashMap<String, Class<CommandHandler>> commands = new HashMap<String, Class<CommandHandler>>();
	private HashMap<String, CommandHandler> initializedCommands = new HashMap<String, CommandHandler>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public RandomCommand() {
		super();
		
		// Collect all internal commands and names, then map the commands
		List<Class<CommandHandler>> classes = ReflectUtil.getClasses(CommandHandler.class, "me.egg82.tcpp.commands.internal", true, false, false);
		Map<String, String> names = PluginReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.commands.internal", null, "Command");
		for (Class<CommandHandler> c : classes) {
			String name = c.getName().toLowerCase();
			String command = names.remove(name);
			
			if (command == null) {
				continue;
			}
			
			commandNames.add(command);
			commands.put(command, c);
		}
		
		// Sort command names because it looks pretty on the client side
		Collections.sort(commandNames);
		
		commandNames.remove("search");
		commands.remove("search");
		commandNames.remove("random");
		commands.remove("random");
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
		if (!sender.hasPermission(PermissionsType.COMMAND_RANDOM)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (player.hasPermission(PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player.getName());
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
			
			e(player.getName());
		}
	}
	private void e(String player) {
		CommandHandler command = getCommand(commandNames.get(MathUtil.fairRoundedRandom(0, commandNames.size() - 1)));
		Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll " + command.getCommandName() + " " + player);
		
		metricsHelper.commandWasRun(this);
	}
	protected void onUndo() {
		
	}
	
	private CommandHandler getCommand(String command) {
		String key = getRealCommandName(command);
		
		if (key == null) {
			return null;
		}
		
		CommandHandler run = initializedCommands.get(key);
		Class<CommandHandler> c = commands.get(key);
		
		// run might be null, but c will never be as long as the command actually exists
		if (c == null) {
			return null;
		}
		
		// Lazy initialize. No need to create a command until it's actually going to be used
		if (run == null) {
			// Create a new command and store it
			try {
				run = c.newInstance();
			} catch (Exception ex) {
				IExceptionHandler handler = ServiceLocator.getService(IExceptionHandler.class);
				if (handler != null) {
					handler.sendException(ex);
				}
				return null;
			}
			
			initializedCommands.put(key, run);
		}
		
		ArrayList<String> newArgs = new ArrayList<String>(Arrays.asList(args));
		newArgs.remove(0);
		
		run.setSender(sender);
		run.setCommandName(key);
		run.setArgs(newArgs.toArray(new String[0]));
		
		return run;
	}
	private String getRealCommandName(String commandName) {
		commandName = commandName.toLowerCase();
		if (commandNames.contains(commandName)) {
			// Found an exact match
			return commandName;
		}
		
		return null;
	}
}