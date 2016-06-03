package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.plugin.commands.PluginCommand;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;

public class CometCommand extends PluginCommand {
	//vars
	
	//constructor
	public CometCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, CommandErrorType.CONSOLE_NOT_ALLOWED);
			return;
		}
		if (!permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_USE)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 0) {
			comet((Player) sender, 1.0f);
		} else if (args.length == 1) {
			try {
				comet((Player) sender, Float.parseFloat(args[0]));
			} catch (Exception ex) {
				sender.sendMessage(MessageType.INCORRECT_USAGE);
				sender.getServer().dispatchCommand(sender, "help " + command.getName());
				dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
			}
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	private void comet(Player player, float power) {
		Fireball fireball = (Fireball) player.launchProjectile(Fireball.class);
		fireball.setYield(power);
		fireball.setShooter(player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
}