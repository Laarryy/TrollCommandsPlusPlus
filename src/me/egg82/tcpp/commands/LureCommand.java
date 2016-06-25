package me.egg82.tcpp.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class LureCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public LureCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_LURE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		List<Entity> entities = player.getNearbyEntities(1000.0d, 512.0d, 1000.0d);
		for (Entity e : entities) {
			EntityType type = e.getType();
			if (
					type == EntityType.BLAZE ||
					type == EntityType.CAVE_SPIDER ||
					type == EntityType.CREEPER ||
					type == EntityType.ENDER_DRAGON ||
					type == EntityType.ENDERMAN ||
					type == EntityType.ENDERMITE ||
					type == EntityType.GHAST || 
					type == EntityType.GIANT ||
					type == EntityType.MAGMA_CUBE ||
					type == EntityType.PIG_ZOMBIE ||
					type == EntityType.SHULKER ||
					type == EntityType.SILVERFISH ||
					type == EntityType.SKELETON ||
					type == EntityType.SLIME ||
					type == EntityType.SPIDER ||
					type == EntityType.WITCH ||
					type == EntityType.WITHER ||
					type == EntityType.ZOMBIE) {
				
				if (type == EntityType.PIG_ZOMBIE) {
					PigZombie pig = (PigZombie) e;
					pig.setAngry(true);
				}
				
				try {
					((Creature) e).setTarget(player);
				} catch (Exception ex) {
					
				}
				
				Vector velocity = e.getLocation().clone().subtract(player.getLocation()).toVector().normalize().multiply(1.0d);
				e.setVelocity(velocity);
			}
		}
		
		sender.sendMessage("Nearby monsters have been lured to " + name + ".");
	}
}
