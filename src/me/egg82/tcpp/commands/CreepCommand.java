package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.utils.MathUtil;

public class CreepCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public CreepCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_CREEP, new int[]{1}, new int[]{0})) {
			e(Bukkit.getPlayer(args[0]));
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player) {
		int rand = MathUtil.fairRoundedRandom(2, 5);
		for (int i = 0; i < rand; i++) {
			Creeper c = (Creeper) player.getWorld().spawn(player.getLocation(), Creeper.class);
			c.setPowered(true);
			c.setTarget(player);
		}
		
		sender.sendMessage(player.getName() + " is now playing with explosives.");
	}
}