package me.egg82.tcpp.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.SenderNotAllowedException;
import ninja.egg82.plugin.handlers.MessageHandler;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.ReflectUtil;

public class GTrollCommand extends PluginCommand {
	//vars
	private final Charset UTF_8 = Charset.forName("UTF-8");
	
	private MessageHandler messageHandler = ServiceLocator.getService(MessageHandler.class);
	
	private ArrayList<String> commandNames = new ArrayList<String>();
	private HashMap<String, String> commandDescriptions = new HashMap<String, String>();
	private HashMap<String, PluginCommand> commands = new HashMap<String, PluginCommand>();
	
	int totalPages = -1;
	
	//constructor
	public GTrollCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		String[] list = ((String) ((PluginDescriptionFile) ServiceLocator.getService(InitRegistry.class).getRegister("plugin", JavaPlugin.class).getDescription()).getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
		for (String entry : list) {
			if (entry.contains("-= Available Commands =-")) {
				continue;
			}
			
			String usage = entry.substring(0, entry.indexOf(':')).trim();
			String c = usage.split(" ")[1];
			String description = entry.substring(entry.indexOf(':') + 1).trim();
			
			commandDescriptions.put(c.toLowerCase(), description);
		}
		
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
		
		Collections.sort(commandNames);
		totalPages = (int) Math.ceil(((double) commandNames.size()) / 9.0d);
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_GTROLL)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_GTROLL)));
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.SENDER_NOT_ALLOWED));
			onError().invoke(this, new ExceptionEventArgs<SenderNotAllowedException>(new SenderNotAllowedException(sender, this)));
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
			sender.sendMessage(ChatColor.YELLOW + "---- " + ChatColor.GOLD + "Help: gtroll" + ChatColor.YELLOW + " -- " + ChatColor.GOLD + "Page " + ChatColor.RED + (page + 1) + ChatColor.GOLD + "/" + ChatColor.RED + totalPages + ChatColor.YELLOW + " ----");
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
				sender.getServer().dispatchCommand(sender, "gtroll search " + args[0]);
				return;
			}
		}
		
		e((Player) sender, "troll " + String.join(" ", args));
	}
	private void e(Player sender, String command) {
		messageHandler.sendMessage(sender, "tcpp_command", command.getBytes(UTF_8));
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
