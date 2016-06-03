package me.egg82.tcpp.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.plugin.commands.PluginCommand;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;

public class LureCommand extends PluginCommand {
	//vars
	
	//constructor
	public LureCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (sender instanceof Player && !permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_LURE)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 1) {
			lure(Bukkit.getPlayer(args[0]));
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	private void lure(Player player) {
		if (player == null) {
			sender.sendMessage(MessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (permissionsManager.playerHasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		List<Entity> entities = player.getNearbyEntities(1000.0d, 1000.0d, 1000.0d);
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
					System.out.println(ex.getMessage());
				}
				Vector velocity = e.getLocation().clone().subtract(player.getLocation()).toVector().normalize().multiply(1.0d);
				e.setVelocity(velocity);
			}
		}
		
		sender.sendMessage("Nearby monsters have been lured to " + player.getName() + ".");
		
		dispatch(CommandEvent.COMPLETE, null);
	}
}
