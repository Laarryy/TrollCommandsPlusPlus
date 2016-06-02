package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.plugin.commands.PluginCommand;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;

public class CannonCommand extends PluginCommand {
	//vars
	
	//constructor
	public CannonCommand(CommandSender sender, Command command, String label, String[] args) {
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
		if (!permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_BOMB)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 0) {
			cannon((Player) sender, 2.0d);
		} else if (args.length == 1) {
			try {
				cannon((Player) sender, Double.parseDouble(args[1]));
			} catch (Exception ex) {
				sender.sendMessage(MessageType.INCORRECT_USAGE);
				dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
			}
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	
	private void cannon(Player player, double speed) {
		if (permissionsManager.playerHasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		Vector direction = player.getLocation().getDirection().multiply(speed);
		TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
		tnt.setVelocity(direction);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
}