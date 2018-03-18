package me.egg82.tcpp.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidCommandException;
import me.egg82.tcpp.services.databases.CommandSearchDatabase;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.BasePlugin;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.utils.BukkitReflectUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.ReflectUtil;

public class TrollCommand extends PluginCommand {
	//vars
	private LanguageDatabase commandDatabase = ServiceLocator.getService(CommandSearchDatabase.class);
	private ArrayList<String> commandNames = new ArrayList<String>();
	private HashMap<String, String> commandDescriptions = new HashMap<String, String>();
	
	private HashMap<String, Class<PluginCommand>> commands = new HashMap<String, Class<PluginCommand>>();
	private HashMap<String, PluginCommand> initializedCommands = new HashMap<String, PluginCommand>();
	
	private BasePlugin plugin = ServiceLocator.getService(BasePlugin.class);
	
	int totalPages = -1;
	
	//constructor
	public TrollCommand() {
		super();
		
		String[] list = ((String) ((PluginDescriptionFile) plugin.getDescription()).getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
		for (String entry : list) {
			if (entry.contains("-= Available Commands =-")) {
				continue;
			}
			
			String usage = entry.substring(0, entry.indexOf(':')).trim();
			String c = usage.split(" ")[1];
			String description = entry.substring(entry.indexOf(':') + 1).trim();
			
			commandDescriptions.put(c.toLowerCase(), description);
		}
		
		// Collect all internal commands and names, then map the commands
		List<Class<PluginCommand>> classes = ReflectUtil.getClasses(PluginCommand.class, "me.egg82.tcpp.commands.internal", true, false, false);
		Map<String, String> names = BukkitReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.commands.internal", null, "Command");
		for (Class<PluginCommand> c : classes) {
			String name = c.getName().toLowerCase();
			String command = names.remove(name);
			
			if (command == null) {
				plugin.printWarning("Internal \"" + name + "\" not found in command map!");
				continue;
			}
			
			commandNames.add(command);
			commands.put(command, c);
		}
		if (!names.isEmpty()) {
			plugin.printWarning("Internal command map contains unused values! " + Arrays.toString(names.keySet().toArray()));
		}
		
		// Sort command names because it looks pretty on the client side
		Collections.sort(commandNames);
		// Get the total pages for /help
		totalPages = (int) Math.ceil(((double) commandNames.size()) / 9.0d);
	}
	
	//public
	public List<String> tabComplete() {
		if (args.length == 0 || args[0].isEmpty()) {
			// We want command names
			return commandNames;
		} else if (args.length == 1) {
			// We want command names that start with the existing input
			ArrayList<String> retVal = new ArrayList<String>();
			for (int i = 0; i < commandNames.size(); i++) {
				if (commandNames.get(i).startsWith(args[0].toLowerCase())) {
					retVal.add(commandNames.get(i));
				}
			}
			return retVal;
		}
		
		// Pass it along to the command to handle
		String commandName = args[0].toLowerCase();
		
		PluginCommand c = getCommand(commandName, true);
		if (c == null) {
			return null;
		}
		return c.tabComplete();
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_TROLL)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_TROLL)));
			return;
		}
		
		// Is /help requested?
		int page = 1;
		if (args.length == 1) {
			page = -1;
			
			// Is the argument a player, or are we trying to parse a number?
			if (CommandUtil.getPlayerByName(args[0]) == null) {
				try {
					page = Integer.parseInt(args[0]);
				} catch (Exception ex) {
					
				}
			}
			
			if (page != -1 && page < 1) {
				page = 1;
			}
		}
		
		// Only run if the command is /troll or /troll <number>
		if (args.length == 0 || (args.length == 1 && page != -1)) {
			// Act like /help
			page -= 1;
			sender.sendMessage(ChatColor.YELLOW + "---- " + ChatColor.GOLD + "Help: troll" + ChatColor.YELLOW + " -- " + ChatColor.GOLD + "Page " + ChatColor.RED + (page + 1) + ChatColor.GOLD + "/" + ChatColor.RED + totalPages + ChatColor.YELLOW + " ----");
			sender.sendMessage(ChatColor.GOLD + "Commands from TrollCommands++:");
			if (page * 9 < commandNames.size()) {
				for (int i = page * 9; i < Math.min((page * 9) + 9, commandNames.size()); i++) {
					sender.sendMessage(ChatColor.GOLD + "/troll " + commandNames.get(i) + ChatColor.WHITE + ": " + commandDescriptions.get(commandNames.get(i)));
				}
				if (page + 1 < totalPages) {
					sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/troll " + (page + 2) + ChatColor.GOLD + " to read the next page.");
				}
			}
			onComplete().invoke(this, CompleteEventArgs.EMPTY);
			return;
		}
		
		// Help wasn't requested. Try running commands as per usual
		
		String troll = args[0];
		if (args.length > 1) {
			String player = args[1];
			if (CommandUtil.getPlayerByName(troll) != null && CommandUtil.getPlayerByName(player) == null) {
				// Issuer accidentally swapped args. No problem, we'll just swap them back.
				troll = args[1];
				player = args[0];
				
				args[0] = troll;
				args[1] = player;
			}
		} else {
			// Only 1 argument given. This could be a command or a player
			if (CommandUtil.getPlayerByName(args[0]) != null) {
				// It's a player, so it's likely the issuer actually wanted to search. We'll do that for them.
				sender.getServer().dispatchCommand(sender, "troll search " + args[0]);
				return;
			}
		}
		
		PluginCommand run = getCommand(troll, false);
		if (run == null) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_COMMAND));
			onError().invoke(this, new ExceptionEventArgs<InvalidCommandException>(new InvalidCommandException(commandName)));
			return;
		}
		run.start();
	}
	
	protected void onUndo() {
		for (Entry<String, PluginCommand> kvp : initializedCommands.entrySet()) {
			PluginCommand run = kvp.getValue();
			
			run.setSender(sender);
			run.setCommand(command);
			run.setCommandName((command != null) ? command.getName() : null);
			run.setLabel(label);
			run.setArgs(args);
			run.undo();
		}
	}
	
	private PluginCommand getCommand(String command, boolean exact) {
		String key = getRealCommandName(command, exact);
		
		if (key == null) {
			return null;
		}
		if (!key.equalsIgnoreCase(command)) {
			sender.sendMessage(ChatColor.YELLOW + "Running \"" + key + "\" in lieu of \"" + command + "\".");
		}
		
		PluginCommand run = initializedCommands.get(key);
		Class<PluginCommand> c = commands.get(key);
		
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
				ServiceLocator.getService(IExceptionHandler.class).silentException(ex);
				return null;
			}
			
			run.onComplete().attach((s, e) -> onComplete().invoke(this, e));
			run.onError().attach((s, e) -> onError().invoke(this, e));
			
			initializedCommands.put(key, run);
		}
		
		ArrayList<String> newArgs = new ArrayList<String>(Arrays.asList(args));
		newArgs.remove(0);
		
		run.setSender(sender);
		run.setCommand(this.command);
		run.setCommandName(key);
		run.setLabel(label);
		run.setArgs(newArgs.toArray(new String[0]));
		
		return run;
	}
	private String getRealCommandName(String commandName, boolean exact) {
		commandName = commandName.toLowerCase();
		if (commandNames.contains(commandName)) {
			// Found an exact match
			return commandName;
		}
		
		if (exact) {
			// Exact match was requested, but nothing found.
			return null;
		}
		
		// Possible misspelling. Try searching instead.
		String[] search = commandDatabase.getValues(commandDatabase.naturalLanguage(commandName, false), 0);
		
		if (search == null || search.length == 0) {
			return null;
		}
		
		return search[0].toLowerCase();
	}
}
