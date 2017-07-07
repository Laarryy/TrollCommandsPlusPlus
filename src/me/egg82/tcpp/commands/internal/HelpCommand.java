package me.egg82.tcpp.commands.internal;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.CommandSearchDatabase;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.Pair;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.StringUtil;

public class HelpCommand extends PluginCommand {
	//vars
	private HashMap<String, Pair<String, String>> commands = new HashMap<String, Pair<String, String>>();
	private LanguageDatabase ldb = (LanguageDatabase) ServiceLocator.getService(CommandSearchDatabase.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public HelpCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		String[] list = ((String) ((PluginDescriptionFile) ((JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin")).getDescription()).getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
		for (String entry : list) {
			if (entry.contains("-= Available Commands =-")) {
				continue;
			}
			
			String usage = entry.substring(0, entry.indexOf(':')).trim();
			String c = usage.split(" ")[1];
			String description = entry.substring(entry.indexOf(':') + 1).trim();
			
			commands.put(c, new Pair<String, String>(usage, description));
		}
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_HELP)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			e("help", commands.get("help"));
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		String search = "";
		for (int i = 0; i < args.length; i++) {
			search += args[i] + " ";
		}
		search = search.trim();
		
		String command = search.toLowerCase();
		Pair<String, String> commandValues = commands.get(command);
		
		if (command == null) {
			// Might have simply misspelled the command. Search the database.
			String[] list = ldb.getValues(ldb.naturalLanguage(search, false), 0);
			
			if (list == null || list.length == 0) {
				sender.sendMessage(MessageType.COMMAND_NOT_FOUND);
				dispatch(CommandEvent.ERROR, CommandErrorType.COMMAND_NOT_FOUND);
				return;
			}
			
			command = list[0].toLowerCase();
			commandValues = commands.get(command);
		}
		
		e(command, commandValues);
		
		dispatch(CommandEvent.COMPLETE, null);
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
