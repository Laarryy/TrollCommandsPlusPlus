package me.egg82.tcpp.commands;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;

public class CometCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public CometCommand() {
		super();
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
					sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
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