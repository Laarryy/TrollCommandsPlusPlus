package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidTargetException;
import me.egg82.tcpp.exceptions.InvalidLibraryException;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.services.registries.ControlRegistry;
import me.egg82.tcpp.util.ControlHelper;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.disguise.reflection.IDisguiseHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.exceptions.SenderNotAllowedException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class ControlCommand extends PluginCommand {
	//vars
	private IRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	
	private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
	private ControlHelper controlHelper = ServiceLocator.getService(ControlHelper.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public ControlCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
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
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_CONTROL)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_CONTROL)));
			return;
		}
		if (!disguiseHelper.isValidLibrary()) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_LIBRARY));
			onError().invoke(this, new ExceptionEventArgs<InvalidLibraryException>(new InvalidLibraryException(disguiseHelper)));
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.SENDER_NOT_ALLOWED));
			onError().invoke(this, new ExceptionEventArgs<SenderNotAllowedException>(new SenderNotAllowedException(sender, this)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		Player controller = (Player) sender;
		UUID controllerUuid = controller.getUniqueId();
		
		if (args.length == 0) {
			if (!controlRegistry.hasRegister(controllerUuid)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TARGET));
				onError().invoke(this, new ExceptionEventArgs<InvalidTargetException>(new InvalidTargetException(controller)));
				return;
			}
			
			controlHelper.uncontrol(controllerUuid, controller);
		} else if (args.length == 1) {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			
			UUID playerUuid = player.getUniqueId();
			
			if (controllerUuid.equals(playerUuid)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_TARGET));
				onError().invoke(this, new ExceptionEventArgs<InvalidTargetException>(new InvalidTargetException(controller)));
				return;
			}
			
			Player controlledPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(controllerUuid, UUID.class));
			if (controlledPlayer != null) {
				controlHelper.uncontrol(controllerUuid, controller);
				if (controlledPlayer.getUniqueId().equals(playerUuid)) {
					metricsHelper.commandWasRun(this);
					onComplete().invoke(this, CompleteEventArgs.EMPTY);
					return;
				}
			}
			
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
				onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
				return;
			}
			
			controlHelper.control(controller.getUniqueId(), controller, playerUuid, player);
			
			metricsHelper.commandWasRun(this);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			return;
		}
		
		UUID playerUuid = player.getUniqueId();
		
		for (UUID controllerUuid : controlRegistry.getKeys()) {
			Player controlledPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(controllerUuid, UUID.class));
			
			if (controlledPlayer != null && controlledPlayer.getUniqueId().equals(playerUuid)) {
				controlHelper.uncontrol(controllerUuid, CommandUtil.getPlayerByUuid(controllerUuid));
				break;
			}
		}
	}
}
