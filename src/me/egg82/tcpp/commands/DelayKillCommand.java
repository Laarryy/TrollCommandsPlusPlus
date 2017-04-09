package me.egg82.tcpp.commands;

import java.util.EnumMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.DelayKillRegistry;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.startup.InitRegistry;

public class DelayKillCommand extends PluginCommand {
	//vars
	private IRegistry delayKillRegistry = (IRegistry) ServiceLocator.getService(DelayKillRegistry.class);
	
	private IRegistry initRegistry = (IRegistry) ServiceLocator.getService(InitRegistry.class);
	
	//constructor
	public DelayKillCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_DELAY_KILL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1, 2)) {
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
		
		long delay = 10L;
		
		if (args.length == 2) {
			try {
				delay = Long.parseLong(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				sender.getServer().dispatchCommand(sender, "help " + command.getName());
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
			
			if (delay <= 0L) {
				sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
				sender.getServer().dispatchCommand(sender, "help " + command.getName());
				dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
				return;
			}
		}
		
		e(player.getUniqueId().toString(), player, delay);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, long delay) {
		if (delayKillRegistry.hasRegister(uuid)) {
			delayKillRegistry.setRegister(uuid, Player.class, null);
			
			sender.sendMessage(player.getName() + "'s death is no longer inevitable.");
		} else {
			delayKillRegistry.setRegister(uuid, Player.class, player);
			
			// Wait Xx20 ticks
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
				public void run() {
					// Is the player still in the registry, or have they been removed for one reason or another?
					if (delayKillRegistry.hasRegister(uuid)) {
						// Kill them!
						player.setHealth(0.0d);
						EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, Double.MAX_VALUE)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(Double.MAX_VALUE))));
						Bukkit.getPluginManager().callEvent(damageEvent);
						damageEvent.getEntity().setLastDamageCause(damageEvent);
					}
				}
			}, delay * 20);
			
			sender.sendMessage(player.getName() + "'s death is inevitable.");
		}
	}
}
