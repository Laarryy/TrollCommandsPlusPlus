package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.utils.MathUtil;

public class GolemCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public GolemCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_GOLEM, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		int rand = MathUtil.fairRoundedRandom(10, 15);
		Location loc = player.getLocation();
		
		for (int i = 0; i < rand; i++) {
			Location r = BlockUtil.getTopAirBlock(new Location(loc.getWorld(), MathUtil.random(loc.getX() - 10.0d, loc.getX() + 10.0d), loc.getY(), MathUtil.random(loc.getZ() - 10.0d, loc.getZ() + 10.0d)));
			Vector velocity = r.clone().subtract(player.getLocation()).toVector().normalize().multiply(1.0d);
			IronGolem g = (IronGolem) player.getWorld().spawn(r, IronGolem.class);
			g.setVelocity(velocity);
			g.setTarget(player);
		}
		
		sender.sendMessage(name + " is now having fun with golems.");
	}
}
