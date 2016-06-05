package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.utils.MathUtil;

public class StampedeCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public StampedeCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_STAMPEDE, new int[]{1}, new int[]{0})) {
			if (args.length == 1) {
				e(Bukkit.getPlayer(args[0]));
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player) {
		int rand = MathUtil.fairRoundedRandom(10, 20);
		Location tloc = player.getLocation();
		Location loc = BlockUtil.getTopAirBlock(new Location(tloc.getWorld(), MathUtil.random(tloc.getX() - 5.0d, tloc.getX() + 5.0d), tloc.getY(), MathUtil.random(tloc.getZ() - 5.0d, tloc.getZ() + 5.0d)));
		Vector vel = loc.clone().subtract(tloc).toVector().normalize().multiply(3.0d);
		for (int i = 0; i < rand; i++) {
			spawnCow(player, loc, vel);
		}
		
		sender.sendMessage("The angry cows have been unleashed on " + player.getName() + ".");
	}
	private void spawnCow(Player p, Location l, Vector v) {
		Cow cow = (Cow) p.getWorld().spawn(l, Cow.class);
		Silverfish fish = (Silverfish) p.getWorld().spawn(l, Silverfish.class);
		fish.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 3), true);
		fish.setPassenger(cow);
		fish.setVelocity(v);
		cow.setVelocity(v);
		fish.setTarget(p);
	}
}
