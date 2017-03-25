package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.utils.MathUtil;

public class LiftCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public LiftCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_LIFT, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		player.setVelocity(new Vector(0.0d, MathUtil.random(15.0d, 25.0d), 0.0d));
		
		sender.sendMessage(name + " has been lifted.");
	}
}
