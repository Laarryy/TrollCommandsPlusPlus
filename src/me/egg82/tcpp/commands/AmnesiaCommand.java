package me.egg82.tcpp.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.AmnesiaMessageRegistry;
import me.egg82.tcpp.services.AmnesiaRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class AmnesiaCommand extends PluginCommand {
	//vars
	private IRegistry amnesiaRegistry = (IRegistry) ServiceLocator.getService(AmnesiaRegistry.class);
	private IRegistry amnesiaMessageRegistry = (IRegistry) ServiceLocator.getService(AmnesiaMessageRegistry.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public AmnesiaCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_AMNESIA)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
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
		
		e(player.getUniqueId().toString(), player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		if (amnesiaRegistry.hasRegister(uuid)) {
			amnesiaRegistry.setRegister(uuid, Player.class, null);
			amnesiaMessageRegistry.setRegister(uuid, List.class, null);
			
			sender.sendMessage(player.getName() + " is no longer an amnesiac.");
		} else {
			amnesiaMessageRegistry.setRegister(uuid, List.class, Collections.synchronizedList(new ArrayList<String>()));
			amnesiaRegistry.setRegister(uuid, Player.class, player);
			metricsHelper.commandWasRun(command.getName());
			
			sender.sendMessage(player.getName() + " is now an amnesiac.");
		}
	}
}
