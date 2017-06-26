package me.egg82.tcpp.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class LureCommand extends PluginCommand {
	//vars
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public LureCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_LURE)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		e(player.getUniqueId().toString(), player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
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
					type == EntityType.ZOMBIE
			) {
				if (type == EntityType.PIG_ZOMBIE) {
					PigZombie pig = (PigZombie) e;
					pig.setAngry(true);
				}
				
				try {
					((Creature) e).setTarget(player);
				} catch (Exception ex) {
					
				}
				
				e.setVelocity(LocationUtil.moveSmoothly(e.getLocation(), player.getLocation()));
			}
		}
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage("Nearby monsters have been lured to " + player.getName() + ".");
	}
	
	protected void onUndo() {
		
	}
}
