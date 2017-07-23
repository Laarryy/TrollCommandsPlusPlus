package me.egg82.tcpp.commands.internal;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.SenderNotAllowedException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

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
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_COMET)));
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.SENDER_NOT_ALLOWED));
			onError().invoke(this, new ExceptionEventArgs<SenderNotAllowedException>(new SenderNotAllowedException(sender, this)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1, 2)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		Player player = (Player) sender;
		double speed = 2.0d;
		double power = 1.0d;
		
		if (args.length == 1) {
			try {
				speed = Double.parseDouble(args[0]);
			} catch (Exception ex) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
				return;
			}
		}
		if (args.length == 2) {
			try {
				power = Double.parseDouble(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
				return;
			}
		}
		
		e(player, speed, power);
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
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