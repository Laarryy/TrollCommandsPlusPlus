package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class CometCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public CometCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(true, PermissionsType.COMMAND_COMET, new int[]{0,1}, null)) {
			if (args.length == 0) {
				e((Player) sender, 1.0f);
			} else if (args.length == 1) {
				try {
					e((Player) sender, Float.parseFloat(args[0]));
				} catch (Exception ex) {
					sender.sendMessage(MessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
					return;
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player, float power) {
		Fireball fireball = (Fireball) player.launchProjectile(Fireball.class);
		fireball.setYield(power);
		fireball.setShooter(player);
	}
}