package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.reflection.disguise.IDisguiseHelper;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class ScareCommand extends PluginCommand {
	//vars
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(IDisguiseHelper.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public ScareCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_SCARE)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0)) {
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
		
		Player player = (Player) sender;
		EntityType type = disguiseHelper.disguiseType(player);
		
		if (type == null) {
			disguiseHelper.disguiseAsEntity(player, EntityType.CREEPER);
			metricsHelper.commandWasRun(command.getName());
			
			sender.sendMessage("You are now a creeper. Ooh, scary!");
		} else if (type == EntityType.CREEPER) {
			disguiseHelper.undisguise(player);
			
			sender.sendMessage("You are no longer a creeper.");
		} else {
			sender.sendMessage(MessageType.ALREADY_DISGUISED);
			dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_DISGUISED);
			return;
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	
	protected void onUndo() {
		
	}
}
