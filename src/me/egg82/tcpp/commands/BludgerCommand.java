package me.egg82.tcpp.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.BludgerBallRegistry;
import me.egg82.tcpp.services.BludgerRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class BludgerCommand extends PluginCommand {
	//vars
	private IRegistry bludgerRegistry = (IRegistry) ServiceLocator.getService(BludgerRegistry.class);
	private IRegistry bludgerBallRegistry = (IRegistry) ServiceLocator.getService(BludgerBallRegistry.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public BludgerCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_BLUDGER)) {
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
		
		if (!bludgerRegistry.hasRegister(uuid)) {
			e(uuid, player);
		} else {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		bludgerRegistry.setRegister(uuid, Player.class, player);
		
		Location playerLocation = player.getLocation();
		
		Location location = null;
		if (sender instanceof Player) {
			if (!((Player) sender).getUniqueId().toString().equals(uuid)) {
				Location senderLocation = ((Player) sender).getLocation();
				if (senderLocation.getWorld().getName().equals(playerLocation.getWorld().getName())) {
					if (senderLocation.distanceSquared(playerLocation) <= 900.0d) { // 30
						location = LocationUtil.getLocationInFront(senderLocation, 1.5d);
					}
				}
			}
		}
		
		if (location == null) {
			location = LocationUtil.getRandomPointAround(playerLocation, MathUtil.fairRoundedRandom(10, 14));
		}
		
		Fireball fireball = player.getWorld().spawn(location, Fireball.class);
		fireball.setVelocity(playerLocation.toVector().subtract(location.toVector()).normalize().multiply(0.35d));
		bludgerBallRegistry.setRegister(uuid, Fireball.class, fireball);
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage(player.getName() + " is now being chased by a bludger!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		String uuid = player.getUniqueId().toString();
		
		if (bludgerRegistry.hasRegister(uuid)) {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void eUndo(String uuid, Player player) {
		bludgerRegistry.setRegister(uuid, Player.class, null);
		
		((Fireball) bludgerBallRegistry.getRegister(uuid)).remove();
		bludgerBallRegistry.setRegister(uuid, Fireball.class, null);
		
		sender.sendMessage(player.getName() + " is no longer being chased by a bludger.");
	}
}
