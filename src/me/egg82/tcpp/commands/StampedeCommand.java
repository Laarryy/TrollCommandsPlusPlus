package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.SpigotReflectType;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.reflection.entity.interfaces.IEntityUtil;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class StampedeCommand extends BasePluginCommand {
	//vars
	private IEntityUtil entityUtil = (IEntityUtil) ((IRegistry) ServiceLocator.getService(SpigotServiceType.REFLECT_REGISTRY)).getRegister(SpigotReflectType.ENTITY);
	
	//constructor
	public StampedeCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_STAMPEDE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		int rand = MathUtil.fairRoundedRandom(10, 20);
		Location tloc = player.getLocation();
		Location loc = BlockUtil.getTopAirBlock(new Location(tloc.getWorld(), MathUtil.random(tloc.getX() - 5.0d, tloc.getX() + 5.0d), tloc.getY(), MathUtil.random(tloc.getZ() - 5.0d, tloc.getZ() + 5.0d)));
		Vector vel = loc.clone().subtract(tloc).toVector().normalize().multiply(3.0d);
		for (int i = 0; i < rand; i++) {
			spawnCow(player, loc, vel);
		}
		
		sender.sendMessage("The angry cows have been unleashed on " + name + ".");
	}
	private void spawnCow(Player p, Location l, Vector v) {
		Cow cow = (Cow) p.getWorld().spawn(l, Cow.class);
		Silverfish fish = (Silverfish) p.getWorld().spawn(l, Silverfish.class);
		fish.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 3), true);
		entityUtil.addPassenger(fish, cow);
		fish.setVelocity(v);
		cow.setVelocity(v);
		fish.setTarget(p);
	}
}
