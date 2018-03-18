package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.BukkitReflectUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class RandomCommand extends PluginCommand {
	//vars
	private ArrayList<String> commandNames = new ArrayList<String>();
	private HashMap<String, Class<PluginCommand>> commands = new HashMap<String, Class<PluginCommand>>();
	private HashMap<String, PluginCommand> initializedCommands = new HashMap<String, PluginCommand>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	private BiConsumer<Object, CompleteEventArgs<?>> bubbleComplete = (s, e) -> onComplete().invoke(this, e);
	private BiConsumer<Object, ExceptionEventArgs<?>> bubbleError = (s, e) -> onError(s, e);
	
	//constructor
	public RandomCommand() {
		super();
		
		// Collect all internal commands and names, then map the commands
		List<Class<PluginCommand>> classes = ReflectUtil.getClasses(PluginCommand.class, "me.egg82.tcpp.commands.internal", true, false, false);
		Map<String, String> names = BukkitReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.commands.internal", null, "Command");
		for (Class<PluginCommand> c : classes) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_RANDOM)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_RANDOM)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player.getName());
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
			
			e(player.getName());
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(String player) {
		PluginCommand command = getCommand(commandNames.get(MathUtil.fairRoundedRandom(0, commandNames.size() - 1)));
		sender.getServer().dispatchCommand(sender, "troll " + command + " " + player);
		
		metricsHelper.commandWasRun(this);
	}
	protected void onUndo() {
		
	}
	
	private PluginCommand getCommand(String command) {
		String key = getRealCommandName(command);
		
		if (key == null) {
			return null;
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
			
			run.onComplete().attach(bubbleComplete);
			run.onError().attach(bubbleError);
			
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
	private String getRealCommandName(String commandName) {
		commandName = commandName.toLowerCase();
		if (commandNames.contains(commandName)) {
			// Found an exact match
			return commandName;
		}
		
		return null;
	}
	
	private void onError(Object sender, ExceptionEventArgs<?> e) {
		boolean redo = false;
		
		if (e.getException() instanceof IncorrectCommandUsageException) {
			String key = null;
			for (Entry<String, PluginCommand> kvp : initializedCommands.entrySet()) {
				if (kvp.getValue().equals(sender)) {
					key = kvp.getKey();
					break;
				}
			}
			if (key != null) {
				initializedCommands.remove(key);
				commands.remove(key);
				commandNames.remove(key);
			}
			redo = true;
		} else if (e.getException() instanceof InvalidPermissionsException) {
			redo = true;
		}
		
		if (redo) {
			e(((PluginCommand) sender).getArgs()[0]);
		}
		
		onError().invoke(this, e);
	}
}