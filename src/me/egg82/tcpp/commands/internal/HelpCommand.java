package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidCommandException;
import me.egg82.tcpp.services.databases.CommandSearchDatabase;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.Pair;
import ninja.egg82.plugin.BasePlugin;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.StringUtil;

public class HelpCommand extends PluginCommand {
	//vars
	private ArrayList<String> commandNames = new ArrayList<String>();
	private HashMap<String, Pair<String, String>> commands = new HashMap<String, Pair<String, String>>();
	private LanguageDatabase commandDatabase = ServiceLocator.getService(CommandSearchDatabase.class);
	
	private BasePlugin plugin = ServiceLocator.getService(BasePlugin.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public HelpCommand() {
		super();
		
		String[] list = ((String) ((PluginDescriptionFile) ServiceLocator.getService(JavaPlugin.class).getDescription()).getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
		for (String entry : list) {
			if (entry.contains("-= Available Commands =-")) {
				continue;
			}
			
			String usage = entry.substring(0, entry.indexOf(':')).trim();
			String c = usage.split(" ")[1];
			String description = entry.substring(entry.indexOf(':') + 1).trim();
			
			commandNames.add(c);
			commands.put(c, new Pair<String, String>(usage, description));
		}
	}
	
	//public
	public List<String> tabComplete() {
		if (args.length == 0 || args[0].isEmpty()) {
			return commandNames;
		} else if (args.length == 1) {
			ArrayList<String> retVal = new ArrayList<String>();
			for (int i = 0; i < commandNames.size(); i++) {
				if (commandNames.get(i).startsWith(args[0].toLowerCase())) {
					retVal.add(commandNames.get(i));
				}
			}
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_HELP)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_HELP)));
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			e("help", commands.get("help"));
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		String search = "";
		for (int i = 0; i < args.length; i++) {
			search += args[i] + " ";
		}
		search = search.trim();
		
		String command = search.toLowerCase();
		Pair<String, String> commandValues = commands.get(command);
		
		if (commandValues == null) {
			// Might have simply misspelled the command. Search the database.
			String[] list = commandDatabase.getValues(commandDatabase.naturalLanguage(search, false), 0);
			
			if (list == null || list.length == 0) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_COMMAND));
				onError().invoke(this, new ExceptionEventArgs<InvalidCommandException>(new InvalidCommandException(search)));
				return;
			}
			
			command = list[0].toLowerCase();
			commandValues = commands.get(command);
			
			if (commandValues == null) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_COMMAND));
				onError().invoke(this, new ExceptionEventArgs<InvalidCommandException>(new InvalidCommandException(search)));
				return;
			}
		}
		
		e(command, commandValues);
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(String command, Pair<String, String> commandValues) {
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(ChatColor.YELLOW + "--------- " + ChatColor.WHITE + "Help: /troll " + command + " " + ChatColor.YELLOW + StringUtil.repeatChar('-', 25 - command.length()));
		sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.WHITE + commandValues.getRight());
		sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.WHITE + commandValues.getLeft());
	}
	
	protected void onUndo() {
		
	}
}
