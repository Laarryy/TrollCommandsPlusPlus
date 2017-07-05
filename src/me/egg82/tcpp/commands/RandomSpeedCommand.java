package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.RandomSpeedRegistry;
import me.egg82.tcpp.services.RandomSpeedSaveRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.Pair;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class RandomSpeedCommand extends PluginCommand {
	//vars
	private IRegistry randomSpeedRegistry = (IRegistry) ServiceLocator.getService(RandomSpeedRegistry.class);
	private IRegistry randomSpeedSaveRegistry = (IRegistry) ServiceLocator.getService(RandomSpeedSaveRegistry.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public RandomSpeedCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_RANDOM_SPEED)) {
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
		
		String uuid = player.getUniqueId().toString();
		
		if (!randomSpeedRegistry.hasRegister(uuid)) {
			e(uuid, player);
		} else {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		randomSpeedRegistry.setRegister(uuid, Player.class, player);
		randomSpeedSaveRegistry.setRegister(uuid, Pair.class, new Pair<Float, Float>(player.getWalkSpeed(), player.getFlySpeed()));
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage(player.getName() + " is now finding it hard to control themselves!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		String uuid = player.getUniqueId().toString();
		
		if (randomSpeedRegistry.hasRegister(uuid)) {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	@SuppressWarnings("unchecked")
	private void eUndo(String uuid, Player player) {
		randomSpeedRegistry.setRegister(uuid, Player.class, null);
		Pair<Float, Float> originalSpeed = (Pair<Float, Float>) randomSpeedSaveRegistry.getRegister(uuid);
		player.setWalkSpeed(originalSpeed.getLeft());
		player.setFlySpeed(originalSpeed.getRight());
		randomSpeedSaveRegistry.setRegister(uuid, Pair.class, null);
		
		sender.sendMessage(player.getName() + " is no longer finding it hard to control themselves.");
	}
}
