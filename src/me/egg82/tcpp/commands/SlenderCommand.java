package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.utils.MathUtil;

public class SlenderCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public SlenderCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_SLENDER, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		Location loc = player.getLocation();
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 500, 3), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 500, 3), true);
		
		Location r = BlockUtil.getTopAirBlock(new Location(loc.getWorld(), MathUtil.random(loc.getX() - 10.0d, loc.getX() + 10.0d), loc.getY(), MathUtil.random(loc.getZ() - 10.0d, loc.getZ() + 10.0d)));
		Enderman e = (Enderman) player.getWorld().spawn(r, Enderman.class);
		e.setTarget(player);
		
		sender.sendMessage(name + " is now playing Slender.");
	}
}
