package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlRegistry;
import me.egg82.tcpp.util.ControlHelper;
import me.egg82.tcpp.util.IDisguiseHelper;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlCommand extends PluginCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(ControlRegistry.class);
	
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(IDisguiseHelper.class);
	private ControlHelper controlHelper = (ControlHelper) ServiceLocator.getService(ControlHelper.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public ControlCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_CONTROL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		if (!disguiseHelper.isValidLibrary()) {
			sender.sendMessage(MessageType.NO_LIBRARY);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_LIBRARY);
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(SpigotMessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.CONSOLE_NOT_ALLOWED);
			return;
		}
		
		Player controller = (Player) sender;
		String controllerUuid = controller.getUniqueId().toString();
		
		if (args.length == 0) {
			if (!controlRegistry.hasRegister(controllerUuid)) {
				sender.sendMessage(MessageType.NOT_CONTROLLING);
				dispatch(CommandEvent.ERROR, CommandErrorType.NOT_CONTROLLING);
				return;
			}
			
			controlHelper.uncontrol(controllerUuid, controller);
		} else if (args.length == 1) {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
				return;
			}
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(MessageType.PLAYER_IMMUNE);
				dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
				return;
			}
			
			String playerUuid = player.getUniqueId().toString();
			
			if (controllerUuid.equals(playerUuid)) {
				sender.sendMessage(MessageType.NO_CONTROL_SELF);
				dispatch(CommandEvent.ERROR, CommandErrorType.NO_CONTROL_SELF);
				return;
			}
			
			Player controlledPlayer = (Player) controlRegistry.getRegister(controllerUuid);
			if (controlledPlayer != null) {
				controlHelper.uncontrol(controllerUuid, controller);
				if (controlledPlayer.getUniqueId().toString().equals(playerUuid)) {
					metricsHelper.commandWasRun(command.getName());
					dispatch(CommandEvent.COMPLETE, null);
					return;
				}
			}
			
			controlHelper.control(controller.getUniqueId().toString(), controller, playerUuid, player);
			
			metricsHelper.commandWasRun(command.getName());
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
}
