package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.DelayKillRegistry;
import me.egg82.tcpp.services.DelayKillTicksRegistry;
import me.egg82.tcpp.services.DelayKillTimeRegistry;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class DelayKillCommand extends PluginCommand {
	//vars
	private IRegistry delayKillRegistry = (IRegistry) ServiceLocator.getService(DelayKillRegistry.class);
	private IRegistry delayKillTimeRegistry = (IRegistry) ServiceLocator.getService(DelayKillTimeRegistry.class);
	private IRegistry delayKillTicksRegistry = (IRegistry) ServiceLocator.getService(DelayKillTicksRegistry.class);
	
	//constructor
	public DelayKillCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_DELAY_KILL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
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
		
		long delay = 10L;
		
		if (args.length == 2) {
			try {
				delay = Long.parseLong(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				sender.getServer().dispatchCommand(sender, "help " + command.getName());
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
			
			if (delay <= 0L) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				sender.getServer().dispatchCommand(sender, "help " + command.getName());
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
		}
		
		e(player.getUniqueId().toString(), player, delay);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, long delay) {
		if (delayKillRegistry.hasRegister(uuid)) {
			delayKillRegistry.setRegister(uuid, Player.class, null);
			delayKillTimeRegistry.setRegister(uuid, long.class, null);
			delayKillTicksRegistry.setRegister(uuid, long.class, null);
			
			sender.sendMessage(player.getName() + "'s death is no longer inevitable.");
		} else {
			delayKillRegistry.setRegister(uuid, Player.class, player);
			delayKillTimeRegistry.setRegister(uuid, long.class, System.currentTimeMillis());
			delayKillTicksRegistry.setRegister(uuid, long.class, delay);
			
			sender.sendMessage(player.getName() + "'s death is inevitable.");
		}
	}
}
