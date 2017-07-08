package me.egg82.tcpp.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.CommandSearchDatabase;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.ReflectUtil;

public class TrollCommand extends PluginCommand {
	//vars
	private LanguageDatabase commandDatabase = (LanguageDatabase) ServiceLocator.getService(CommandSearchDatabase.class);
	private ArrayList<String> commandNames = new ArrayList<String>();
	private HashMap<String, PluginCommand> commands = new HashMap<String, PluginCommand>();
	
	//constructor
	public TrollCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		// List all classes in the command package
		List<Class<? extends PluginCommand>> temp = ReflectUtil.getClasses(PluginCommand.class, "me.egg82.tcpp.commands.internal");
		for (int i = 0; i < temp.size(); i++) {
			// Attempt to instantiate the command
			PluginCommand run = null;
			try {
				run = temp.get(i).getDeclaredConstructor(CommandSender.class, Command.class, String.class, String[].class).newInstance(sender, command, label, args);
			} catch (Exception ex) {
				continue;
			}
			
			// Put the command in a map for fast lookup/retrieval
			String name = temp.get(i).getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			commandNames.add(name);
			commands.put(name, run);
		}
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
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
		ArrayList<String> newArgs = new ArrayList<String>(Arrays.asList(args));
		newArgs.remove(0);
		
		return (commands.containsKey(commandName)) ? commands.get(commandName).tabComplete(sender, command, label, newArgs.toArray(new String[0])) : null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_TROLL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
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
		
		// Chop off the command name from the argument list
		ArrayList<String> newArgs = new ArrayList<String>(Arrays.asList(args));
		newArgs.remove(0);
		
		e(troll.toLowerCase(), newArgs.toArray(new String[0]));
	}
	private void e(String commandName, String[] args) {
		PluginCommand run = commands.get(commandName);
		
		if (run == null) {
			// Possible misspelling. Try searching instead.
			String[] search = commandDatabase.getValues(commandDatabase.naturalLanguage(commandName, false), 0);
			
			if (search == null || search.length == 0) {
				sender.sendMessage(MessageType.COMMAND_NOT_FOUND);
				dispatch(CommandEvent.ERROR, CommandErrorType.COMMAND_NOT_FOUND);
				return;
			}
			
			run = commands.get(search[0].toLowerCase());
			if (run == null) {
				sender.sendMessage(MessageType.COMMAND_NOT_FOUND);
				dispatch(CommandEvent.ERROR, CommandErrorType.COMMAND_NOT_FOUND);
				return;
			}
		}
		
		// Set and run new command
		run.setSender(sender);
		run.setCommand(command);
		run.setLabel(label);
		run.setArgs(args);
		run.start();
	}
	
	protected void onUndo() {
		for (Entry<String, PluginCommand> kvp : commands.entrySet()) {
			PluginCommand run = kvp.getValue();
			
			run.setSender(sender);
			run.setArgs(args);
			run.undo();
		}
	}
}
