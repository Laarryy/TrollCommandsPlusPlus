package me.egg82.tcpp.commands.internal;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class CometCommand extends PluginCommand {
	//vars
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public CometCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_COMET)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1, 2)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(SpigotMessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.CONSOLE_NOT_ALLOWED);
			return;
		}
		
		Player player = (Player) sender;
		double speed = 2.0d;
		double power = 1.0d;
		
		if (args.length == 1) {
			try {
				speed = Double.parseDouble(args[0]);
			} catch (Exception ex) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
		}
		if (args.length == 2) {
			try {
				power = Double.parseDouble(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
		}
		
		e(player, speed, power);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(Player player, double speed, double power) {
		Fireball fireball = player.launchProjectile(Fireball.class);
		fireball.setYield((float) power);
		fireball.setIsIncendiary((power > 0.0d) ? true : false);
		fireball.setShooter(player);
		Vector direction = player.getLocation().getDirection().multiply(speed);
		fireball.setVelocity(direction);
		
		metricsHelper.commandWasRun(this);
	}
	
	protected void onUndo() {
		
	}
}